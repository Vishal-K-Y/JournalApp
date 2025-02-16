package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HashResponse {
    @JsonProperty("Digest")
    private String Digest;

    @JsonProperty("DigestEnc")
    private String DigestEnc;

    @JsonProperty("Type")
    private String Type;

    @JsonProperty("Key")
    private String Key;
}
