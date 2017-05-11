/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.RestUtil;
import be.miras.kristof.rest.app.Secured;
import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.DbOpdrachtDao;
import be.miras.programs.frederik.dao.DbTaakDao;
import be.miras.programs.frederik.dao.DbWerknemerOpdrachtTaakDao;
import be.miras.programs.frederik.dao.adapter.AdresAdapter;
import be.miras.programs.frederik.dao.adapter.TaakDaoAdapter;
import be.miras.programs.frederik.dbo.DbGebruiker;
import be.miras.programs.frederik.dbo.DbOpdracht;
import be.miras.programs.frederik.dbo.DbTaak;
import be.miras.programs.frederik.model.Adres;
import be.miras.programs.frederik.model.Taak;
import com.google.gson.Gson;
import java.util.Iterator;
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
@Path("/address")
public class AddressService {
    
    
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response getAddressByKlantAddressId(@PathParam("id") int id) {
        
        DbKlantAdresDao klAdrDao = new DbKlantAdresDao();
        Integer addressId = klAdrDao.geefEersteAdresId(id);
       
        AdresAdapter adrDao = new AdresAdapter();
        Adres address = (Adres) adrDao.lees(addressId);
        
        
        Gson gson = new Gson();
        // convert your list to json
        String taskJson = gson.toJson(address);
      
        return Response.status(200).entity(taskJson).build();
    }

}

