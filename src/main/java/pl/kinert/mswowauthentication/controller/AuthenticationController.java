package pl.kinert.mswowauthentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kinert.mswowauthentication.service.Oauth2Handler;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

@RestController
public class AuthenticationController {

    private final Oauth2Handler oauth2Handler;

    public AuthenticationController(Oauth2Handler oauth2Handler){
        this.oauth2Handler = oauth2Handler;
    }

    @GetMapping("/token")
    public String getToken() throws URISyntaxException, UnsupportedEncodingException {
        return oauth2Handler.getToken();
    }
}
