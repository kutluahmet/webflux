package com.textkernel.javatask.authentication;

import com.textkernel.javatask.constants.Constants;
import com.textkernel.javatask.domain.document.User;
import com.textkernel.javatask.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;


/**
 * @author AKUTLU
 * This class implements the token based secruity for endpoints
 */
@Slf4j
@Component
public class TokenServiceInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    private final JwtTokenUtil jwtTokenUtil;

    private final AuthenticationResponseHandler authenticationResponseHandler;

    private final static int tokenExpireTime = 100000;

    public TokenServiceInterceptor(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, AuthenticationResponseHandler authenticationResponseHandler) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationResponseHandler = authenticationResponseHandler;
    }

    /**
     * This method is an the entry point for api calls
     * It checks if the token in url exists
     * If the token exists then it performs token validation
     * If the token is valid it let the request reach the relevant endpoint
     * If the token is not valid it returns 401 UNAUTHORIZED response
     * @param request http request
     * @param response http response
     * @param handler handler
     * @return boolean
     * @throws IOException ex
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        if (!request.getRequestURI().equals(Constants.ACCESS_TOKEN_URL)) {
            if (request.getQueryString() != null
                    && request.getQueryString().contains(Constants.ACCESS_TOKEN_QUERY_STRING)) {
                return ValidateToken(request, response);
            } else {
                authenticationResponseHandler.unauthorizedResponse(response, "Access token not found");
                log.info("Access token not found");
                return false;
            }
        }
        return true;
    }


    /**
     * This method performs a basic authentication for generate token requests
     * It  generates a token if only the request has correct username password
     * If username password credentials are not correct it return 401 UNAUTHORIZED response
     * @param request http request
     * @param response http response
     * @param handler handler
     * @param modelAndView model view
     * @throws IOException ex
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws IOException {

        if(request.getRequestURI().equals(Constants.ACCESS_TOKEN_URL))
        {
            Optional<User> user = basicAuthenticationCheck(request);
            if(user.isPresent()){
                generateToken(response, user);
            } else {
                authenticationResponseHandler.unauthorizedResponse(response, "User not found");
            }
        }
    }

    /**
     * It generates a JWT token and save it to database
     * @param response Http Response
     * @param user User object
     * @throws IOException ex
     */
    private void generateToken(HttpServletResponse response, Optional<User> user) throws IOException {
        User authenticatedUser = user.get();
        String accessToken = jwtTokenUtil
        .createJWT(authenticatedUser.getId(), authenticatedUser.getUsername(),authenticatedUser.getPassword(), tokenExpireTime);
        authenticatedUser.setAccessToken(accessToken);
        userRepository.save(authenticatedUser).block();
        authenticationResponseHandler.tokenResponse(response,"Access token is generated", authenticatedUser.getAccessToken());
        log.info("Access token is generated for user {} ", authenticatedUser.getUsername());
    }

    /**
     * This methods performs the basic authentication service
     * @param request Http request
     * @return
     */
    private Optional<User> basicAuthenticationCheck(HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        String[] credentials = new String[2];
        if(authorization!=null && authorization.split(" ").length==2
                && authorization.split(" ")[0].toLowerCase().startsWith("basic")) {
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String values = new String(credDecoded, StandardCharsets.UTF_8);
            credentials = values.split(":", 2);
        }

        return userRepository.findUserByNameAndPassword(credentials[0],credentials[1]).blockOptional();
    }


    /**
     * This method checks if a token exist in database and if it exists it checks its expiration status
     * @param request Http Request
     * @param response Http Response
     * @return boolean
     * @throws IOException ex
     */
    private boolean ValidateToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String tokenStr = request.getQueryString();
        if (tokenStr.split(Constants.ACCESS_TOKEN_QUERY_STRING).length == 2) {
            String token = tokenStr.split(Constants.ACCESS_TOKEN_QUERY_STRING)[1];
            User user= userRepository.findAll()
                    .filter(u -> u.getAccessToken().equals(token) && !jwtTokenUtil.isTokenExpired(token))
                    .blockFirst();
            return AuthenticateUser(user, response);
        }  else {
            authenticationResponseHandler.unauthorizedResponse(response, "Token Not Found");
            log.info("Token Not Found");
            return false;
        }
    }

    /**
     * This inner method authenticate a user with correct credentials
     * @param user User object
     * @param response http response
     * @return boolean
     * @throws IOException ex
     */
    private boolean AuthenticateUser(User user, HttpServletResponse response) throws IOException {
        if (user != null) {
            log.info("Access granted for user {}", user.getUsername());
            return true; }
        else {
            authenticationResponseHandler.unauthorizedResponse(response, "Token Validation is failed");
            log.info("Token Validation is failed");
            return false;
        }
    }
}