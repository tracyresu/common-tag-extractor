package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InputDTO {
    @JsonProperty("recipients")
    private List<RecipientDTO> recipients;

    public List<RecipientDTO> getRecipients() {
        return recipients;
    }
}
