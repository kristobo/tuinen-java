/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.RestUtil;
import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dbo.DbGebruiker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author kbo
 */
@Path("/password")

public class PasswordService {
    
    /**
     * Validate login and return token.
     * @param username
     * @param password
     * @return 
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password,
                                     @FormParam("pass_new") String pass_new) {

        if(!RestUtil.isExistingUser(username, password)){
            return Response.status(Response.Status.UNAUTHORIZED).entity("Verkeerde gebruikersnaam of passwoord").build();
        }
        
        DbGebruikerDao dgd = new DbGebruikerDao();
        DbGebruiker dg = dgd.getGebruiker(username);

        dg.setWachtwoord(pass_new);
        dgd.wijzig(dg);

        Gson gson = new Gson();
        String output = gson.toJson("POST: Password changed.");
        return Response.status(200).entity(output).build();
       
    }
    
}
