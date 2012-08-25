package net.awired.restmcu.api.resource.client;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.restmcu.api.domain.pin.RestMcuPin;
import net.awired.restmcu.api.domain.pin.RestMcuPinSettings;

@Path("/pin/{pinId}")
@Produces("application/json")
@Consumes("application/json")
public interface RestMcuPinResource {

    @GET
    RestMcuPin getPin(@PathParam("pinId") Integer pinId) throws NotFoundException;

    @GET
    @Path("/settings")
    RestMcuPinSettings getPinSettings(@PathParam("pinId") Integer pinId) throws NotFoundException, UpdateException;

    @PUT
    @Path("/settings")
    void setPinSettings(@PathParam("pinId") Integer pinId, RestMcuPinSettings pinSettings) throws NotFoundException,
            UpdateException;

    @GET
    @Path("/value")
    Float getPinValue(@PathParam("pinId") Integer pinId) throws NotFoundException;

    @PUT
    @Path("/value")
    void setPinValue(@PathParam("pinId") Integer pinId, Float value) throws NotFoundException, UpdateException;
}
