package com.pey.WowProfileStats.controller;

import com.pey.WowProfileStats.service.WowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestController
@RequestMapping("/authorized")
public class AuthorizedController {

    @Autowired
    private WowService wowService;

    @GetMapping(path ="/profile")
    public ResponseEntity<String> profile() throws Exception {
        if(!wowService.getProfile().isEmpty()){
            return ResponseEntity.ok("Success");
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("")
    public String stats(){
        return "authorized";
    }

    @GetMapping("/getAccessToken")
    public OAuth2AccessToken test(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient){
        OAuth2AccessToken accessToken = oAuth2AuthorizedClient.getAccessToken();
        System.out.println(accessToken.getTokenValue());
        return accessToken;
    }

    @GetMapping("/getAuthorizedClient")
    public OAuth2AuthorizedClient test2(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient oAuth2AuthorizedClient){
        return oAuth2AuthorizedClient;
    }




}
