package com.pey.WowProfileStats.repository;

import com.pey.WowProfileStats.Model.CharacterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterProfileRepository extends JpaRepository<CharacterProfile, Long> {

    CharacterProfile findCharacterProfileByCharacterId(Long charactedId);

    Boolean existsCharacterProfileByCharacterId(Long charactedId);
}
