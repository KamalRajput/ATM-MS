package com.zinkworks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zinkworks.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {}
