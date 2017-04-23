/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.miras.kristof.rest.app;

import be.miras.programs.frederik.dao.DbGebruikerDao;
import be.miras.programs.frederik.dao.DbWerknemerDao;
import be.miras.programs.frederik.dbo.DbGebruiker;
import be.miras.programs.frederik.dbo.DbWerknemer;
import java.util.StringTokenizer;
import org.glassfish.jersey.internal.util.Base64;

/**
 *
 * @author kbo
 */
public class RestUtil {
    
    public static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    public static final String AUTHENTICATION_HEADER_PREFIX = "Basic ";
    public static final String AUTHENTICATION_LOGIN_URL = "login";


    public static boolean isUserAllowed(String username, String password){

            boolean isAllowed = false;

            // Get user from db
            DbGebruikerDao userDao = new DbGebruikerDao();
            DbGebruiker gebruiker = userDao.getUserByName(username);

            // If user exists check for pasword
            if(gebruiker == null){
                return isAllowed;
            }

            String pas = userDao.leesWachtwoord(gebruiker.getId());

            if (pas.equals(password)){
                isAllowed = true;
            }
            

            return isAllowed;
    }
    
    public static int getIdGebruikerFromToken(String usernameAndPassword){
            
            //Split username and password tokens
            StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
            String username = tokenizer.nextToken();
            String password = tokenizer.nextToken();
            
            // Get user from db
            DbGebruikerDao userDao = new DbGebruikerDao();
            DbGebruiker gebruiker = userDao.getUserByName(username);

            int id = gebruiker.getId();

            return id;
    }
    
     public static int getIdWerknemerFromToken(String usernameAndPassword){
            
            int userId = getIdGebruikerFromToken(usernameAndPassword);
            
            // Get wernemer from db
            DbWerknemerDao wnDao = new DbWerknemerDao();
            int id = wnDao.getWerknemerIdByUser(userId);
           
            return id;
    }
    
    
        
    public static String generateToken(String username, String password){
        String token = AUTHENTICATION_HEADER_PREFIX + Base64.encodeAsString(username + ":" + password);
        return token;
    }
        
    
}
