package rso.itemscompare.entityeditor.api.v1.resources;

import rso.itemscompare.entityeditor.services.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/demo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DemoResource {
    @Inject
    private RestProperties restProperties;

    @POST
    @Path("break")
    public Response makeUnhealthy() {
        restProperties.setBroken(true);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("fix")
    public Response makeHealthy() {
        restProperties.setBroken(false);
        return Response.status(Response.Status.OK).build();
    }
}
