package com.pey.WowProfileStats.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pey.WowProfileStats.Model.AccountProfile;
import com.pey.WowProfileStats.Model.CharacterProfile;
import com.pey.WowProfileStats.repository.AccountProfileRepository;
import com.pey.WowProfileStats.repository.CharacterProfileRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Setter
@Service
public class WowService {

    private final WebClient wowProfileWebClient;
    private final ObjectMapper objectMapper;
    private final CharacterProfileRepository characterProfileRepository;
    private final AccountProfileRepository accountProfileRepository;

    private String namespace = "profile-eu";
    private String locale = "en_US";

    @Autowired
    public WowService(
            @Qualifier("oauth-webclient-builder") WebClient.Builder webClientBuilder,
            @Value("${blizzard.api.host}") String host,
            ObjectMapper objectMapper, CharacterProfileRepository characterProfileRepository1, AccountProfileRepository accountProfileRepository) {
        this.objectMapper = objectMapper;
        this.characterProfileRepository = characterProfileRepository1;
        this.accountProfileRepository = accountProfileRepository;
        this.wowProfileWebClient = webClientBuilder.baseUrl(host).build();
    }

    public String getProfile() throws Exception {
        String temp = wowProfileWebClient.get()
                .uri("/profile/user/wow?namespace={namespace}&locale={locale}", namespace, locale)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode rootNode = objectMapper.readTree(temp);
        String characterName = "";
        String realm = "";

        AccountProfile account = objectMapper.readValue(temp, AccountProfile.class);

        accountProfileRepository.save(account);

        System.out.println(rootNode.path("wow_accounts").path(0).path("characters").size());

        for (int i = 0; i < rootNode.path("wow_accounts").path(0).path("characters").size(); i++) {
            characterName = rootNode.path("wow_accounts").path(0).path("characters").path(i).path("name").asText();
            realm = rootNode.path("wow_accounts").path(0).path("characters").path(i).path("realm").path("slug").asText();

            System.out.println(i);
            System.out.println(characterName);
            System.out.println(realm);

            try {
                CharacterProfile character = getCharacterProfilesFromAccount(realm, characterName, account);
                character.setAccountId(account);
                account.setCharacters(character);
                characterProfileRepository.save(character);
            } catch (Exception e) {

            }
        }
        accountProfileRepository.save(account);

        return temp;
    }

    public String getCharacterProfileSummary(String realm, String characterName) throws JsonProcessingException {
        String temp = wowProfileWebClient.get()
                .uri("/profile/wow/character/{realm}/{characterName}?namespace={namespace}&locale={locale}", realm.toLowerCase(), characterName.toLowerCase(), namespace, locale)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        CharacterProfile character = objectMapper.readValue(temp, CharacterProfile.class);
        characterProfileRepository.save(character);

        return temp;
    }

    public CharacterProfile getCharacterProfilesFromAccount(String realm, String characterName, AccountProfile account) throws Exception {
        try {
            String temp = wowProfileWebClient.get()
                    .uri("/profile/wow/character/{realm}/{characterName}?namespace={namespace}&locale={locale}", realm.toLowerCase(), characterName.toLowerCase(), namespace, locale)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            CharacterProfile character = objectMapper.readValue(temp, CharacterProfile.class);
            return character;
        } catch (Exception e) {
            throw new Exception("error", e);
        }
    }

}
