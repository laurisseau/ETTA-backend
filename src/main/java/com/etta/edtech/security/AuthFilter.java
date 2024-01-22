package com.etta.edtech.security;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.etta.edtech.exceptions.InvalidTokenException;
import com.etta.edtech.model.Educator;
import com.etta.edtech.model.User;
import com.etta.edtech.service.EducatorAuthenticationService;
import com.etta.edtech.service.UserAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final UserAuthenticationService userAuthenticationService;
    private final EducatorAuthenticationService educatorAuthenticationService;
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

            String authHeader = request.getHeader(AUTH_HEADER);
        try{

            if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = authHeader.substring(BEARER_PREFIX.length());

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                verifyAndSetAuthentication(accessToken, request);
            }

            filterChain.doFilter(request, response);
        }catch(Exception e){
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private void verifyAndSetAuthentication(String accessToken, HttpServletRequest request) {

            boolean verifyUser = userAuthenticationService.verifyUser(accessToken);
            boolean verifyEducator = educatorAuthenticationService.verifyUser(accessToken);

            if (verifyUser) {
                setAuthentication(userAuthenticationService.findUser(accessToken), request);
            } else if (verifyEducator) {
                setAuthentication(educatorAuthenticationService.findEducator(accessToken), request);
            } else {
                throw new InvalidTokenException("Invalid token please login again.");
            }
    }

    private void setAuthentication(Object userDetails, HttpServletRequest request) {
        if (userDetails != null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, ((UserDetails) userDetails).getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

}
