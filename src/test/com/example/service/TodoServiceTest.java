package com.example.service;

import com.example.entity.Todo;
import com.example.entity.User;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.lang.RuntimeEnvironment;
import io.jsonwebtoken.lang.UnknownClassException;
import jakarta.ejb.EJB;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.lang.ShiroException;
import org.apache.shiro.lang.codec.CodecSupport;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.lang.util.StringUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class TodoServiceTest {

    private User user;
    @EJB
    TodoService todoService;
    Logger logger;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage(Todo.class.getPackage())
                .addPackage(TodoService.class.getPackage())
                .addPackage(ByteSource.class.getPackage())
                .addPackage(Sha512Hash.class.getPackage())
                .addPackage(CodecSupport.class.getPackage())
                .addPackage(ShiroException.class.getPackage())
                .addPackage(StringUtils.class.getPackage())
                .addPackage(SecureRandomNumberGenerator.class.getPackage())
                .addPackage(SignatureAlgorithm.class.getPackage())
                .addPackage(UnknownClassException.class.getPackage())
                .addPackage(MacProvider.class.getPackage())
                .addPackage(RuntimeEnvironment.class.getPackage())
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() {
        logger = Logger.getLogger(TodoService.class.getName());
        user = new User();
        user.setEmail("andreivoicu80@gmail.com");
        user.setFullName("Andrei Voicu");
        user.setPassword("AndreiVoicu10");

        todoService.saveUser(user);
    }

    @Test
    public void saveUser() {
        assertNotNull(user.getId());
        logger.log(Level.INFO, user.getId().toString());

        assertNotEquals("The user password is not the same as hashed", "AndreiVoicu10", user.getPassword());
        logger.log(Level.INFO, user.getPassword());
    }

}
