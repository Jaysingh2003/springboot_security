package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntity;
import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.service.JournalService;
import com.example.journalApp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;


    //we want to post the  journal of a particular person which  is login  (localhost:8080/journal)   [post  call]
    @PostMapping
    public ResponseEntity<JournalEntity> createEntry(@RequestBody JournalEntity journalEntry) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        try {
            journalService.saveEntry(journalEntry, userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    //we want to see the journal of a particular person which  is login   (localhost:8080/journal) [get call]
    @GetMapping
    public ResponseEntity<List<JournalEntity>> getAllJournalEntriesByUser() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        UserEntity user = userService.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<JournalEntity> allEntries = journalService.getAllByUser(user);
        return new ResponseEntity<>(allEntries, HttpStatus.OK);
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntity> getJournalEntryById(@PathVariable Long myId) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        UserEntity userEntity = userService.findByUserName(userName);
        List<JournalEntity> collect = userEntity.getJournalEntries().stream().filter(x ->x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntity> journalEntity=journalService.findById(myId);
            if(journalEntity.isPresent()){
                return new ResponseEntity<>(journalEntity.get(), HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @DeleteMapping("id/{myid}")
    public ResponseEntity<?> deleteById(@PathVariable Long myid) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        boolean removed = journalService.deleteById(myid, userName);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable long myId,  @RequestBody JournalEntity newEntry) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        UserEntity user = userService.findByUserName(userName);
        List<JournalEntity> collect = user.getJournalEntries().stream().filter(x ->x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntity> journalEntity=journalService.findById(myId);
            if(journalEntity.isPresent()){
                JournalEntity oldEntry = journalEntity.get();
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());
                journalService.saveEntry(oldEntry);
                return new ResponseEntity<>(oldEntry, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}