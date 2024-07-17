/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.entity;

import java.io.Serializable;

import java.time.Instant;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

/**
 *
 * @author blankie
 */
@NamedQueries({
    @NamedQuery(name = "getAllTransactions", query = "SELECT t FROM CurrencyTransaction t"),
    @NamedQuery(name = "getUserCompletedTransactions", query = "SELECT t FROM CurrencyTransaction t WHERE (t.fromSystemUser = :systemUser OR t.toSystemUser = :systemUser) AND t.completed = true"),
    @NamedQuery(name = "getUserRequestsSending", query = "SELECT t FROM CurrencyTransaction t WHERE t.fromSystemUser = :systemUser AND t.completed = false"),})

@Entity
public class CurrencyTransaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    SystemUser toSystemUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    SystemUser fromSystemUser;

    @NotNull
    Float currencyCountTo;

    @NotNull
    Float currencyCountFrom;

    @NotNull
    CurrencyEnum currencyTypeTo;

    @NotNull
    CurrencyEnum currencyTypeFrom;

    @NotNull
    Instant transactionTimeStamp;

    @NotNull
    boolean completed;

    public CurrencyTransaction() {
    }

    public CurrencyTransaction(SystemUser toSystemUser, SystemUser fromSystemUser, Float currencyCountTo, Float currencyCountFrom, CurrencyEnum currencyTypeTo, CurrencyEnum currencyTypeFrom, Instant timeStamp, boolean completed) {
        this.toSystemUser = toSystemUser;
        this.fromSystemUser = fromSystemUser;
        this.currencyCountTo = currencyCountTo;
        this.currencyCountFrom = currencyCountFrom;
        this.currencyTypeTo = currencyTypeTo;
        this.currencyTypeFrom = currencyTypeFrom;
        this.completed = completed;
        this.transactionTimeStamp = timeStamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SystemUser getToSystemUser() {
        return toSystemUser;
    }

    public void setToSystemUser(SystemUser toSystemUser) {
        this.toSystemUser = toSystemUser;
    }

    public SystemUser getFromSystemUser() {
        return fromSystemUser;
    }

    public void setFromSystemUser(SystemUser fromSystemUser) {
        this.fromSystemUser = fromSystemUser;
    }

    public Float getCurrencyCountTo() {
        return currencyCountTo;
    }

    public void setCurrencyCountTo(Float currencyCountTo) {
        this.currencyCountTo = currencyCountTo;
    }

    public Float getCurrencyCountFrom() {
        return currencyCountFrom;
    }

    public void setCurrencyCountFrom(Float currencyCountFrom) {
        this.currencyCountFrom = currencyCountFrom;
    }

    public CurrencyEnum getCurrencyTypeTo() {
        return currencyTypeTo;
    }

    public void setCurrencyTypeTo(CurrencyEnum currencyTypeTo) {
        this.currencyTypeTo = currencyTypeTo;
    }

    public CurrencyEnum getCurrencyTypeFrom() {
        return currencyTypeFrom;
    }

    public void setCurrencyTypeFrom(CurrencyEnum currencyTypeFrom) {
        this.currencyTypeFrom = currencyTypeFrom;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Instant getTimeStamp() {
        return transactionTimeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.transactionTimeStamp = timeStamp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 64 * hash + Objects.hashCode(this.id);
        hash = 64 * hash + Objects.hashCode(this.toSystemUser);
        hash = 64 * hash + Objects.hashCode(this.fromSystemUser);
        hash = 64 * hash + Objects.hashCode(this.currencyCountTo);
        hash = 64 * hash + Objects.hashCode(this.currencyCountFrom);
        hash = 64 * hash + Objects.hashCode(this.currencyTypeTo);
        hash = 64 * hash + Objects.hashCode(this.currencyTypeFrom);
        hash = 64 * hash + Objects.hashCode(this.completed);
        hash = 64 * hash + Objects.hashCode(this.transactionTimeStamp);

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CurrencyTransaction other = (CurrencyTransaction) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.toSystemUser, other.toSystemUser)) {
            return false;
        }
        if (!Objects.equals(this.fromSystemUser, other.fromSystemUser)) {
            return false;
        }

        if (!Objects.equals(this.currencyCountTo, other.currencyCountTo)) {
            return false;
        }

        if (!Objects.equals(this.currencyCountFrom, other.currencyCountFrom)) {
            return false;
        }

        if (!Objects.equals(this.currencyTypeTo, other.currencyTypeTo)) {
            return false;
        }

        if (!Objects.equals(this.currencyTypeFrom, other.currencyTypeFrom)) {
            return false;
        }

        if (!Objects.equals(this.transactionTimeStamp, other.transactionTimeStamp)) {
            return false;
        }

        return Objects.equals(this.completed, other.completed);
    }
}
