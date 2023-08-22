package org.acme.apiendpoint;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/")
public class ApiResource {

    @ConfigProperty(name = "podname", defaultValue = "no-podname-provided")
    String podName;

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

    @GET
    @Path("/headers")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,String> hello(@Context HttpHeaders headers) {
        Map<String, String> resultMap = new HashMap<>();
        headers.getRequestHeaders().forEach(
                (key, values) -> resultMap.put(key, String.join(", ", values)));
        return resultMap;
    }

    @GET
    @Path("/podname")
    public String getPodName() {
        return podName;
    }
}