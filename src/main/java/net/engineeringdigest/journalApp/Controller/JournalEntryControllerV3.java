package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.Entity.JournalEntity;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.Service.JournalEntryService;
import net.engineeringdigest.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV3 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user= userService.findByUserName(userName).orElse(null);
        List<JournalEntity> all= user.getJournalEntiies();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        System.out.println(user);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntity> addJournal(@RequestBody JournalEntity journal){
        try {
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String userName= authentication.getName();
            journalEntryService.saveEntry(journal, userName);
            return new ResponseEntity<>(journal, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{ID}")
    public ResponseEntity<?> getJournalById(@PathVariable long ID){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user=userService.findByUserName(userName).get();
        List<JournalEntity> collect= user.getJournalEntiies().stream().filter(x-> x.getId()==ID).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntity> journalEntity= journalEntryService.findById(ID);
            if(journalEntity.isPresent()){
                return new ResponseEntity<>(journalEntity.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{ID}")
    public ResponseEntity<?> removeJournal(@PathVariable long ID){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user=userService.findByUserName(userName).get();
        List<JournalEntity> collect= user.getJournalEntiies().stream().filter(x-> x.getId()==ID).collect(Collectors.toList());
        if(!collect.isEmpty()){
            journalEntryService.removeJournal(ID, userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable long id,
                                               @RequestBody JournalEntity journalEntity)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName= authentication.getName();
        User user=userService.findByUserName(userName).get();
        List<JournalEntity> collect= user.getJournalEntiies().stream().filter(x-> x.getId()==id).collect(Collectors.toList());
        if(!collect.isEmpty()){
            JournalEntity old=journalEntryService.findById(id).get();
            old.setContent((journalEntity.getContent()==null || journalEntity.getContent().isEmpty())?old.getContent():journalEntity.getContent());
            old.setTitle((journalEntity.getTitle()==null || journalEntity.getContent().isEmpty())?old.getTitle():journalEntity.getTitle());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
