/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.Secured;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dbo.DbOpdracht;
import com.google.gson.Gson;
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
@Path("/job")
public class JobService {
    
    /**
     * Get job by id.
     * @param id
     * @return 
     */
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getJobById(@PathParam("id") int id) {
        
        DbOpdrachtDao opdrDao = new DbOpdrachtDao();
        DbOpdracht opdracht = (DbOpdracht) opdrDao.lees(id);
        
        
        Gson gson = new Gson();
        // convert your list to json
        String taskJson = gson.toJson(opdracht);
      
        return Response.status(200).entity(taskJson).build();
    }

}

