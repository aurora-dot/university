package com.webapps2022.jsf;

import com.webapps2022.ejb.RestService;
import com.webapps2022.entity.CurrencyEnum;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author blankie
 */
@Named
@RequestScoped
public class RestBean {

    @EJB
    RestService restService;

    CurrencyEnum currency1;
    CurrencyEnum currency2;
    Float amountOfCurrency;

    public RestBean() {
    }

    public void retrieveConversion() {
        String result = restService.retrieveConversion(currency1, currency2, amountOfCurrency);
        if (result.equals("error")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, result, ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, CurrencyEnum.getCurrencySymbol(currency2) + result, ""));
        }
    }

    public CurrencyEnum[] getAllOrderCurrencies() {
        return CurrencyEnum.values();
    }

    public RestService getRestService() {
        return restService;
    }

    public void setRestService(RestService restService) {
        this.restService = restService;
    }

    public CurrencyEnum getCurrency1() {
        return currency1;
    }

    public void setCurrency1(CurrencyEnum currency1) {
        this.currency1 = currency1;
    }

    public CurrencyEnum getCurrency2() {
        return currency2;
    }

    public void setCurrency2(CurrencyEnum currency2) {
        this.currency2 = currency2;
    }

    public Float getAmountOfCurrency() {
        return amountOfCurrency;
    }

    public void setAmountOfCurrency(Float amountOfCurrency) {
        this.amountOfCurrency = amountOfCurrency;
    }

}
