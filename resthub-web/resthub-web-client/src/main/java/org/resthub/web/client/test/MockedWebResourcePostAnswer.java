package org.resthub.web.client.test;

import static org.mockito.BDDMockito.*;

import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import java.util.Map;
import org.mockito.invocation.InvocationOnMock;

/**
 *
 * @author Loïc Frering <loic.frering@gmail.com>
 */
class MockedWebResourcePostAnswer extends AbstractMockedWebResourceAnswer {
    
    private final StringBuilder path;
    private final Map<String, Object> routes;

    public MockedWebResourcePostAnswer(final StringBuilder path, final Map<String, Object> routes) {
        this.path = path;
        this.routes = routes;
    }

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        Object response = null;
        Class<?> type = (Class<?>) invocation.getArguments()[0];
        Object object = invocation.getArguments()[1];
        if (type == ClientResponse.class) {
            response = clientResponseAnswer(object);
        } else {
            response = entityAnswer(object);
        }
        // Reset path holder
        path.setLength(0);
        return response;
    }
    
    private ClientResponse clientResponseAnswer(Object object) {
        ClientResponse clientResponse = mockClientResponse();
        Status clientResponseStatus = null;
        
        for (String route : routes.keySet()) {
            if (route.equals(path.toString())) {
                clientResponseStatus = ClientResponse.Status.CREATED;
                given(clientResponse.getEntity(object.getClass())).willReturn(object);
                break;
            }
        }
        if (null == clientResponseStatus) {
            clientResponseStatus = ClientResponse.Status.NOT_FOUND;
            given(clientResponse.getEntity(any(Class.class))).willThrow(new ClientHandlerException());
        }
        given(clientResponse.getClientResponseStatus()).willReturn(clientResponseStatus);
        return clientResponse;
    }
    
    private Object entityAnswer(Object object) {
        Object response = null;
        
        for (String route : routes.keySet()) {
            if (route.equals(path.toString())) {
                response = object;
                break;
            }
        }
        if (null == response) {
            throw new ClientHandlerException();
        }
        return response;
    }
    
}
