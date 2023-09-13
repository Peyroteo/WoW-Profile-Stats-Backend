package com.pey.WowProfileStats;

import com.pey.WowProfileStats.config.WebClientConfig;
import com.pey.WowProfileStats.controller.AuthorizedController;
import com.pey.WowProfileStats.controller.WelcomeController;
import com.pey.WowProfileStats.service.WowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(classes ={WowService.class, WelcomeController.class, WebClientConfig.class})
public class MyTests {

    private WebTestClient webTestClient;
    private final String characterName = "crimsmage";
    private final String realm = "draenor";

    @Value("${blizzard.api.host}")
    private String host;

    @Autowired
    private WelcomeController controller;

    /*
    @BeforeEach
    void setUp(@Value("${blizzard.api.host}") String host) {
        webTestClient = WebTestClient.bindToServer().baseUrl(host).build();
    }
     */

    @Test
    public void test1(){
        /*webTestClient.get().uri("/searchCharacter/{realm}/{characterName}?{accessToken}", realm.toLowerCase(), characterName.toLowerCase(), accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
         */
        WebTestClient.bindToController(controller)
                .build()
                .get()
                .uri(host + "/searchCharacter/{realm}/{characterName}?{accessToken}", realm.toLowerCase(), characterName.toLowerCase(), accessToken)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

}
