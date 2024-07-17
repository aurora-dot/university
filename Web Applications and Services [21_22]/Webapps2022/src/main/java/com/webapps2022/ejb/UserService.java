package com.webapps2022.ejb;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.webapps2022.entity.CurrencyEnum;
import com.webapps2022.entity.SystemUserGroup;
import com.webapps2022.entity.SystemUser;
import java.lang.reflect.UndeclaredThrowableException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.DependsOn;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

/**
 *
 * @author blankie
 */
@Stateless
@DependsOn("StartupService")
public class UserService {

    @PersistenceContext
    EntityManager em;

    public UserService() {
    }

    public Boolean userExists(String username) {
        TypedQuery<SystemUser> query = em.createNamedQuery("getUserByUsername", SystemUser.class);
        query.setParameter("username", username);
        try {
            query.getSingleResult();
            return true;
        } catch (UndeclaredThrowableException | PersistenceException e) {
            return false;
        }
    }

    public String registerUser(String username, String password, String confPassword, String name, String surname,
            Float currencyCount,
            CurrencyEnum currencyType) {
        return registerSystemUser(username, password, confPassword, name, surname, currencyCount, currencyType,
                "users");
    }

    public String registerAdmin(String username, String password, String confPassword, String name, String surname,
            Float currencyCount,
            CurrencyEnum currencyType) {
        return registerSystemUser(username, password, confPassword, name, surname, currencyCount, currencyType,
                "admins");
    }

    private String registerSystemUser(String username, String password, String confPassword, String name, String surname,
            Float currencyCount,
            CurrencyEnum currencyType, String group) {
        try {
            SystemUser sys_user;
            SystemUserGroup sys_user_group;

            if (!password.equals(confPassword)) {
                return "User error: Passwords do not match";
            }

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = password;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);

            if (!userExists(username)) {
                sys_user_group = new SystemUserGroup(username, group);
                sys_user = new SystemUser(username, paswdToStoreInDB, name, surname, currencyCount, currencyType, sys_user_group);
                em.persist(sys_user_group);
                em.persist(sys_user);
                em.flush();

                return "index";
            } else {
                return "User error: User already exists";
            }

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return "Backend error: Unsupported encoding or no such algorithm error";
        }
    }
}
