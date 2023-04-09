package com.abio.service;

import com.abio.csv.model.*;
import com.abio.persistance.model.addData.PictureAddModel;
import com.abio.rest.CurrentStatusEnum;
import com.abio.rest.LoginForm;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AbioService {

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
                    this.restTemplate.postForEntity(expand, new LoginForm(login, password),
                            Void.class);
            return Objects.requireNonNull(responseEntity.getHeaders().get("Authorization")).get(0);

        } catch (Exception e) {
            throw new Exception(String.format("Неправильное имя пользователя или пароль. %s", e.getMessage()));
        }

    }


    /**
     * Finds all products
     *
     * @param authorizationToken token
     * @return csv of all products
     * @throws Exception if response code is not 200
     */
    public byte[] getAllProductsCSV(String authorizationToken, Integer type) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/getProducts");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("type", type);

        try {

            return this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            ).getBody();

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    public byte[] getAllProductsCSVByQuantity(String authorizationToken, int type, int quantity) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/getProductsByQuantityEquals");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("type", type)
                .queryParam("quantity", quantity);

        try {

            return this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            ).getBody();

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    /**
     * Updates product
     *
     * @param product            product
     * @param authorizationToken authorization token
     */
    public void updateProduct(ProductModelAdd product, String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/products/update/model");

        HttpEntity<?> requestEntity = getForm(product, authorizationToken);

        try {

            this.restTemplate.postForEntity(
                    expand,
                    requestEntity,
                    Void.class);

        } catch (Exception e) {
            throw new Exception(String.format("Невозможно обновить. %s", e.getMessage()));
        }
    }

    public byte[] getDeliveryRegions(String authorizationToken, Integer type) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/getDeliveryRegions");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("type", type);

        try {

            return this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            ).getBody();

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

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/delivery/add");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("name_en", name_en)
                .queryParam("name_ru", name_ru)
                .queryParam("name_am", name_am)
                .queryParam("price", price)
                .queryParam("bulky", bulky);

        try {

            this.restTemplate.postForEntity(
                    builder.toUriString(),
                    requestEntity,
                    Void.class);

        } catch (Exception e) {
            throw new Exception(String.format("Невозможно добавить URL-адреса видео. %s", e.getMessage()));
        }

    }

    /**
     * Updates delivery region
     *
     * @param region             region
     * @param authorizationToken token
     */
    public void updateDeliveryRegion(DeliveryRegion region, String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/delivery/update");

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
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/delivery/delete/region");

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
     * Get videos csv
     *
     * @param authorizationToken authorization token
     * @return csv bytes
     * @throws Exception error
     */
    public byte[] getVideos(String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/getVideos");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        try {

            return this.restTemplate.exchange(
                    expand,
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            ).getBody();

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    /**
     * Updates video
     *
     * @param video              video
     * @param authorizationToken authorization token
     * @throws Exception error
     */
    public void updateVideo(Video video, String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/video/update");

        HttpEntity<?> requestEntity = getForm(video, authorizationToken);

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
     * Deletes video by Id
     *
     * @param authorizationToken authorization token
     * @param id                 video id
     * @throws Exception error
     */
    public void deleteVideoById(String authorizationToken, String id) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/video/delete");

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
     * Adds new video
     *
     * @param video              Video model
     * @param authorizationToken token
     * @throws Exception error
     */
    public void addVideo(Video video, String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/video/add");

        HttpEntity<?> requestEntity = getForm(video, authorizationToken);

        try {

            this.restTemplate.postForEntity(
                    expand,
                    requestEntity,
                    Void.class);

        } catch (Exception e) {
            throw new Exception(String.format("Невозможно добавить URL-адреса видео. %s", e.getMessage()));
        }

    }


    /**
     * Get blacklist customer csv
     *
     * @param authorizationToken token
     * @return csv bytes
     * @throws Exception error
     */
    public byte[] getBlacklistedCustomers(String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/getBlacklistedCustomers");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        try {

            return this.restTemplate.exchange(
                    expand,
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            ).getBody();

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }

    }

    /**
     * Deletes customer from black list by email
     *
     * @param authorizationToken authorization token
     * @param id                 id
     * @throws Exception error
     */
    public void deleteBlacklistCustomerById(String authorizationToken, String id) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/blacklist/delete");

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
     * Add customer to black list
     *
     * @param blacklistedCustomer customer
     * @param authorizationToken  authorization token
     * @throws Exception error
     */
    public void addBlacklistCustomer(BlacklistedCustomer blacklistedCustomer, String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/blacklist/add");

        HttpEntity<?> requestEntity = getForm(blacklistedCustomer, authorizationToken);

        try {
            this.restTemplate.postForEntity(
                    expand,
                    requestEntity,
                    Void.class);

        } catch (Exception e) {
            throw new Exception(String.format("Невозможно добавить клиента в черный список. %s", e.getMessage()));
        }
    }

    /**
     * Updates black list customer
     *
     * @param customer           customer
     * @param authorizationToken token
     */
    public void updateBlacklistedCustomer(BlacklistedCustomer customer, String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/blacklist/update");

        HttpEntity<?> requestEntity = getForm(customer, authorizationToken);

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
     * Get promo codes
     *
     * @param authorizationToken authorization token
     * @return csv bytes
     */
    public byte[] getPromoCodes(String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/getPromoCodes");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        try {
            return this.restTemplate.exchange(
                    expand,
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            ).getBody();

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

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/promocode/add");

        HttpEntity<?> requestEntity = getForm(promoCode, authorizationToken);

        try {

            this.restTemplate.postForEntity(
                    expand,
                    requestEntity,
                    Void.class);

        } catch (Exception e) {
            throw new Exception(String.format("Невозможно добавить Promo code. %s", e.getMessage()));
        }

    }

    /**
     * Update promo code
     *
     * @param promoCode          promo code model
     * @param authorizationToken authorization token
     */
    public void updatePromoCode(PromoCode promoCode, String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/promocode/update");

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
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/promocode/delete");

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
     * Get order details
     *
     * @param authorizationToken authorization token
     * @return csv bytes
     * @throws Exception error
     */
    public byte[] getOrderDetails(String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/getOrderDetails");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        try {
            return this.restTemplate.exchange(
                    expand,
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            ).getBody();

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    public byte[] getNotPayedOrders(String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/getNotPayedOrders");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        try {
            return this.restTemplate.exchange(
                    expand,
                    HttpMethod.GET,
                    requestEntity,
                    byte[].class
            ).getBody();

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    /**
     * Adds pictures
     *
     * @param authorizationToken authorization token
     * @param selectedFiles      files
     * @throws Exception error
     */
    public void addPictures(String authorizationToken, List<File> selectedFiles) throws Exception {

        List<PictureAddModel> pictureAddModels = new ArrayList<>();

        for (File file :
                selectedFiles) {
            pictureAddModels.add(new PictureAddModel(file.getName(), Utils.toBase64(DSSUtils.toByteArray(file))));
        }

        URI expand = this.restTemplate.getUriTemplateHandler().expand("/abio/management/products/add/pictures");

        HttpEntity<?> requestEntity = getForm(pictureAddModels, authorizationToken);

        try {

            this.restTemplate.postForEntity(
                    expand,
                    requestEntity,
                    Void.class);

        } catch (Exception e) {
            throw new Exception(String.format("Не возможно добавить картинки. %s", e.getMessage()));
        }

    }


    /**
     * Imports csv file
     *
     * @param csv                csv
     * @param currentStatusEnum  current status
     * @param authorizationToken authorization token
     */
    public void importCSV(byte[] csv, CurrentStatusEnum currentStatusEnum, String authorizationToken) throws Exception {

        URI expand = switch (currentStatusEnum) {
            case PRODUCT -> this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/updateProducts");
            case REGION_0, REGION_1 ->
                    this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/updateDeliveryRegions");
            case VIDEO -> this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/updateVideos");
            case BLACKLIST ->
                    this.restTemplate.getUriTemplateHandler().expand("/abio/management/csv/updateBlacklistedCustomers");
            default -> throw new Exception("Сначала выберите раздел.");
        };

        try {

            HttpEntity<?> requestEntity = getByteForm(csv, authorizationToken);
            this.restTemplate.exchange(expand, HttpMethod.POST, requestEntity, Void.class);

        } catch (Exception e) {
            throw new Exception(String.format("Невозможно импортировать данные. %s", e.getMessage()));
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

    public HttpEntity<?> getForm(Object object, String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);

        return new HttpEntity<>(object, headers);
    }

    public HttpEntity<?> getByteForm(byte[] csvFile, String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Authorization", authorizationToken);

        return new HttpEntity<>(csvFile, headers);

    }


}
