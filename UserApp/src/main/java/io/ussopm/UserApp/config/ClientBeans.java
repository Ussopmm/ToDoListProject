package io.ussopm.UserApp.config;

import io.ussopm.UserApp.client.RestClientTaskRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientTaskRestClient restClient(@Value("${task.services.url:http://localhost:8081}") String taskBaseUrl) {
        return new RestClientTaskRestClient(RestClient.builder()
                .baseUrl(taskBaseUrl)
                .build());
    }
}
