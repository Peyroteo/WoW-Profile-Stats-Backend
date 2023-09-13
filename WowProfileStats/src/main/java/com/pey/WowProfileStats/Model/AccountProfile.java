package com.pey.WowProfileStats.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountProfile {

    @Id
    @JsonProperty("id")
    @Column(name = "account_id")
    private Long accountId;

    @OneToMany
    @JoinColumn(name = "account_id")
    private List<CharacterProfile> characters = new ArrayList<>();

    public void setCharacters(CharacterProfile characters) {
        this.characters.add(characters);
    }
}
