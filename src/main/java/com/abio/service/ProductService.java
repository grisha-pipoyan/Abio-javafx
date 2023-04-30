package com.abio.service;

import com.abio.dto.ProductCsvDTO;
import com.abio.dto.ProductAdminDTO;
import com.abio.dto.PictureDTO;
import com.abio.utils.CSVUtils;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "/abio/management/products";

    /**
     * Finds all products
     *
     * @param authorizationToken token
     * @return csv of all products
     * @throws Exception if response code is not 200
     */
    public byte[] getAllProductsCSV(String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/get");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

//        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
//                .queryParam("type", type);

        try {

            ProductCsvDTO[] body = this.restTemplate.exchange(expand,
                    HttpMethod.GET,
                    requestEntity,
                    ProductCsvDTO[].class).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.productColumns);

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    public byte[] getAllProductsCSVByQuantity(String authorizationToken, String query) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/getByQuantityEquals");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("query", query);

        try {
            ProductCsvDTO[] body = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    ProductCsvDTO[].class).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.productColumns);

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    public byte[] getProductsByHavingDescription(String authorizationToken, boolean has) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/getProductsByDescription");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("has", has);

        try {
            ProductCsvDTO[] body = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    ProductCsvDTO[].class).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.productColumns);

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    public byte[] getProductsByHavingName(String authorizationToken, String searchString) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/getByNameContaining");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("searchString", searchString);

        try {
            ProductCsvDTO[] body = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    ProductCsvDTO[].class).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.productColumns);

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    public byte[] getProductsByHavingPictures(String authorizationToken, Boolean has) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/getProductsByHavingPictures");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("has", has);

        try {
            ProductCsvDTO[] body = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    ProductCsvDTO[].class).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.productColumns);

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    public byte[] getProductByProductCode(String authorizationToken, String productCode) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/getProductByProductCode");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(expand.toString())
                .queryParam("productCode", productCode);

        try {
            ProductCsvDTO[] body = this.restTemplate.exchange(builder.toUriString(),
                    HttpMethod.GET,
                    requestEntity,
                    ProductCsvDTO[].class).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.productColumns);

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
    public void updateProduct(ProductAdminDTO product, String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/update");

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

    /**
     * Adds pictures
     *
     * @param authorizationToken authorization token
     * @param selectedFiles      files
     * @throws Exception error
     */
    public void addPictures(String authorizationToken, List<File> selectedFiles) throws Exception {

        List<PictureDTO> pictureDTOS = new ArrayList<>();

        for (File file :
                selectedFiles) {
            pictureDTOS.add(new PictureDTO(file.getName(), Utils.toBase64(DSSUtils.toByteArray(file))));
        }

        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/add/pictures");

        HttpEntity<?> requestEntity = getForm(pictureDTOS, authorizationToken);

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
