/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.thrift;

import org.apache.thrift.TException;

/**
 *
 * @author blankie
 */
public class TimestamperHandler implements TimestampService.Iface {

    @Override
    public long getTimestamp() throws TException {
        return System.currentTimeMillis();
    }
}
