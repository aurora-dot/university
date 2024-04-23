/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.ejb;

import static com.webapps2022.entity.CurrencyEnum.GBP;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author blankie
 */
@Startup
@Singleton
public class StartupService {

    @EJB
    UserService usrSrv;

    @PostConstruct
    private void startup() {
        if (!usrSrv.userExists("admin1")) {
            usrSrv.registerAdmin("admin1", "admin1", "admin1", "admin", "admin", Float.valueOf(100), GBP);
        }
    }

    @PreDestroy
    public void destroy() {
    }
}
