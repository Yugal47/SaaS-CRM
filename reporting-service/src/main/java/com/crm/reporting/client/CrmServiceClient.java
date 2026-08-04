package com.crm.reporting.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Calls crm-service's own REST API rather than touching its database directly -
 * each microservice owns its data, and this is how a service borrows another's
 * data without breaking that boundary. The caller's bearer token is forwarded
 * so crm-service can independently verify tenant/role - no separate service-to-
 * service credential is needed since all services share the same JWT secret.
 */
@Component
public class CrmServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CrmServiceClient(RestTemplate restTemplate, @Value("${crm-service.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<Map<String, Object>> getCustomers(String bearerToken) {
        return get("/api/customers", bearerToken);
    }

    public List<Map<String, Object>> getLeads(String bearerToken) {
        return get("/api/leads", bearerToken);
    }

    public List<Map<String, Object>> getDeals(String bearerToken) {
        return get("/api/deals", bearerToken);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> get(String path, String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", bearerToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(baseUrl + path, HttpMethod.GET, entity, List.class);
        return (List<Map<String, Object>>) (List<?>) response.getBody();
    }
}
