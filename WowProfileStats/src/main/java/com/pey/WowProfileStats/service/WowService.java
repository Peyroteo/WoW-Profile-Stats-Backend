package com.pey.WowProfileStats.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pey.WowProfileStats.Model.AccountProfile;
import com.pey.WowProfileStats.Model.CharacterClass;
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
    private final WebClient wowProfileWebClientRaider;
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
        this.wowProfileWebClientRaider = webClientBuilder.build();
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

    public String getCharacterProfileSummary(String realm, String characterName) throws Exception {
        String temp = wowProfileWebClient.get()
                .uri("/profile/wow/character/{realm}/{characterName}?namespace={namespace}&locale={locale}", realm.toLowerCase(), characterName.toLowerCase(), namespace, locale)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        CharacterProfile character = objectMapper.readValue(temp, CharacterProfile.class);
        character.setVaultWeekly(getVaultsPreviousWeekly(realm, characterName));
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
            character.setVaultWeekly(getVaultsPreviousWeekly(realm, characterName));
            return character;
        } catch (Exception e) {
            throw new Exception("error", e);
        }
    }

    public int getVaultsPreviousWeekly(String realm, String characterName) throws Exception {
        try {
            String temp = wowProfileWebClientRaider.get()
                    .uri("https://raider.io/api/v1/characters/profile?region=eu&realm={realm}&name={characterName}&fields=mythic_plus_previous_weekly_highest_level_runs", realm.toLowerCase(), characterName.toLowerCase())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode rootNode = objectMapper.readTree(temp);
            int weeklyVaults = 0;
            weeklyVaults = rootNode.path("mythic_plus_previous_weekly_highest_level_runs").size();
            return weeklyVaults;
        } catch (Exception e) {
            throw new Exception("error", e);
        }
    }

    public String getAllCharactersFromGuild(String realmName, String guildName) throws Exception {
        try {
            String temp = wowProfileWebClient.get()
                    .uri("/data/wow/guild/{realmName}/{guildName}/roster?namespace={namespace}&locale={locale}", realmName.toLowerCase(), guildName.toLowerCase(), namespace, locale)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            String result = "";

            JsonNode rootNode = objectMapper.readTree(temp);
            for(int i = 0; i<rootNode.path("members").size(); i++){
                int characterId = rootNode.path("members").path(i).path("character").path("id").asInt();
                String characterName = rootNode.path("members").path(i).path("character").path("name").asText();
                String characterLvl = rootNode.path("members").path(i).path("character").path("level").asText();
                String characterClass = CharacterClass.values()[Integer.parseInt(rootNode.path("members").path(i).path("character").path("playable_class").path("id").asText())-1].name();

                CharacterProfile characterTemp = new CharacterProfile();
                if(Integer.parseInt(characterLvl) == 70) {
                    characterTemp.setVaultWeekly(getVaultsPreviousWeekly(realmName, characterName));
                }
                characterTemp.setCharacterId(characterId);
                characterTemp.setName(characterName);
                characterTemp.setLevel(characterLvl);
                characterTemp.setCharacter_class2(characterClass);
                characterProfileRepository.save(characterTemp);
                result = "{\"CharacterName\": " + characterName + " \"LvL\": " + characterLvl + "\"Class\": " + characterClass + "}";
                System.out.println(result);
            }
            return "done";
        } catch (Exception e) {
            throw new Exception("error", e);
        }
    }

    public void addCharacterToRaidTeam(CharacterProfile characterProfile){

    }

}
