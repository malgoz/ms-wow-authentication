package pl.kinert.mswowauthentication.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

public interface Oauth2Handler {

    String getToken() throws URISyntaxException, UnsupportedEncodingException;

    boolean isTokenInvalid();
}
