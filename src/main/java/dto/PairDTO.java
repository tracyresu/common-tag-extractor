package dto;

import java.util.List;

public class PairDTO {
    private final String firstRecipientName;
    private final String secondRecipientName;
    private final List<String> commonTags;

    public PairDTO(String firstRecipientName, String secondRecipientName, List<String> commonTags) {
        this.firstRecipientName = firstRecipientName;
        this.secondRecipientName = secondRecipientName;
        this.commonTags = commonTags;
    }

    public String getFirstRecipientName() {
        return firstRecipientName;
    }

    public String getSecondRecipientName() {
        return secondRecipientName;
    }

    @Override
    public String toString() {
        return String.format("%s, %s - [%s]" , this.firstRecipientName, this.secondRecipientName, concatCommonTags());
    }

    public String concatCommonTags() {
        return String.join(", ", this.commonTags);
    }
}
