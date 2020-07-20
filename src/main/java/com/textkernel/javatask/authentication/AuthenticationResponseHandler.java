package com.textkernel.javatask.authentication;


import com.google.gson.Gson;
import com.textkernel.javatask.constants.Constants;
import com.textkernel.javatask.domain.dto.AuthResponse;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author AKUTLU
 * This class is responsible to return custom http response messages
 */
@Component
public class AuthenticationResponseHandler {


    /**
     *  Unauthorized requests are handled with a proper response message
     * @param response servlet response
     * @param message text
     * @throws IOException ex
     */
    public void unauthorizedResponse (HttpServletResponse response, String message) throws IOException {

        AuthResponse authResponse = new AuthResponse(Constants.UNAUTHORIZED, message,null);
        response.getWriter().write(new Gson().toJson(authResponse));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(Constants.UNAUTHORIZED);
    }

    /**
     * Create response message with the token in response of successful attempts for token request
     * @param response servlet response
     * @param message text
     * @param token jwt token
     * @throws IOException ex
     */
    public void tokenResponse(HttpServletResponse response, String message, String token) throws IOException {

        AuthResponse authResponse = new AuthResponse(Constants.CREATED, message, token);
        response.getWriter().write(new Gson().toJson(authResponse));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(Constants.CREATED);
    }

}
