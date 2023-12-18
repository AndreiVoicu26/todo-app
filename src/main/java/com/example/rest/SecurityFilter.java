package com.example.rest;

import com.example.service.SecurityUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.security.Key;
import java.security.Principal;

@Authz
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
    @Inject
    private SecurityUtil securityUtil;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authString = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authString == null || !authString.startsWith("Bearer")) {
            throw new NotAuthorizedException(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        String token = authString.substring(SecurityUtil.BEARER.length()).trim();

        try {
            Key key = securityUtil.getSecurityKey();
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            SecurityContext originalContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public boolean isUserInRole(String role) {
                    return originalContext.isUserInRole(role);
                }

                @Override
                public boolean isSecure() {
                    return originalContext.isSecure();
                }

                @Override
                public Principal getUserPrincipal() {
                    return () -> claimsJws.getBody().getSubject();
                }

                @Override
                public String getAuthenticationScheme() {
                    return originalContext.getAuthenticationScheme();
                }
            });
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
