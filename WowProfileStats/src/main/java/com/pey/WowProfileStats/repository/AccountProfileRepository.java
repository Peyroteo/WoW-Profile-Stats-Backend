package com.pey.WowProfileStats.repository;

import com.pey.WowProfileStats.Model.AccountProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountProfileRepository extends JpaRepository<AccountProfile, Long> {

    AccountProfile findAccountProfileByAccountId(Long accountId);

    Boolean existsAccountProfileByAccountId(Long accountId);

}
