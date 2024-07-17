/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.jsf;

import com.webapps2022.ejb.CurrencyTransactionService;
import com.webapps2022.entity.CurrencyEnum;
import com.webapps2022.entity.CurrencyTransaction;
import com.webapps2022.entity.SystemUser;

import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author blankie
 */
@Named
@RequestScoped
public class CurrencyTransactionBean {

    @EJB
    CurrencyTransactionService transactionService;

    private String otherUsername;
    private Float currencyCount;

    public CurrencyTransactionService getTransactionService() {
        return transactionService;
    }

    public void setTransactionService(CurrencyTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public Float getCurrencyCount() {
        return currencyCount;
    }

    public void setCurrencyCount(Float currencyCount) {
        this.currencyCount = currencyCount;
    }

    public String getUserCurrencyCount(String username) {
        SystemUser user = transactionService.getUserByUsername(username);
        return CurrencyEnum.getCurrencySymbol(user.getCurrencyType()) + user.getCurrencyCount();
    }

    public String getUserCurrencySymbol(String username) {
        SystemUser user = transactionService.getUserByUsername(username);
        return CurrencyEnum.getCurrencySymbol(user.getCurrencyType());
    }

    public void sendPayment(String currentUsername) {
        String returned = transactionService.sendPayment(otherUsername, currentUsername, currencyCount);
        currencyCommon(returned);
    }

    public void requestPayment(String currentUsername) {
        String returned = transactionService.requestPayment(currentUsername, otherUsername, currencyCount);
        currencyCommon(returned);
    }

    public void acceptRequest(CurrencyTransaction transaction) {
        String returned = transactionService.acceptRequest(transaction);
        currencyCommon(returned);
    }

    public void denyRequest(CurrencyTransaction transaction) {
        String returned = transactionService.denyRequest(transaction);
        currencyCommon(returned);
    }

    public void currencyCommon(String returned) {
        FacesMessage.Severity faceMessageType;
        if (!returned.contains("!")) {
            faceMessageType = FacesMessage.SEVERITY_ERROR;
        } else {
            faceMessageType = FacesMessage.SEVERITY_INFO;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(faceMessageType, returned, ""));
    }

    public List<CurrencyTransaction> completedTransactions(String currentUsername) {
        return transactionService.getUserCompletedTransactions(currentUsername);
    }

    public List<CurrencyTransaction> outboundUserRequestedTransactions(String currentUsername) {
        return transactionService.getUserRequestsSending(currentUsername);
    }

    public List<CurrencyTransaction> allTransactions() {
        return transactionService.getAllTransactions();
    }

    public List<SystemUser> allUsers() {
        return transactionService.getAllUsers();
    }
}
