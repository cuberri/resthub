package org.resthub.web.client.test;

import static org.mockito.BDDMockito.*;

import com.sun.jersey.api.client.ClientResponse;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
public abstract class AbstractMockedWebResourceAnswer implements Answer<Object> {
    
    protected ClientResponse mockClientResponse() {
        ClientResponse mockedClientResponse = mock(ClientResponse.class);
        given(mockedClientResponse.getStatus()).willAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return ((ClientResponse) invocation.getMock()).getClientResponseStatus().getStatusCode();
            }
        });
        return mockedClientResponse;
    }
    
}
