/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import model.User;
import service.UserFacadeREST;

import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author rol
 */
@Path("/")
@Produces(MediaType.TEXT_PLAIN)
@Stateless
public class AuthService {

    @EJB
    private UserFacadeREST userBean;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("username") String username, @FormParam("password") String password, @Context HttpServletRequest req) {

        User user = userBean.findBy(username, password);
        
        if (user == null) {
                return Response
                        .status(Response.Status.UNAUTHORIZED)
                        .build();
        }

        UUID key = UUID.randomUUID();
        
        UserSession.register(key.toString());
        
        return Response
                .ok()
                .header("sessionKey", key.toString())
                .cookie(new NewCookie("sessionKey", key.toString()))
                .entity(user)
                .build();
    }

    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(@FormParam("username") String username,
            @FormParam("displayName") String displayName,
            @FormParam("password") String password,
            @Context HttpServletRequest req) {


        User user = new User();

        user.setUsername(username);
        user.setPassword(password);
        user.setDisplayName(displayName);
                
        userBean.create(user);

        return login(username, password, req);
    }

}
