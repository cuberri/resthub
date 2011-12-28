package org.resthub.async.test;

import com.sun.jersey.api.client.AsyncWebResource;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.client.non.blocking.NonBlockingClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.ws.rs.core.UriBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.resthub.web.client.ClientFactory;
import org.resthub.web.test.AbstractWebTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test project is using a variant of the RESThub HTTP client:<br>
 * <code>
 *     &lt;groupId&gt;com.ning&lt;/groupId&gt;<br>
 *     &lt;artifactId&gt;async-http-client&lt;/artifactId&gt;<br>
 * </code>
 * 
 * instead of the regular:<br>
 * <code>
 *   &lt;groupId&gt;com.sun.jersey.contribs&lt;/groupId&gt;<br>
 *   &lt;artifactId&gt;jersey-apache-client4&lt;/artifactId&gt;<br>
 * </code>
 * <br>
 * 
 * This HTTP client handles responses asynchronously; here are some examples.
 * @author http://github.com/bclozel
 */
public class AsyncHTTPTest extends AbstractWebTest {

    private Logger logger = LoggerFactory.getLogger(AsyncHTTPTest.class);

    /**
     * Creates a async HTTP client using RESThub's ClientFactory
     * (patched for this project)
     * @return 
     */
    private NonBlockingClient createClient() {
        return ClientFactory.create();
    }

    public UriBuilder getUri() {
        return UriBuilder.fromUri("http://localhost").port(port);
    }

    /*
     * Use an async HTTP client in "synchronous" mode.
     * This code is equivalent to using the regular synchronous HTTP client
     */
    @Test
    public void testOneSyncRequest() {

        // create a client instance using the ClientFactory
        Client client = this.createClient();

        // build the request URI and send request
        // blocks the Thread until a ClientResponse is received
        ClientResponse cr = client.resource(getUri().path("async").path("waitfor").path("1").build()).
                get(ClientResponse.class);

        Assert.assertNotNull("the client response should not be null", cr.getEntity(String.class));
    }

    /*
     * Use an async HTTP client in "asynchronous" mode.
     * This time, the client gets a <a href="http://docs.oracle.com/javase/6/docs/api/java/util/concurrent/Future.html">Future</a> 
     * Object that represents the HTTP response.
     * 
     */
    @Test
    public void testOneAsyncRequest() {

        // create a client instance using the ClientFactory
        Client client = this.createClient();
        // build the request URI and send request
        AsyncWebResource r = client.asyncResource(getUri().path("async").path("waitfor").path("5").build());
        // the Future object reprensents the result of this async task
        Future<ClientResponse> future = r.get(ClientResponse.class);

        // Now you can work on something and and even check if the async 
        // task is done (i.e. if the HTTP response has been received)
        future.isDone();

        try {
            // When calling the get method, your application is blocking again,
            // waiting for the response
            ClientResponse cr = future.get();
            cr.close();
        } catch (InterruptedException ex) {
            logger.error(null, ex);
        } catch (ExecutionException ex) {
            logger.error(null, ex);
        }
    }

    /*
     * Send several synchronous requests - in sequential order
     * This test method blocks the application for each request.
     * Total duration time = sum(time taken for each request)
     */
    @Test
    public void testMultipleSyncRequests() {

        // create a client instance using the ClientFactory
        NonBlockingClient client = this.createClient();

        final long startTime = System.nanoTime();
        final long endTime;

        try {

            // build several requests and send them
            // Each request blocks your application until response is received
            ClientResponse cr1 = client.resource(getUri().path("async").path("waitfor").path("3").build()).
                    get(ClientResponse.class);
            // Second request is sent only when the first response is received
            ClientResponse cr2 = client.resource(getUri().path("async").path("waitfor").path("2").build()).
                    get(ClientResponse.class);
        } finally {
            endTime = System.nanoTime();
            client.close();
        }

        // duration should be approx 3 + 2 = 5 seconds
        final double duration = (double) ((endTime - startTime) / 1000000000.0);

        logger.info(String.format("testMultipleSyncRequests total time: %10.2f secs", duration));

    }

    /*
     * Send several async requests - in parallel order
     * This test method DOES NOT block the application for each request.
     * Total duration time = max(time taken for each request)
     */
    @Test
    public void testMultipleAsyncRequests() {

        NonBlockingClient client = this.createClient();

        final long startTime = System.nanoTime();
        final long endTime;

        // send async requests in parallel
        AsyncWebResource r1 = client.asyncResource(getUri().path("async").path("waitfor").path("3").build());
        Future<String> future1 = r1.get(String.class);

        AsyncWebResource r2 = client.asyncResource(getUri().path("async").path("waitfor").path("2").build());
        Future<String> future2 = r2.get(String.class);

        AsyncWebResource r3 = client.asyncResource(getUri().path("async").path("waitfor").path("1").build());
        Future<String> future3 = r3.get(String.class);

        try {
            // get all HTTP responses
            future1.get();
            future2.get();
            future3.get();
        } catch (InterruptedException ex) {
            logger.error(null, ex);
        } catch (ExecutionException ex) {
            logger.error(null, ex);
        } finally {
            endTime = System.nanoTime();
            client.close();
        }

        // duration should be approx 3,2,1 in parallel -> 3 seconds total
        final double duration = (double) ((endTime - startTime) / 1000000000.0);

        logger.info(String.format("testMultipleAsyncRequests total time: %10.2f secs", duration));
    }
}
