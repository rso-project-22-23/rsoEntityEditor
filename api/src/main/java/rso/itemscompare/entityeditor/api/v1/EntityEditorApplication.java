package rso.itemscompare.entityeditor.api.v1;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1")
@OpenAPIDefinition(info = @Info(title = "EntityEditor", version = "v1"),
        servers = @Server(url = "http://20.31.253.184/entity-editor"))
public class EntityEditorApplication extends Application {
}
