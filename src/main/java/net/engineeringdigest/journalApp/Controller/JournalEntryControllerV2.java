package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.Entity.JournalEntity;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.Service.JournalEntryService;
import net.engineeringdigest.journalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journalv2")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user= userService.findByUserName(userName).orElse(null);
            List<JournalEntity> all= user.getJournalEntiies();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        System.out.println(user);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntity> addJournal(@RequestBody JournalEntity journal, @PathVariable String userName){
        try {
            journalEntryService.saveEntry(journal, userName);
            return new ResponseEntity<>(journal, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{ID}")
    public ResponseEntity<?> getJournalById(@PathVariable long ID){
        Optional<JournalEntity> journalEntity= journalEntryService.findById(ID);
        if(journalEntity.isPresent()){
            return new ResponseEntity<>(journalEntity.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{userName}/{ID}")
    public ResponseEntity<?> removeJournal(@PathVariable long ID, @PathVariable String userName){
        journalEntryService.removeJournal(ID, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{userName}/{id}")
    public ResponseEntity<?> updateJournalById(@PathVariable long id,
                                               @RequestBody JournalEntity journalEntity,
                                               @PathVariable String userName)
    {
        JournalEntity old=journalEntryService.findById(id).orElse(null);
        if(old!=null){
            old.setContent((journalEntity.getContent()==null || journalEntity.getContent().isEmpty())?old.getContent():journalEntity.getContent());
            old.setTitle((journalEntity.getTitle()==null || journalEntity.getContent().isEmpty())?old.getTitle():journalEntity.getTitle());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
