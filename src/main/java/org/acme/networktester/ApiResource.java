package org.acme.networktester;

import java.net.http.HttpResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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

    @GET
    @Path("/status/{status}")
    public Response status(@PathParam("status") String status) {
        Status responseStatus = Response.Status.BAD_REQUEST;
        try {
            responseStatus = Response.Status.fromStatusCode(Integer.valueOf(status));
        } catch (IllegalArgumentException e) {
            // Noop
        }
        if (responseStatus == null) {
            responseStatus = Response.Status.BAD_REQUEST;
        }
        return Response
            .status(responseStatus)
            .entity("you requested status code: " + status)
            .build();
    }
}