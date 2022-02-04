package dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RecipientDTO {
    @JsonProperty("tags")
    private List<String> tags;
    @JsonProperty("name")
    private String name;
    @JsonProperty("id")
    private int id;

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
