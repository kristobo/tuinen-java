/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.app;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.glassfish.jersey.internal.util.Base64;

/**
 *
 * Filter all requests with @secured tag and check if user has access.
 * @author kbo
 */
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class RestAuthFilter implements ContainerRequestFilter {
   
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
        Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED).entity("Verkeerde gebruikersnaam of passwoord").build();
        
        // Get auth header
        List<String> authHeader = requestContext.getHeaders().get(RestUtil.AUTHORIZATION_HEADER_KEY);

        // If empty return denied
        if(authHeader == null || authHeader.isEmpty()){
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }

        // Get token
        String encodedUserPassword = authHeader.get(0).replaceFirst(RestUtil.AUTHENTICATION_HEADER_PREFIX, "");

        //Decode username and password
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));
        
        //Split username and password tokens
        StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();

        if(RestUtil.isExistingUser(username,password)){
            //Add id in header to pass to endpoint
            requestContext.getHeaders().add("X-Authentication-decrypted", usernameAndPassword);
            
            return;
        }
        
        requestContext.abortWith(ACCESS_DENIED);

    }
    
}
