package com.zinkworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zinkworks.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	Account findAccountByHashedAccountNumber(String paramString);
}
