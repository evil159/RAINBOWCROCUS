/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import model.Post;
import model.User;

/**
 *
 * @author rol
 */
public class Test {
    
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("RAINBOWCROCUSPU");
        EntityManager manager = factory.createEntityManager();
        manager.getTransaction().begin();
        
        User user = new User();
        user.setUsername("evil159");
        user.setPassword("pass");
        
        Post post = new Post();
        post.setAuthor(user);
        post.setCaption("First post");
        
        Collection<Post> posts = user.getPosts();
        
        if (posts == null) {
            posts = new ArrayList<Post>();
        }
        
        posts.add(post);
        
        user.setPosts(posts);

        manager.persist(user);
        manager.persist(post);
        
        manager.getTransaction().commit();
        manager.close();
        factory.close();
    }
}
