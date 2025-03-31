package com.example.journalApp.service;

import com.example.journalApp.entity.JournalEntity;
import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.repository.JournalRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public void saveEntry(JournalEntity journalEntry, String userName) {
        try {
            UserEntity user = userService.findByUserName(userName);
            JournalEntity saved = journalRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        } catch(Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occured while saving the entry",e);
        }
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


    @Transactional
    public boolean deleteById(Long id, String userName) {
        boolean removed=false;
        try {
            UserEntity user = userService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));//here lambda expression is used
            if (removed) {
                userService.saveUser(user);   //updated user will save                                                            // means user ki saari journal entries delete ho jayegi
                journalRepository.deleteById(id);
            }
        } catch(Exception e){
              log.error("ERROR",e);
                throw new RuntimeException("ann error occourd while delting", e);
            }
        return removed;
        }




    public void saveEntry(JournalEntity journalEntry) {
        journalRepository.save(journalEntry);
    }
}