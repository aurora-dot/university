/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.jsf;

import com.webapps2022.ejb.UserService;
import com.webapps2022.entity.CurrencyEnum;

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
public class RegistrationBean {

    @EJB
    UserService userService;

    String username;
    String password;
    String name;
    String surname;
    CurrencyEnum currencyType;
    String confPassword;

    public RegistrationBean() {

    }

    public String registerUser() {
        String result = userService.registerUser(username, password, confPassword, name, surname, Float.valueOf(100), currencyType);
        if (result.equals("index")) {
            return result + "?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, result, ""));
        }
        return null;
    }

    public String registerAdmin() {
        String result = userService.registerAdmin(username, password, confPassword, name, surname, Float.valueOf(100),
                currencyType);
        if (result.equals("index")) {
            return result;
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, result, ""));
        }
        return null;
    }

    public CurrencyEnum[] getAllOrderCurrencies() {
        return CurrencyEnum.values();
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String userpassword) {
        this.password = userpassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public CurrencyEnum getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyEnum currencyType) {
        this.currencyType = currencyType;
    }
}
