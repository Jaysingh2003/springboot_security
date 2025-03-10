package com.example.journalApp.service;

import com.example.journalApp.entity.JournalEntity;
import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;

    public void saveEntry(JournalEntity journalEntry, String userName) {
        UserEntity user = userService.findByUserName(userName);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        journalEntry.setUser(user);
        JournalEntity saved = journalRepository.save(journalEntry);
        user.getJournalEntries().add(saved);
    }

    public List<JournalEntity> getAllByUser(UserEntity user) {
        return journalRepository.findByUser(user);
    }



    public List<JournalEntity> getAll() {
        return journalRepository.findAll();
    }

    public Optional<JournalEntity> findById(Long id) {
        return journalRepository.findById(id);
    }

    public void deleteById(Long id, String userName) {
        UserEntity user = userService.findByUserName(userName);
        user.getJournalEntries().removeIf(x ->x.getId().equals(id));//here lambda expression is used
        userService.saveUser (user);   //updated user will save                                                            // means user ki saari journal entries delete ho jayegi
        journalRepository.deleteById(id);
    }

    public void saveEntry(JournalEntity journalEntry) {
        journalRepository.save(journalEntry);
    }
}