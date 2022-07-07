package org.acme.networktester;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/")
public class ApiResource {

    @GET
    @Path("/ping")
    public String ping() {
        return "PONG";
    }

    @GET
    @Path("/sleep/{duration}")
    public void sleep(@PathParam("duration") int duration) throws InterruptedException {
        Thread.sleep(duration * 1000);
    }
}