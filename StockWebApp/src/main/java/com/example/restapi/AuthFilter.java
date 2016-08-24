/**
 * Jersey HTTP Basic Auth filter
 * @author Deisss (LGPLv3)
 */
package com.example.restapi;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter{
	@Context HttpServletRequest sr;
	@Override
	public void filter(ContainerRequestContext containerRequest) throws IOException {
		// TODO Auto-generated method stub
		//Get the authentification passed in HTTP headers parameters
        String auth = containerRequest.getHeaderString("authorization");
 
        //If the user does not have the right (does not provide any HTTP Basic Auth)
        if(auth == null){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
 
        //
        String[] loginAndPass = BasicAuth.decode(auth);
 
        //If login or password fail
        if(loginAndPass == null || loginAndPass.length != 2){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }
 
        String userName=loginAndPass[0];
        String password=loginAndPass[1];
        sr.setAttribute("username", userName);
        return;
	}

}
