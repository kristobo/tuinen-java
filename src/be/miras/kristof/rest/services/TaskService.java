/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.RestUtil;
import be.miras.kristof.rest.app.Secured;
import be.miras.programs.frederik.dao.DbOpdrachtTaakDao;
import be.miras.programs.frederik.dao.DbVooruitgangDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dbo.DbWerknemerOpdrachtTaak;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.dbo.DbOpdrachtTaak;
import be.miras.programs.frederik.dbo.DbVooruitgang;
import be.miras.programs.frederik.model.Taak;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import static java.util.Calendar.HOUR;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.internal.util.Base64;

/**
 *
 * @author kbo
 */
@Path("/task")
public class TaskService {
    
    /**
     * Get all tasks by user
     * @param auth
     * @return 
     */
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
    
    /**
     * get task by id
     * @param id
     * @return 
     */
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
    
    /**
     * Set task progress.
     * @param obj
     * @return 
     */
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/progress")
    public Response setProgress(String obj) {
        
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
    
    /**
     * Add recording info to task.
     * @param obj
     * @param token
     * @return
     * @throws ParseException 
     */
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/track")
    public Response logTaskRegistration(String obj, @HeaderParam("X-Authentication-decrypted") String token) throws ParseException {
        
        // Get user info front header
        int werknermerId = RestUtil.getIdWerknemerFromToken(token);
        
        // Get Track Info from json.
        Gson gson = new Gson();
        JsonObject track = gson.fromJson(obj, JsonObject.class); 
        
        // Get fields from json
        int taskId = track.get("taskId").getAsInt();
        int opdrachtId = track.get("opdrachtId").getAsInt();
        long startTime_int = track.get("startTime").getAsLong();
        long endTime_int = track.get("endTime").getAsLong();
        
        // Convert date to correct format
        Date startTime = new Date(startTime_int);
        Date endTime = new Date(endTime_int);
        
        startTime = new Date(startTime.getTime() + TimeUnit.HOURS.toMillis(6));
        endTime = new Date(endTime.getTime() + TimeUnit.HOURS.toMillis(6));
       
        System.out.print(startTime_int);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime_s = sdf.format(startTime);
        String endTime_s = sdf.format(endTime);
   
        startTime = sdf.parse(startTime_s);
        endTime = sdf.parse(endTime_s);

        // Create object
        DbWerknemerOpdrachtTaak dbWOT = new DbWerknemerOpdrachtTaak();
        dbWOT.setWerknemerId(werknermerId);
        dbWOT.setBeginuur(startTime);
        dbWOT.setEinduur(endTime);
        dbWOT.setOpdrachtTaakOpdrachtId(opdrachtId);
        dbWOT.setOpdrachtTaakTaakId(taskId);
        
        // Save track info
        DbWerknemerOpdrachtTaakDao dbWOTd = new DbWerknemerOpdrachtTaakDao();
        dbWOTd.voegToe(dbWOT);
     
        String output = gson.toJson("POST: tijdregistratie toegevoegd ");
        return Response.status(200).entity(output).build();
    }

}

