/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.services;

import be.miras.kristof.rest.app.Secured;
import be.miras.programs.frederik.dao.DbKlantAdresDao;
import be.miras.programs.frederik.dao.adapter.AdresAdapter;
import be.miras.programs.frederik.model.Adres;
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
@Path("/address")
public class AddressService {
    
    /**
     * Get Address by id.
     * @param id
     * @return 
     */
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

