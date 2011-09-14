package org.resthub.web.client.test;

import static org.mockito.BDDMockito.*;

import com.sun.jersey.api.client.WebResource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public class WebResourceMockBuilder {
    private Map<String, Object> getRoutes = new HashMap<String, Object>();
    
    public WebResourceMockBuilder get(String path, Object response) {
        getRoutes.put(path, response);
        return this;
    }
    
    public WebResource create() {
        final StringBuilder pathHolder = new StringBuilder();
        WebResource mockedResource = mock(WebResource.class);
        given(mockedResource.path(anyString())).will(new MockedWebResourcePathAnswer(pathHolder));
        given(mockedResource.get(any(Class.class))).will(new MockedWebResourceGetAnswer(pathHolder, getRoutes));
        return mockedResource;
    }
}
