package com.pey.WowProfileStats.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@Table(name = "characters")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterProfile {

    @Id
    @JsonProperty("id")
    private int characterId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountProfile accountId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("faction")
    private String faction;

    @JsonProperty("race")
    private String race;

    @JsonProperty("character_class")
    private String character_class;

    @JsonProperty("active_spec")
    private String active_spec;

    @JsonProperty("realm")
    private String realm;

    @JsonProperty("guild")
    private String guild;

    @JsonProperty("level")
    private String level;

    @JsonProperty("achievement_points")
    private String achievement_points;

    @JsonProperty("equipment")
    private String equipment;

    @JsonProperty("active_title")
    private String active_title;

    @JsonProperty("average_item_level")
    private double averageItemLevel;

    @JsonProperty("equipped_item_level")
    private double equippedItemLevel;

    public void setGender(Map<String, Object> gender) {
        this.gender = (String) gender.get("name");
    }

    public void setFaction(Map<String, Object> faction) {
        this.faction = (String) faction.get("name");
    }

    public void setRace(Map<String, Object> race) {
        this.race = (String) race.get("name");
    }

    public void setCharacter_class(Map<String, Object> character_class) {
        this.character_class = (String) character_class.get("name");
    }

    public void setActive_spec(Map<String, Object> active_spec) {
        this.active_spec = (String) active_spec.get("name");
    }

    public void setRealm(Map<String, Object> realm) {
        this.realm = (String) realm.get("name");
    }

    public void setGuild(Map<String, Object> guild) {
        this.guild = (String) guild.get("name");
    }

    public void setEquipment(Map<String, Object> equipment) {
        this.equipment = (String) equipment.get("href");
    }

    public void setActive_title(Map<String, Object> active_title) {
        this.active_title = (String) active_title.get("name");
    }

}
