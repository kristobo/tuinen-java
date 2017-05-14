/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.app;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Create jersey.
 * @author kbo
 */
public class RestApp extends ResourceConfig   {
    public RestApp() {
        packages("be.miras.kristof.rest.services");
        register(RestCorsFilter.class);
        
        //Handled by secured annotation
        register(RestAuthFilter.class);
    }
}