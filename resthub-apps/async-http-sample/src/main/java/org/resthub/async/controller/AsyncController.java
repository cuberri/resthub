package org.resthub.async.controller;

import java.util.Date;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/async")
@Named("asyncController")
public class AsyncController {

    private Logger logger = LoggerFactory.getLogger(AsyncController.class);

    @GET
    @Path("/waitfor/{secs}")
    public String waitFor(@PathParam("secs") long secs) {
        try {
            logger.debug(String.format("START-- /async/waitfor/%2$d %1$tH:%1$tM:%1$tS", new Date(), secs));
            Thread.sleep(secs * 1000);
            logger.debug(String.format("END-- /async/waitfor/%2$d %1$tH:%1$tM:%1$tS", new Date(), secs));
        } catch (InterruptedException ex) {
        }
        return String.format("waited for %1d seconds", secs);
    }
}
