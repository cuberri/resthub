package org.resthub.web.controller;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.resthub.core.tools.BeanDetail;
import org.resthub.core.tools.ToolingService;

import com.sun.jersey.api.view.ImplicitProduces;

@Path("/beans")
@Named("beanDetailsController")
@Singleton
@ImplicitProduces("text/html;qs=5")
public class BeanDetailsController {
	
	@Inject
    @Named("toolingService")
	private ToolingService toolingService;
	
//	@Context
//	private UriInfo uriInfo;

	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getResources() {
		List<BeanDetail> beanDetails = this.toolingService.getBeanDetails();
		return Response.ok(beanDetails.toArray(new BeanDetail[beanDetails.size()])).build();
	}
	
//	@GET
//	@Path("/{name}")
//	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
//	public Response getResource(@PathParam("name")String name) {
//		T resource =  this.resourceService.findByName(name);
//		
//		if(resource == null) {
//			return Response.status(Status.NOT_FOUND).build();
//		}
//				
//		return Response.ok(resource).build();
//
//	}
//	
//	@GET
//	@Path("/{name}")
//    @Produces(MediaType.TEXT_HTML)
//    public Viewable getResourceView(@PathParam("name")String name) {
//		T resource =  this.resourceService.findByName(name);
//		return new Viewable("default", resource);
//    }
//	
//	@GET
//    @Produces(MediaType.TEXT_HTML)
//    public Viewable getResourcesView() {
//		List<T> resources = this.resourceService.findAll();
//		return new Viewable("default", resources.toArray(resourceClassArray));
//    }

}
