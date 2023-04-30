package com.abio.service;

import com.abio.dto.OrderDetailsDTO;
import com.abio.utils.CSVUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class OrderService {
    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "/abio/management/order";

    /**
     * Get order details
     *
     * @param authorizationToken authorization token
     * @return csv bytes
     * @throws Exception error
     */
    public byte[] getOrderDetails(String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/get");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        try {
            OrderDetailsDTO[] body = this.restTemplate.exchange(
                    expand,
                    HttpMethod.GET,
                    requestEntity,
                    OrderDetailsDTO[].class
            ).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.orderColumns);

        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    public byte[] getNotPayedOrders(String authorizationToken) throws Exception {
        URI expand = this.restTemplate.getUriTemplateHandler().expand(baseUrl + "/getNotPayedOrders");

        HttpEntity<?> requestEntity = getForm(null, authorizationToken);

        try {
            OrderDetailsDTO[] body = this.restTemplate.exchange(
                    expand,
                    HttpMethod.GET,
                    requestEntity,
                    OrderDetailsDTO[].class
            ).getBody();

            return CSVUtils.convertProductToCSV(body, CSVUtils.orderColumns);
        } catch (Exception e) {
            throw new Exception(String.format("Не удалось загрузить данные. Попробуйте позже. %s", e.getMessage()));
        }
    }

    public HttpEntity<?> getForm(Object object, String authorizationToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);

        return new HttpEntity<>(object, headers);
    }

}
