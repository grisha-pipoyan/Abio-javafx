package com.abio.service;

import com.abio.dto.DeliveryRegion;
import com.abio.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@Service
public class DeliverRegionService {

    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "/abio/management/delivery";

    public byte[] getDeliveryRegions(String authorizationToken, Integer type) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/get");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("type", type);

        try {

            DeliveryRegion[] body = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    DeliveryRegion[].class).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.deliveryRegionsColumns);

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    /**
     * @param name_en            eng
     * @param name_ru            rus
     * @param name_am            rus
     * @param price              price
     * @param bulky              bulky
     * @param authorizationToken authorization token
     */
    public void addDeliveryRegion(String name_en, String name_ru, String name_am, BigDecimal price, Integer bulky, String authorizationToken)
            throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/add");

        DeliveryRegion region = new DeliveryRegion();
        region.setName_en(name_en);
        region.setName_ru(name_ru);
        region.setName_am(name_am);
        region.setPrice(price);
        region.setBulky(bulky);

        HttpEntity<?> requestEntity = getForm(region, authorizationToken);

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
     * Updates delivery region
     *
     * @param region             region
     * @param authorizationToken token
     */
    public void updateDeliveryRegion(DeliveryRegion region, String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/update");

        HttpEntity<?> requestEntity = getForm(region, authorizationToken);

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
     * Delete region by id
     *
     * @param authorizationToken token
     * @param id                 id
     */
    public void deleteRegionById(String authorizationToken, String id) throws Exception {
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
