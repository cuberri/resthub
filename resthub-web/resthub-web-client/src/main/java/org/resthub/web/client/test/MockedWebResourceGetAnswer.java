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
class MockedWebResourceGetAnswer extends AbstractMockedWebResourceAnswer {
    
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
        ClientResponse clientResponse = mockClientResponse();
        Status clientResponseStatus = null;
        
        for (String route : routes.keySet()) {
            if (route.equals(path.toString())) {
                Object entity = routes.get(route);
                clientResponseStatus = ClientResponse.Status.OK;
                given(clientResponse.getEntity(entity.getClass())).willReturn(entity);
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
