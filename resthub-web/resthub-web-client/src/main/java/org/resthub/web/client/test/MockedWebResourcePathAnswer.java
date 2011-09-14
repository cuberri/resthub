package org.resthub.web.client.test;

import com.sun.jersey.api.client.WebResource;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
class MockedWebResourcePathAnswer implements Answer<WebResource> {

    private final StringBuilder path;

    public MockedWebResourcePathAnswer(final StringBuilder path) {
        this.path = path;
    }
    
    @Override
    public WebResource answer(InvocationOnMock invocation) throws Throwable {
        String arg = (String) invocation.getArguments()[0];
        WebResource mockedResource = (WebResource) invocation.getMock();
        path.append(arg);
        return mockedResource;
    }
    
}
