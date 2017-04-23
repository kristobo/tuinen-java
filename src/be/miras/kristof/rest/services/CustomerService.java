/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.Secured;
import be.miras.programs.frederik.dao.DbKlantDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dbo.DbKlant;
import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author kbo
 */
@Path("/customer")
public class CustomerService {
    
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllCustomers() {
        
        // Get all klanten.
        DbKlantDao klantDao = new DbKlantDao();
        List<Object> klantList = klantDao.leesAlle();
        
        
        Gson gson = new Gson();
        // Convert your list to json.
        String klantListJson = gson.toJson(klantList);
        
        return Response.status(200).entity(klantListJson).build();
    }
    
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        
        // Get klant by id.
        DbKlantDao klantDao = new DbKlantDao();
        DbKlant dbKlant = (DbKlant) klantDao.lees(id);
        
        
        Gson gson = new Gson();
        // Convert your list to json.
        String dbKlantJson = gson.toJson(dbKlant);
        
        return Response.status(200).entity(dbKlantJson).build();
    }
    
}
