/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import model.Group;
import model.User;
import service.UserFacadeREST;

import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * @author rol
 */
@Path("/auth")
@Produces(MediaType.TEXT_PLAIN)
@Stateless
public class AuthService {
    
    @EJB
    private UserFacadeREST userBean;
    
    @GET
    @Path("ping")
    public String ping() {
        return "alive";
    }
    
    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("username") String username, @FormParam("password") String password, @Context HttpServletRequest req) {
                
        if(req.getUserPrincipal() == null) {
            try {
                req.getServletContext().log(String.format("zzz: username: %s, password: %s", username, DigestUtils.sha512Hex(password)));
                req.login(username, password);
            } catch (ServletException e) {
                e.printStackTrace(); // oopsie
                return Response
                        .status(Response.Status.UNAUTHORIZED)
                        .build();
            }
        }
        
        User user = userBean.find(1);
        
        return Response.ok().entity(user).build();
    }
    
    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public Response register(@FormParam("username") String username,
            @FormParam("displayName") String displayName, 
            @FormParam("password1") String password1, 
            @FormParam("password2") String password2, 
            @Context HttpServletRequest req) {
        
        if(password1.isEmpty() || !password1.equals(password2)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
//        req.authenticate(response);
        
        User user = new User();
        
        List<Group> groups = new ArrayList<Group>();
        groups.add(Group.ADMINISTRATOR);
        groups.add(Group.USER);
        groups.add(Group.DEFAULT);
        
        user.setUsername(username);
        user.setGroups(groups);
        user.setPassword(DigestUtils.sha512Hex(password1));
        user.setDisplayName(displayName);
        
        userBean.create(user);
        
        try {
            req.login(user.getUsername(), password1);
        } catch (ServletException e) {
            e.printStackTrace(); // oopsie
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .build();
        }
        
        return Response.ok().entity(user).build();
    }

}
