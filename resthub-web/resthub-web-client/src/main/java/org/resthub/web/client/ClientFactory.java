package org.resthub.web.client;

import com.ning.http.client.AsyncHttpClient;
import com.sun.jersey.client.non.blocking.NonBlockingClient;
import com.sun.jersey.client.non.blocking.config.DefaultNonBlockingClientConfig;
import com.sun.jersey.client.non.blocking.config.NonBlockingClientConfig;
import org.resthub.web.jackson.JacksonProvider;



public class ClientFactory {

    @SuppressWarnings("deprecation")
	public static NonBlockingClient create() {
        
        NonBlockingClientConfig config = new DefaultNonBlockingClientConfig();
        config.getSingletons().add(new JacksonProvider());

        
        NonBlockingClient jerseyClient = NonBlockingClient.create(config);
        AsyncHttpClient httpClient = jerseyClient.getClientHandlerNing().getHttpClient();
        
        // Allow unsigned SSL certificates for testing purpose
        // TODO : control this thnaks to a properties value
        // SSLSocketFactory sf = (SSLSocketFactory) httpClient.getConnectionManager().getSchemeRegistry().getScheme("https").getSchemeSocketFactory();
        // sf.setHostnameVerifier(new AllowAllHostnameVerifier());
        return jerseyClient;
    }

}
