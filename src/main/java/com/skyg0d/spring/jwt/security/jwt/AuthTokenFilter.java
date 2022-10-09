package com.skyg0d.spring.jwt.security.jwt;

import com.skyg0d.spring.jwt.exception.details.ExceptionDetails;
import com.skyg0d.spring.jwt.exception.details.TokenExpiredExceptionDetails;
import com.skyg0d.spring.jwt.security.service.UserDetailsServiceImpl;
import com.skyg0d.spring.jwt.util.ExceptionUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String email = jwtUtils.getEmailFromToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            HttpStatus status = HttpStatus.UNAUTHORIZED;

            ExceptionDetails details = ExceptionDetails.createExceptionDetails(ex, status, "Unauthorized.");

            TokenExpiredExceptionDetails tokenDetails = TokenExpiredExceptionDetails
                    .builder()
                    .title(details.getTitle())
                    .details(details.getDetails())
                    .status(status.value())
                    .timestamp(details.getTimestamp())
                    .developerMessage(details.getDeveloperMessage())
                    .expired(true)
                    .build();

            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(ExceptionUtils.convertObjectToJson(tokenDetails));
        } catch (
                Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());

            filterChain.doFilter(request, response);
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        return null;
    }

}
