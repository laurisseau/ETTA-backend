package com.etta.edtech.security;

import com.etta.edtech.model.Educator;
import com.etta.edtech.model.User;
import com.etta.edtech.service.EducatorAuthenticationService;
import com.etta.edtech.service.UserAuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final UserAuthenticationService userAuthenticationService;
    private final EducatorAuthenticationService educatorAuthenticationService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {


        final String authHeader = request.getHeader("Authorization");


        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authHeader.substring(7);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            User userDetails = userAuthenticationService.findUser(accessToken);
            Educator educatorDetails = educatorAuthenticationService.findEducator(accessToken);

            if (userDetails != null) {
                    //set a roles attribute in aws-cognito for role so i can use userDetails.role
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
            }else if(educatorDetails != null){
                //set a roles attribute in aws-cognito for role so i can use educatorDetails.role
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(educatorDetails, null, educatorDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))) {

            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        } else if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EDUCATOR"))) {

            authorities.add(new SimpleGrantedAuthority("ROLE_EDUCATOR"));

        } else if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {

            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        }


        filterChain.doFilter(request,response);

    }


}
