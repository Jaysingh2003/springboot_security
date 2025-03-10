package com.example.journalApp.repository;

import com.example.journalApp.entity.JournalEntity;
import com.example.journalApp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JournalRepository extends JpaRepository<JournalEntity, Long> {
    List<JournalEntity> findByUser(UserEntity user);

}