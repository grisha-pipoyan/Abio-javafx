package com.abio.service;

import com.abio.dto.UsernameAndPasswordAuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Initiates session
     *
     * @param login    login
     * @param password password
     * @return authorization token
     * @throws Exception if response code is not 200
     */
    public String initiate(String login, String password) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/api/v1/auth/authenticate");

        try {
            ResponseEntity<Void> responseEntity =
                    this.restTemplate.postForEntity(expand, new UsernameAndPasswordAuthenticationRequest(login, password),
                            Void.class);
            return Objects.requireNonNull(responseEntity.getHeaders().get("Authorization")).get(0);

        } catch (Exception e) {
            throw new Exception(String.format("Неправильное имя пользователя или пароль. %s", e.getMessage()));
        }
    }

    public void changePassword(String oldPassword, String newPassword, String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/sievn/management/password/change");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        String urlTemplate = UriComponentsBuilder.fromUri(expand)
                .queryParam("oldPassword", oldPassword)
                .queryParam("newPassword", newPassword)
                .encode().toUriString();

        try {
            this.restTemplate.put(urlTemplate, requestEntity);
        } catch (Exception e) {
            throw new Exception(String.format("Невозможно изменить пароль. Попробуйте позже։ %s", e.getMessage()));
        }
    }

}
