package pl.kinert.mswowauthentication.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.kinert.mswowauthentication.model.TokenResponse;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;

@Service
public class Oauth2HandlerImpl implements Oauth2Handler {

    @Value("${battlenet.token.url}")
    private String tokenUrl;

    @Value("${battlenet.client.id}")
    private String clientId;

    @Value("${battlenet.client.secret}")
    private String clientSecret;

    private String token = null;
    private Instant tokenExpiry = null; // Instant when the token will expire

    private final Object tokenLock = new Object();

    public String getToken() throws URISyntaxException, UnsupportedEncodingException {
        if(isTokenInvalid()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            String encodedData = DatatypeConverter.printBase64Binary((clientId + ":" + clientSecret).getBytes("UTF-8"));
            httpHeaders.add("Authorization", "Basic " + encodedData);
            URI uri = new URI(tokenUrl);
            HttpEntity httpEntity = new HttpEntity(httpHeaders);
            ResponseEntity<TokenResponse> exchange = new RestTemplate().exchange(
                    tokenUrl + "?grant_type=client_credentials",
                    HttpMethod.POST,
                    httpEntity,
                    TokenResponse.class);
            TokenResponse tokenResponse = exchange.getBody();
            synchronized (tokenLock) {
                tokenExpiry = Instant.now().plusSeconds(tokenResponse.getExpires_in());
                token = tokenResponse.getAccess_token();
            }
        }
        synchronized (tokenLock){
            return token;
        }
    }

    public boolean isTokenInvalid(){
        synchronized (tokenLock) {
            if (token == null) {
                return true;
            }
            if (tokenExpiry == null) {
                return true;
            }
            return Instant.now().isAfter(tokenExpiry);
        }
    }
}
