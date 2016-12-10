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
                .cookie(new NewCookie("sessionKey", key.toString()))
                .entity(user)
                .build();
    }

    @POST
    @Path("register")
    @Produces(MediaType.APPLICATION_JSON)
    @TransactionAttribute(TransactionAttributeType.NEVER)
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
    public Response register(@FormDataParam("username") String username,
            @FormDataParam("displayName") String displayName,
            @FormDataParam("email") String email,
            @FormDataParam("phone") String phone,
            @FormDataParam("password1") String password1,
            @FormDataParam("password2") String password2,
            @FormDataParam("avatar") InputStream imageStream,
            @Context HttpServletRequest req) {

        if (password1.isEmpty() || !password1.equals(password2)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User user = new User();

        user.setUsername(username);
        user.setPassword(password1);
        user.setDisplayName(displayName);
        user.setEmail(email);
        user.setPhone(phone);
        
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = imageStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            
            user.setImage(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        userBean.create(user);

        return login(username, password1, req);
    }

}
