package com.abio.service;

import com.abio.dto.VideoAdminDTO;
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
public class VideoService {


    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "/abio/management/video";


    /**
     * Get videos csv
     *
     * @param authorizationToken authorization token
     * @return csv bytes
     * @throws Exception error
     */
    public byte[] getVideos(String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/get");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        try {

            VideoAdminDTO[] body = this.restTemplate.exchange(
                    expand,
                    HttpMethod.GET,
                    requestEntity,
                    VideoAdminDTO[].class
            ).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.videoColumns);

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    /**
     * Updates video
     *
     * @param videoAdminDTO      video
     * @param authorizationToken authorization token
     * @throws Exception error
     */
    public void updateVideo(VideoAdminDTO videoAdminDTO, String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/update");

        HttpEntity<?> requestEntity = getForm(videoAdminDTO, authorizationToken);

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
     * Adds new video
     *
     * @param videoAdminDTO      Video model
     * @param authorizationToken token
     * @throws Exception error
     */
    public void addVideo(VideoAdminDTO videoAdminDTO, String authorizationToken) throws Exception {

        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/add");

        HttpEntity<?> requestEntity = getForm(videoAdminDTO, authorizationToken);

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
