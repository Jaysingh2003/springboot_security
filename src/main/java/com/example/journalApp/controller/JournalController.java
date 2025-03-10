package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntity;
import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.service.JournalService;
import com.example.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntity> createEntry(@RequestBody JournalEntity journalEntry, @PathVariable String userName) {
        try {
            journalService.saveEntry(journalEntry, userName);
            return new ResponseEntity<>(journalEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("{userName}")
    public ResponseEntity<List<JournalEntity>> getAllJournalEntriesByUser(@PathVariable String userName) {
        UserEntity user = userService.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<JournalEntity> allEntries = journalService.getAllByUser(user);
        return new ResponseEntity<>(allEntries, HttpStatus.OK);
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntity> getJournalEntryById(@PathVariable Long myId) {
        Optional<JournalEntity> journalEntity = journalService.findById(myId);
        return journalEntity.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("id/{userName}/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id, @PathVariable String userName) {
        journalService.deleteById(id, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PutMapping("id/{userName}/{myId}")
    public ResponseEntity<?> updateJournalEntry(
            @PathVariable long myId,
            @RequestBody JournalEntity newEntry,
            @PathVariable String userName
    ) {
        JournalEntity oldEntry = journalService.findById(myId).orElse(null);
        if (oldEntry != null) {
            // Update fields if they are not null/empty
            oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : oldEntry.getContent());

            // Save the entry associated with the user
            journalService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}