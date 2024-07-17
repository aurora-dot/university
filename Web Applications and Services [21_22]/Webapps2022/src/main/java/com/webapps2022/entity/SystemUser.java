/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.entity;

import java.io.Serializable;

import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author blankie
 */
@NamedQueries({
    @NamedQuery(name = "getAllUsers", query = "SELECT u FROM SystemUser u"),
    @NamedQuery(name = "getUserByUsername", query = "SELECT u FROM SystemUser u WHERE u.username = :username"),})

@Entity
public class SystemUser implements Serializable {

    @Id
    String username;

    @NotNull
    String password;

    @NotNull
    String name;

    @NotNull
    String surname;

    @NotNull
    Float currencyCount;

    @NotNull
    CurrencyEnum currencyType;

    @OneToOne
    @NotNull
    private SystemUserGroup group;

    @OneToMany(mappedBy = "fromSystemUser")
    List<CurrencyTransaction> outTransactions;

    @OneToMany(mappedBy = "toSystemUser")
    List<CurrencyTransaction> inTransactions;

    public SystemUser() {
    }

    public SystemUser(String username, String password, String name, String surname, Float currencyCount,
            CurrencyEnum currencyType, SystemUserGroup group) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = username;
        this.currencyCount = currencyCount;
        this.currencyType = currencyType;
        this.group = group;
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

    public void setPassword(String password) {
        this.password = password;
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

    public Float getCurrencyCount() {
        return currencyCount;
    }

    public void setCurrencyCount(Float currencyCount) {
        this.currencyCount = currencyCount;
    }

    public CurrencyEnum getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyEnum currencyType) {
        this.currencyType = currencyType;
    }

    public SystemUserGroup getGroup() {
        return group;
    }

    public void setGroup(SystemUserGroup group) {
        this.group = group;
    }

    public List<CurrencyTransaction> getOutTransactions() {
        return outTransactions;
    }

    public void setOutTransactions(List<CurrencyTransaction> outTransactions) {
        this.outTransactions = outTransactions;
    }

    public List<CurrencyTransaction> getInTransactions() {
        return inTransactions;
    }

    public void setInTransactions(List<CurrencyTransaction> inTransactions) {
        this.inTransactions = inTransactions;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.name);
        hash = 71 * hash + Objects.hashCode(this.surname);
        hash = 71 * hash + Objects.hashCode(this.username);
        hash = 71 * hash + Objects.hashCode(this.password);
        hash = 71 * hash + Objects.hashCode(this.currencyCount);
        hash = 71 * hash + Objects.hashCode(this.currencyType);
        hash = 71 * hash + Objects.hashCode(this.group);

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
        final SystemUser other = (SystemUser) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname)) {
            return false;
        }

        if (!Objects.equals(this.username, other.username)) {
            return false;
        }

        if (!Objects.equals(this.currencyCount, other.currencyCount)) {
            return false;
        }

        if (!Objects.equals(this.currencyType, other.currencyType)) {
            return false;
        }

        if (!Objects.equals(this.group, other.group)) {
            return false;
        }

        if (!Objects.equals(this.outTransactions, other.outTransactions)) {
            return false;
        }

        if (!Objects.equals(this.inTransactions, other.inTransactions)) {
            return false;
        }

        return Objects.equals(this.password, other.password);
    }
}
