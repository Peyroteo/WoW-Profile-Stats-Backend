package com.pey.WowProfileStats.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pey.WowProfileStats.service.WowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    @Autowired
    private WowService wowService;

    @GetMapping("/characterTest")
    public ResponseEntity<String> characterProfileSummary() throws JsonProcessingException {
        //return wowService.getCharacterProfileSummary("draenor", "crimsmage");
        return ResponseEntity.ok("Success");
    }

    @GetMapping("")
    public String welcome(){
        return "Welcome";
    }

    @RequestMapping(value ="/searchCharacter/{realm}/{characterName}", method = RequestMethod.GET)
    public ResponseEntity<String> searchCharacter(@PathVariable String realm, @PathVariable String characterName) throws Exception {
        return ResponseEntity.ok(wowService.getCharacterProfileSummary(realm, characterName));
    }

    @RequestMapping(value ="/getAllCharactersFromGuild/{realm}/{guildName}", method = RequestMethod.GET)
    public ResponseEntity<String> searchGuildGetAllCharacters(@PathVariable String realm, @PathVariable String guildName) throws Exception {
        return ResponseEntity.ok(wowService.getAllCharactersFromGuild(realm, guildName));
    }

}
