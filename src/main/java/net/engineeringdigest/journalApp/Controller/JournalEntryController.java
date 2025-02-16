package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.Entity.JournalEntity;
import net.engineeringdigest.journalApp.JournalApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/_journal")
public class JournalEntryController {

    private Map<Long, JournalEntity> journalEntnty=new HashMap<>();

    @GetMapping("/health")
    public String healthCheck(){
        return "OK";
    }

    @GetMapping
    public List<JournalEntity> getAll(){
        return new ArrayList<>(journalEntnty.values());
    }

    @PostMapping
    public String addJournal(@RequestBody JournalEntity journal){
        this.journalEntnty.put(journal.getId(), journal);
        return "added";
    }

    @GetMapping("/id/{ID}")
    public JournalEntity getJournalById(@PathVariable long ID){
        return journalEntnty.get(ID);
    }

    @DeleteMapping("/id/{ID}")
    public JournalEntity removeJournal(@PathVariable long ID){
        return journalEntnty.remove(ID);
    }

    @PutMapping("/id/{id}")
    public JournalEntity updateJournalById(@PathVariable long id,@RequestBody JournalEntity journal){
        this.journalEntnty.put(id, journal);
       return journalEntnty.get(id);
    }
}
