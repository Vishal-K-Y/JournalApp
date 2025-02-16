package net.engineeringdigest.journalApp.Entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "config_journal_app")
@Getter
@Setter
public class ConfigJournalAppEntity {

    private String key;
    private String value;

}
