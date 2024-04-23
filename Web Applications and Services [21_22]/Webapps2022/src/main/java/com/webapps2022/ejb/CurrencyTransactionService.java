/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.ejb;

import com.webapps2022.entity.CurrencyEnum;
import com.webapps2022.entity.SystemUser;
import com.webapps2022.entity.CurrencyTransaction;
import com.webapps2022.thrift.TimestampService;
import java.time.Instant;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author blankie
 */
@Stateless
@TransactionAttribute(REQUIRED)
public class CurrencyTransactionService {

    @PersistenceContext
    EntityManager em;

    public CurrencyTransactionService() {
    }

    @RolesAllowed({"users"})
    private synchronized CurrencyTransaction createTransaction(SystemUser toSystemUser, SystemUser fromSystemUser,
            Float currencyCountTo, Float currencyCountFrom, CurrencyEnum currencyTypeTo, CurrencyEnum currencyTypeFrom,
            Boolean finalised, Instant timeStamp) {
        return new CurrencyTransaction(toSystemUser, fromSystemUser, currencyCountTo,
                currencyCountFrom, currencyTypeTo, currencyTypeFrom, timeStamp, finalised);
    }

    @RolesAllowed({"users"})
    public synchronized String acceptRequest(CurrencyTransaction pendingTransaction) {
        SystemUser fromUser = pendingTransaction.getFromSystemUser();
        SystemUser toUser = pendingTransaction.getToSystemUser();

        Float currencyCount = pendingTransaction.getCurrencyCountFrom();
        if (fromUser.getCurrencyCount() < currencyCount) {
            return "Not enough money in balance.";
        }

        toUser.setCurrencyCount(toUser.getCurrencyCount() + pendingTransaction.getCurrencyCountTo());
        fromUser.setCurrencyCount(fromUser.getCurrencyCount() - pendingTransaction.getCurrencyCountFrom());
        pendingTransaction.setCompleted(true);

        em.merge(pendingTransaction);
        em.merge(fromUser);
        em.merge(toUser);
        em.flush();

        return "Success!";

    }

    @RolesAllowed({"users"})
    public synchronized String denyRequest(CurrencyTransaction pendingTransaction) {
        em.remove(em.find(CurrencyTransaction.class, pendingTransaction.getId()));
        em.flush();

        return "Success!";
    }

    @RolesAllowed({"users"})
    public synchronized String sendPayment(String toUsername, String fromUsername, Float currencyCount) {
        SystemUser toUser = getUserByUsername(toUsername);
        SystemUser fromUser = getUserByUsername(fromUsername);

        if (toUser == null || fromUser == null) {
            return "No such user.";
        }
        if (fromUser.getCurrencyCount().compareTo(currencyCount) < 0) {
            return "Not enough money in balance.";
        }

        Instant timestamp = getTimestamp();
        if (timestamp == null) {
            return "Error: thrift server not running.";
        }

        Float fromCurrency = CurrencyEnum.convertCurrency(fromUser.getCurrencyType(), toUser.getCurrencyType());

        Float currencyCountTo = currencyCount * fromCurrency;
        Float currencyCountFrom = currencyCount;

        toUser.setCurrencyCount(toUser.getCurrencyCount() + currencyCountTo);
        fromUser.setCurrencyCount(fromUser.getCurrencyCount() - currencyCountFrom);

        em.persist(fromUser);
        em.persist(toUser);
        em.persist(createTransaction(toUser, fromUser, currencyCountTo, currencyCountFrom, toUser.getCurrencyType(),
                fromUser.getCurrencyType(), true, timestamp));
        em.flush();

        return "Success!";
    }

    @RolesAllowed({"users"})
    public synchronized String requestPayment(String toUsername, String fromUsername, Float currencyCount) {
        SystemUser toUser = getUserByUsername(toUsername);
        SystemUser fromUser = getUserByUsername(fromUsername);

        if (toUser == null || fromUser == null) {
            return "No such user.";
        }

        Instant timestamp = getTimestamp();
        if (timestamp == null) {
            return "Error: thrift server not running.";
        }

        Float fromCurrency = CurrencyEnum.convertCurrency(fromUser.getCurrencyType(), toUser.getCurrencyType());
        Float currencyCountTo = currencyCount * fromCurrency;
        Float currencyCountFrom = currencyCount;

        em.persist(createTransaction(toUser, fromUser, currencyCountTo, currencyCountFrom, toUser.getCurrencyType(),
                fromUser.getCurrencyType(), false, timestamp));
        em.flush();

        return "Sent request!";

    }

    @RolesAllowed({"users"})
    public synchronized SystemUser getUserByUsername(String username) {
        TypedQuery<SystemUser> query = em.createNamedQuery("getUserByUsername", SystemUser.class);
        query.setParameter("username", username);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @RolesAllowed({"users"})
    public synchronized List<CurrencyTransaction> getUserRequestsSending(String username) {
        TypedQuery<CurrencyTransaction> query = em.createNamedQuery("getUserRequestsSending", CurrencyTransaction.class);
        query.setParameter("systemUser", getUserByUsername(username));

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @RolesAllowed({"users"})
    public synchronized List<CurrencyTransaction> getUserCompletedTransactions(String username) {
        TypedQuery<CurrencyTransaction> query = em.createNamedQuery("getUserCompletedTransactions", CurrencyTransaction.class);
        query.setParameter("systemUser", getUserByUsername(username));

        try {
            List<CurrencyTransaction> stuff = query.getResultList();
            System.out.println("Got stuff");
            System.out.println(stuff);
            return stuff;
        } catch (NoResultException e) {
            return null;
        }
    }

    @RolesAllowed({"admins"})
    public synchronized List<SystemUser> getAllUsers() {
        TypedQuery<SystemUser> query = em.createNamedQuery("getAllUsers", SystemUser.class);

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @RolesAllowed({"admins"})
    public synchronized List<CurrencyTransaction> getAllTransactions() {
        TypedQuery<CurrencyTransaction> query = em.createNamedQuery("getAllTransactions", CurrencyTransaction.class);

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    @RolesAllowed({"users"})
    public synchronized Instant getTimestamp() {
        try {
            TTransport transport;

            transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            TimestampService.Client client = new TimestampService.Client(protocol);

            long result = client.getTimestamp();
            transport.close();

            return Instant.ofEpochMilli(result);

        } catch (TException x) {
            System.err.println(x);
        }

        return null;
    }
}
