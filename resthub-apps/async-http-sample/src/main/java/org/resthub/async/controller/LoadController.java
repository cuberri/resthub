package org.resthub.async.controller;

import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.client.non.blocking.NonBlockingClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.UriBuilder;
import org.resthub.web.client.ClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/load")
@Named("loadController")
public class LoadController {

    private Logger logger = LoggerFactory.getLogger(AsyncController.class);
    protected int port = 1337;

    private NonBlockingClient createClient() {
        return ClientFactory.create();
    }

    /**
     *
     * @return
     */
    public UriBuilder getUri() {
        return UriBuilder.fromUri("http://127.0.0.1").port(port);
    }

    /**
     * Makes *synchronous* requests to another service, thus simulating an
     * application sending HTTP requests to a remote (and laggy?) webservice.
     *
     * @param requestCount number of requests to send to a remote service
     * @return
     */
    @GET
    @Path("/sync/{requestCount}")
    public String syncRequests(@PathParam("requestCount") int requestCount) {

        // create a client instance using the ClientFactory
        NonBlockingClient client = this.createClient();
        AsyncWebResource r = client.asyncResource(getUri().build());

        // fake data - allocate some memory for each request
        int[] memspace;

        final long startTime = System.nanoTime();
        final long endTime;


        try {

            // send #requestCount requests
            for (int i = 0; i < requestCount; i++) {
                memspace = new int[10240];
                ClientResponse response = r.get(ClientResponse.class).get();
            }

        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(LoadController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            java.util.logging.Logger.getLogger(LoadController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            endTime = System.nanoTime();
            client.close();
        }

        final double duration = (double) ((endTime - startTime) / 1000000000.0);

        return String.format("sent %1d sync requests - total time: %10.2f secs", requestCount, duration);
    }

    /**
     * Makes *asynchronous* requests to another service, thus simulating an
     * application sending HTTP requests to a remote (and laggy?) webservice.
     *
     * @param requestCount number of requests to send to a remote service
     * @return
     */
    @GET
    @Path("/async/{requestCount}")
    public String asyncRequests(@PathParam("requestCount") int requestCount) {

        // create a client instance using the ClientFactory
        NonBlockingClient client = this.createClient();
        AsyncWebResource r = client.asyncResource(getUri().build());

        List<Future<ClientResponse>> futures = new ArrayList<Future<ClientResponse>>();
        // fake data - allocate some memory for each request
        Stack<int[]> memspace = new Stack<int[]>();

        final long startTime = System.nanoTime();
        final long endTime;

        // send #requestCount requests
        for (int i = 0; i < requestCount; i++) {
            futures.add(r.get(ClientResponse.class));
            //allocate some memory
            memspace.push(new int[10240]);
        }

        try {
            // make sure you actually get the responses
            for (Future<ClientResponse> response : futures) {
                response.get();
                //deallocate same amount
                memspace.pop();
            }

        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(LoadController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            java.util.logging.Logger.getLogger(LoadController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            endTime = System.nanoTime();
            client.close();
        }

        final double duration = (double) ((endTime - startTime) / 1000000000.0);

        return String.format("sent %1d async requests - total time: %10.2f secs", requestCount, duration);
    }
}
