package org.resthub.web.client.test;

import static org.mockito.BDDMockito.*;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import java.util.Map;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
class MockedWebResourceGetAnswer implements Answer<Object> {
    
    private final StringBuilder path;
    private final Map<String, Object> routes;

    public MockedWebResourceGetAnswer(final StringBuilder path, final Map<String, Object> routes) {
        this.path = path;
        this.routes = routes;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object response = null;
        Class<?> type = (Class<?>) invocation.getArguments()[0];
        if (type == ClientResponse.class) {
            response = clientResponseAnswer();
        } else {
            response = entityAnswer();
        }
        // Reset path holder
        path.setLength(0);
        return response;
    }
    
    private ClientResponse clientResponseAnswer() {
        ClientResponse clientResponse = mock(ClientResponse.class);
        Status clientResponseStatus = null;
        int status = 0;
        
        for (String route : routes.keySet()) {
            if (route.equals(path.toString())) {
                Object entity = routes.get(route);
                clientResponseStatus = ClientResponse.Status.OK;
                status = 200;
                given(clientResponse.getEntity(entity.getClass())).willReturn(entity);
                break;
            }
        }
        if (null == clientResponseStatus) {
            clientResponseStatus = ClientResponse.Status.NOT_FOUND;
            status = 404;
            given(clientResponse.getEntity(any(Class.class))).willThrow(new ClientHandlerException());
        }
        given(clientResponse.getClientResponseStatus()).willReturn(clientResponseStatus);
        given(clientResponse.getStatus()).willReturn(status);
        return clientResponse;
    }
    
    private Object entityAnswer() {
        Object response = null;
        
        for (String route : routes.keySet()) {
            if (route.equals(path.toString())) {
                response = routes.get(route);
                break;
            }
        }
        if (null == response) {
            throw new ClientHandlerException();
        }
        return response;
    }
    
}
