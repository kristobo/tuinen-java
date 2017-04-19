/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.RestUtil;
import be.miras.kristof.rest.app.Secured;
import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dbo.DbGebruiker;
import be.miras.programs.frederik.dbo.DbTaak;
import com.google.gson.Gson;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author kbo
 */
@Path("/task")
public class TaskService {

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTasksByUser(@HeaderParam("X-Authentication-decrypted") String auth) {
        
        int id = RestUtil.getIdGebruikerFromToken(auth);
        
        //Get all tasks by user
        DbTaakDao taakDao = new DbTaakDao();
        List<Object> taskList = taakDao.leesAlle();
        
        Gson gson = new Gson();
        // convert your list to json
        String taskListJson = gson.toJson(taskList);
        
        return Response.status(200).entity(taskListJson).build();
    }
    
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") int id) {
        String output = "Hello!"+id;
        return Response.status(200).entity(output).build();
    }

}

