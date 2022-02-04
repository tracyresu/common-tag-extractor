import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.InputDTO;
import dto.PairDTO;
import dto.RecipientDTO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommonTagExtractor {

    public static void main(String[] args) {
        printPairListWithCommonTags();
    }

    protected static void printPairListWithCommonTags() {
        try {
            String jsonString = readFileAsString("src/main/resources/data.json");
            InputDTO inputDTO = returnInputDto(jsonString);
            if (inputDTO != null) {
                List<RecipientDTO> recipientsWithTags = filterRecipientsWithMoreThanOneTag(inputDTO);
                List<PairDTO> pairList = getPairsWithCommonTags(recipientsWithTags);
                pairList.forEach(pair -> System.out.println(pair.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static List<RecipientDTO> filterRecipientsWithMoreThanOneTag(InputDTO inputDTO) {
        return inputDTO.getRecipients().stream()
                .filter(recipientDTO -> recipientDTO.getTags().size() > 1)
                .collect(Collectors.toList());
    }

    protected static String readFileAsString(String file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    protected static InputDTO returnInputDto(String jsonString) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, InputDTO.class);
    }

    protected static List<PairDTO> getPairsWithCommonTags(List<RecipientDTO> recipientsWithTags) {
        List<PairDTO> pairList = new ArrayList<>();

        for (int index = 0; index < recipientsWithTags.size(); index++) {
            for (int pairIndex = index+1; pairIndex < recipientsWithTags.size(); pairIndex++) {
                RecipientDTO recipient = recipientsWithTags.get(index);
                RecipientDTO pairRecipient = recipientsWithTags.get(pairIndex);

                List<String> commonTags = extractCommonTags(recipient, pairRecipient);
                if (!commonTags.isEmpty() && commonTags.size() >= 2) {
                    pairList.add(new PairDTO(recipient.getName(), pairRecipient.getName(), commonTags));
                }
            }
        }

        return pairList;
    }

    protected static List<String> extractCommonTags(RecipientDTO recipient, RecipientDTO pairRecipient) {
        List<String> commonTags = new ArrayList<>(recipient.getTags());
        commonTags.retainAll(pairRecipient.getTags());
        return commonTags;
    }
}
