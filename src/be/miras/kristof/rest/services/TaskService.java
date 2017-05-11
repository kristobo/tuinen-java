/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.RestUtil;
import be.miras.kristof.rest.app.Secured;
import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.dbo.DbGebruiker;
import be.miras.programs.frederik.dbo.DbOpdrachtTaak;
import be.miras.programs.frederik.dbo.DbTaak;
import be.miras.programs.frederik.dbo.DbVooruitgang;
import be.miras.programs.frederik.model.Taak;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 *
 * @author kbo
 */
@Path("/task")
public class TaskService {
    
    @Path("/all")
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTasksByUser(@HeaderParam("X-Authentication-decrypted") String auth) {
      
        int id = RestUtil.getIdWerknemerFromToken(auth);

        //Get all tasks by werknemer
        DbWerknemerOpdrachtTaakDao dbWOTdao = new DbWerknemerOpdrachtTaakDao();
        List<Object> taskList = dbWOTdao.getAllCurrentTaskByUser(id);
        
        if(taskList.size() > 0){
            Gson gson = new Gson();
            // convert your list to json
            String taskListJson = gson.toJson(taskList);

            return Response.status(200).entity(taskListJson).build();
        }
        
        return Response.status(Response.Status.NOT_FOUND).entity("Geen taken toegekend").build();
    }
    
    
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getTaskById(@PathParam("id") int id) {
        
        TaakDaoAdapter tda = new TaakDaoAdapter();
        Taak task = tda.lees(id);
        
        
        Gson gson = new Gson();
        // convert your list to json
        String taskJson = gson.toJson(task);
      
        return Response.status(200).entity(taskJson).build();
    }
    
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/status")
    public Response setStatus(String obj) {
        
        // Get Task Info from json.
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(obj, JsonObject.class); 
        JsonObject task = json.get("task").getAsJsonObject();
        int opdrachtId = json.get("id").getAsInt();
        int taskId = task.get("id").getAsInt();
        int vooruitgang = task.get("vooruitgangPercentage").getAsInt();
        
        // Set Task info.
        
        /* Werkt niet???
        TaakDaoAdapter tdaS = new TaakDaoAdapter();
        Taak taak = tdaS.lees(taskId);
        taak.setVooruitgangPercentage(vooruitgang);
        tdaS.wijzig(taak);
        */
        
        DbOpdrachtTaakDao otd = new DbOpdrachtTaakDao();
        DbOpdrachtTaak ot = otd.getByTaskId(taskId);
        int vooruitgangId = ot.getVooruitgangId();
         
        DbVooruitgangDao vgd = new DbVooruitgangDao();
        DbVooruitgang vg = (DbVooruitgang) vgd.lees(vooruitgangId);
        vg.setPercentage(vooruitgang);
        vgd.wijzig(vg);
        
        
        String output = gson.toJson("POST: Taakstatus gewijzigd (taakId=" + taskId + ")");
        return Response.status(200).entity(output).build();
    }

}

