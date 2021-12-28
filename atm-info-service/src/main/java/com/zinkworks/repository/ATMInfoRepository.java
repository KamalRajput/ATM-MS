package com.zinkworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zinkworks.entity.ATMInfo;

@Repository
public interface ATMInfoRepository extends JpaRepository<ATMInfo, Integer> {
}
