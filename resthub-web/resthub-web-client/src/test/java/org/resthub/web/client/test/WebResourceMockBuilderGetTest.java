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
public class WebResourceMockBuilderGetTest {
    
    private WebResource mockedWebResource;
    
    @Before
    public void setUp() {
        WebResourceMockBuilder webResourceMockBuilder = new WebResourceMockBuilder();
        webResourceMockBuilder.get("/user/loicfrering", new DummyUser("Loïc Frering"));
        this.mockedWebResource = webResourceMockBuilder.create();
    }
    
    @Test
    public void testGetClientResponseOk() {
        ClientResponse response = mockedWebResource.path("/user").path("/loicfrering").get(ClientResponse.class);
        assertNotNull(response);
        assertEquals(Status.OK, response.getClientResponseStatus());
        assertEquals(200, response.getStatus());
    }
    
    @Test
    public void testGetClientResponseNotFound() {
        ClientResponse response = mockedWebResource.path("/user").path("/lfrering").get(ClientResponse.class);
        assertNotNull(response);
        assertEquals(Status.NOT_FOUND, response.getClientResponseStatus());
        assertEquals(404, response.getStatus());
    }
    
    @Test
    public void testGetEntityOk() {
        DummyUser user = mockedWebResource.path("/user").path("/loicfrering").get(DummyUser.class);
        assertNotNull(user);
        assertEquals("Loïc Frering", user.getFullname());
    }
    
    @Test(expected=ClientHandlerException.class)
    public void testGetEntityNotFound() {
        DummyUser user = mockedWebResource.path("/user").path("/lfrering").get(DummyUser.class);
    }
}
