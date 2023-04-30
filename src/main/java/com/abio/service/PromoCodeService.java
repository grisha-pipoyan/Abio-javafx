package com.abio.service;

import com.abio.dto.PromoCode;
import com.abio.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class PromoCodeService {

    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "/abio/management/promocode";

    /**
     * Get promo codes
     *
     * @param authorizationToken authorization token
     * @return csv bytes
     */
    public byte[] getPromoCodes(String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/get");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        try {
            PromoCode[] body = this.restTemplate.exchange(
                    expand,
                    HttpMethod.GET,
                    requestEntity,
                    PromoCode[].class
            ).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.promoCodeColumns);

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }

    }

    /**
     * Add promo code
     *
     * @param promoCode          promo code
     * @param authorizationToken authorization token
     * @throws Exception error
     */
    public void addPromoCode(PromoCode promoCode, String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/add");

        HttpEntity<?> requestEntity = getForm(promoCode, authorizationToken);

        try {
            this.restTemplate.postForEntity(
                    expand,
                    requestEntity,
                    Void.class);
        } catch (Exception e) {
            throw new Exception(String.format("Невозможно добавить. %s", e.getMessage()));
        }

    }

    /**
     * Update promo code
     *
     * @param promoCode          promo code model
     * @param authorizationToken authorization token
     */
    public void updatePromoCode(PromoCode promoCode, String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/update");

        HttpEntity<?> requestEntity = getForm(promoCode, authorizationToken);

        try {
            this.restTemplate.postForEntity(
                    expand,
                    requestEntity,
                    Void.class);

        } catch (Exception e) {
            throw new Exception(String.format("Невозможно обновить. %s", e.getMessage()));
        }
    }

    /**
     * Delete promo code
     *
     * @param authorizationToken authorization token
     * @param id                 promo code id
     */
    public void deletePromoCodeById(String authorizationToken, String id) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/delete");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("id", Long.valueOf(id));

        try {

            this.restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.DELETE,
                    requestEntity,
                    Void.class
            ).getBody();

        } catch (Exception e) {
            throw new Exception(String.format("Невозможно удалить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    /**
     * Imports csv file
     *
     * @param csv                csv
     * @param authorizationToken authorization token
     */
    public void importCSV(byte[] csv, String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/csv/update");

        try {
            HttpEntity<?> requestEntity = getByteForm(csv, authorizationToken);
            this.restTemplate.exchange(expand, HttpMethod.POST, requestEntity, Void.class);

        } catch (Exception e) {
            throw new Exception(String.format("Невозможно импортировать данные. %s", e.getMessage()));
        }
    }

    public HttpEntity<?> getByteForm(byte[] csvFile, String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Authorization", authorizationToken);

        return new HttpEntity<>(csvFile, headers);
    }

    public HttpEntity<?> getForm(Object object, String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);

        return new HttpEntity<>(object, headers);
    }

}
