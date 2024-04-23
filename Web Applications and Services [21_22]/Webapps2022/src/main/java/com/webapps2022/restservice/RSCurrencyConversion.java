/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.restservice;

import com.webapps2022.entity.CurrencyEnum;

import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author blankie
 */
@Singleton
@Path("/conversion")
public class RSCurrencyConversion {

    @Context
    UriInfo uriInfo;

    @GET
    @Path("/{currency1}/{currency2}/{amount_of_currency}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getConversion(@PathParam("currency1") CurrencyEnum currency1, @PathParam("currency2") CurrencyEnum currency2, @PathParam("amount_of_currency") Float amountOfCurrency) {
        Float currencyMultiplier = CurrencyEnum.convertCurrency(currency1, currency2);
        Float convertedCurrency = amountOfCurrency * currencyMultiplier;
        return Response.ok(convertedCurrency).build();
    }
}
