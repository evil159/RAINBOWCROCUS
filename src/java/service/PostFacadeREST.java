/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import model.Post;
import model.Post_;
import model.User;
import model.User_;
import org.glassfish.jersey.media.multipart.FormDataParam;
/**
 *
 * @author rol
 */
@Stateless
@Path("model.post")
public class PostFacadeREST extends AbstractFacade<Post> {

    @PersistenceContext(unitName = "RAINBOWCROCUSPU")
    private EntityManager em;

    public PostFacadeREST() {
        super(Post.class);
    }

//    @POST
//    @Override
//    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//    public void create(Post entity) {
//        super.create(entity);
//    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
    public Response register(@FormDataParam("caption") String caption,
            @FormDataParam("author") Integer author,
            @FormDataParam("image") InputStream imageStream,
            @Context HttpServletRequest req) {
     
        User user = getEntityManager().find(User.class, author);
        
        if (user == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Post post = new Post();

        post.setCaption(caption);
        post.setAuthor(user);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = imageStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            post.setImage(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        create(post);

        return Response
                .ok()
                .entity(post)
                .build();
    }


    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Post entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Post find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Path("user/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Post> findByUser(@PathParam("id") Integer userId) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
        Root<Post> root = criteria.from(Post.class);

        Predicate conjunction = builder.conjunction();

        criteria.where(builder.equal(root.get(Post_.author), getEntityManager().find(User.class, userId)));
        
        criteria.orderBy(builder.asc(root.get(Post_.created)));

        return em.createQuery(criteria).getResultList();
    }
    
    @GET
    @Path("image/{id}")
    @Produces({"image/png"})
    public Response getAvatar(@PathParam("id") Integer id) {
        return Response.ok(find(id).getImage()).build();
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Post> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Post> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
