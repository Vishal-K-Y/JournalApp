package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.Entity.JournalEntity;
import net.engineeringdigest.journalApp.Entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public void saveEntry(JournalEntity journalEntity, String userName){
        User user=userService.findByUserName(userName).orElse(null);
        journalEntity.setDate(LocalDateTime.now());
        JournalEntity saved=journalEntryRepository.save(journalEntity);
        user.getJournalEntiies().add(saved);
        userService.saveEntry(user);
    }

    public void saveEntry(JournalEntity journalEntity){
        journalEntryRepository.save(journalEntity);
    }

    public List<JournalEntity> getAllJournal(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntity> findById(Long id){
        return journalEntryRepository.findById(id);
    }

    public void removeJournal(Long id, String userName){
        User user=userService.findByUserName(userName).orElse(null);
        user.getJournalEntiies().removeIf(x -> x.getId()==id);
        userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }

    public void updateJournal(Long id, JournalEntity jooruJournalEntity){
//        journalEntryRepository.
    }

//    public List<JournalEntity> findByUserName(String userName){
//
//    }
}
