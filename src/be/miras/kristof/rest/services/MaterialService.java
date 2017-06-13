/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.Secured;
import be.miras.programs.frederik.dao.DbOpdrachtMateriaalDao;
import be.miras.programs.frederik.dao.adapter.MateriaalDaoAdapter;
import be.miras.programs.frederik.dbo.DbOpdrachtMateriaal;
import be.miras.programs.frederik.model.Materiaal;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author kbo
 */
@Path("/material")
public class MaterialService {
    
    /**
     * Get material list
     * @return 
     */
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all")
    public Response getAllMaterials() {
        
        // Get all klanten.
        MateriaalDaoAdapter mda = new MateriaalDaoAdapter();
        List<Object> materialList = mda.leesAlle();
        
        Gson gson = new Gson();
        // Convert your list to json.
        String klantListJson = gson.toJson(materialList);
        
        return Response.status(200).entity(klantListJson).build();
    }
    
    /**
     * Add material to job.
     * @return 
     */
    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/job/add")
    public Response addJobMaterial(String obj) {
        
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(obj, JsonObject.class); 
       
        int materiaalId = json.get("id").getAsInt();
        int opdrachtId = json.get("opdrachtId").getAsInt();
        double hoeveelheid = json.get("hoeveelheid").getAsDouble();
        
        DbOpdrachtMateriaalDao omd = new DbOpdrachtMateriaalDao();
        DbOpdrachtMateriaal om = new  DbOpdrachtMateriaal();
        om.setMateriaalId(materiaalId);
        om.setOpdrachtId(opdrachtId);
        om.setVerbruik(hoeveelheid);
        omd.voegToe(om);
        
        

        String output = gson.toJson("POST: Materiaal toegevoegd (OpdrachtId=" + opdrachtId + ")");
        return Response.status(200).entity(output).build();
    }
    
    /**
     * Get material list for job
     * @return 
     */
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/job/{id}")
    public Response getAllMaterialsForJob(@PathParam("id") int id) {
        
        // Get all klanten.
        MateriaalDaoAdapter mda = new MateriaalDaoAdapter();
        List<Materiaal>  dbOpdrachtMateriaalLijst = mda.leesOpdrachtMateriaal(id);

        if(dbOpdrachtMateriaalLijst.size() > 0){
            Gson gson = new Gson();
            // Convert your list to json.
            String list = gson.toJson(dbOpdrachtMateriaalLijst);

            return Response.status(200).entity(list).build();
        }
        
       return Response.status(Response.Status.NOT_FOUND).entity("Geen materialen toegekend").build();
    }
    
    /**
     * Delete material from job
     * @param id
     * @return 
     */
    @DELETE
    @Path("/{id}")
    public Response deleteJobMaterial(final @PathParam("id") String id) {
            
            return Response.ok().build();
    }
    

}

