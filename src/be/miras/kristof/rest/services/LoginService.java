/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.RestUtil;
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
@Path("/login")

public class LoginService {
    
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
                                     @FormParam("password") String password) {
        
        if(RestUtil.isExistingUser(username, password)){
            
            // Check if correct rol
            if (RestUtil.hasWerknemerRol(username, password)){
                 
                // Issue a token for the user
                String token = RestUtil.generateToken(username, password);

                // Return the token on the response
                return Response.ok(token).build();

            }
            
            return Response.status(Response.Status.UNAUTHORIZED).entity("Verkeerde rol").build();
           
        }

        return Response.status(Response.Status.UNAUTHORIZED).entity("Verkeerde gebruikersnaam of passwoord").build();
    }
    
}
