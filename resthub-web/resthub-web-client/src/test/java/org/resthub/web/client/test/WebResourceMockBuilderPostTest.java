package org.resthub.web.client.test;

import com.sun.jersey.api.client.ClientHandlerException;
import static org.junit.Assert.*;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public class WebResourceMockBuilderPostTest {
    
    private WebResource mockedWebResource;
    
    @Before
    public void setUp() {
        WebResourceMockBuilder webResourceMockBuilder = new WebResourceMockBuilder();
        webResourceMockBuilder.post("/user");
        this.mockedWebResource = webResourceMockBuilder.create();
    }
    
    @Test
    public void testPostClientResponseOk() {
        ClientResponse response = mockedWebResource.path("/user").post(ClientResponse.class, new DummyUser("Loïc Frering"));
        assertNotNull(response);
        assertEquals(Status.CREATED, response.getClientResponseStatus());
        assertEquals(201, response.getStatus());
        
        DummyUser entity = response.getEntity(DummyUser.class);
        assertNotNull(entity);
        assertEquals("Loïc Frering", entity.getFullname());
    }
    
    @Test
    public void testPostClientResponseNotFound() {
        ClientResponse response = mockedWebResource.path("/wrongpath").post(ClientResponse.class, new DummyUser("Loïc Frering"));
        assertNotNull(response);
        assertEquals(Status.NOT_FOUND, response.getClientResponseStatus());
        assertEquals(404, response.getStatus());
    }
    
    @Test
    public void testPostEntityOk() {
        DummyUser user = mockedWebResource.path("/user").post(DummyUser.class, new DummyUser("Loïc Frering"));
        assertNotNull(user);
        assertEquals("Loïc Frering", user.getFullname());
    }
    
    @Test(expected=ClientHandlerException.class)
    public void testPostEntityNotFound() {
        DummyUser user = mockedWebResource.path("/wrongpath").post(DummyUser.class, new DummyUser("Loïc Frering"));
    }
}
