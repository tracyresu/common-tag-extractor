import com.fasterxml.jackson.core.JsonProcessingException;
import dto.InputDTO;
import dto.PairDTO;
import dto.RecipientDTO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommonTagExtractorTest {
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;

    private final String TEST_JSON_STRING = "{\"recipients\":[{\"tags\":[\"promo\",\"buyer\",\"clicker\"," +
            "\"non-clicker\"],\"name\":\"Maura Hickman\",\"id\":0},{\"tags\":[\"shopping\",\"clicker\"],\"name\":" +
            "\"Luisa Rutledge\",\"id\":1},{\"tags\":[\"shopping\"],\"name\":\"Ruby Goff\",\"id\":6}]}";

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void printHelloWorld() {
        CommonTagExtractor.printPairListWithCommonTags();
        assertEquals("Maura Hickman, Fern Wise - [promo, clicker, non-clicker]\r\n" +
                "Maura Hickman, Elena Vega - [buyer, clicker]\r\n" +
                "Maura Hickman, Alexandra Jacobson - [promo, non-clicker]\r\n" +
                "Maura Hickman, Burt Hampton - [promo, non-clicker]\r\n" +
                "Maura Hickman, Sylvia Norman - [buyer, clicker]\r\n" +
                "Maura Hickman, Colon Reynolds - [promo, buyer, clicker]\r\n" +
                "Luisa Rutledge, Sylvia Norman - [shopping, clicker]\r\n" +
                "Luisa Rutledge, Colon Reynolds - [shopping, clicker]\r\n" +
                "Fern Wise, Alexandra Jacobson - [promo, non-clicker]\r\n" +
                "Fern Wise, Burt Hampton - [promo, non-clicker]\r\n" +
                "Fern Wise, Colon Reynolds - [promo, clicker]\r\n" +
                "Elena Vega, Sylvia Norman - [buyer, clicker]\r\n" +
                "Elena Vega, Colon Reynolds - [buyer, clicker]\r\n" +
                "Alexandra Jacobson, Burt Hampton - [promo, non-clicker]\r\n" +
                "Sylvia Norman, Jana Stevenson - [shopping, buyer]\r\n" +
                "Sylvia Norman, Colon Reynolds - [shopping, buyer, clicker]\r\n" +
                "Jana Stevenson, Colon Reynolds - [shopping, buyer]", outContent.toString().trim());
    }

    @Test
    public void shouldReturnFilteredListOfMoreThanOneTagGivenListWithVariousTagCount() throws JsonProcessingException {
        InputDTO input = CommonTagExtractor.returnInputDto(TEST_JSON_STRING);
        assertEquals(3, input.getRecipients().size());
        List<RecipientDTO> result = CommonTagExtractor.filterRecipientsWithMoreThanOneTag(input);
        assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnStringFromFileGivenCorrectFilePath() throws Exception {
        String result = CommonTagExtractor.readFileAsString("src/test/resources/data.json");
        assertEquals(TEST_JSON_STRING, result);
    }

    @Test
    public void shouldReturnInputDtoGivenJsonString() throws JsonProcessingException {
        InputDTO input = CommonTagExtractor.returnInputDto(TEST_JSON_STRING);
        List<RecipientDTO> recipientDTOList = input.getRecipients();

        assertEquals("Maura Hickman", recipientDTOList.get(0).getName());
        assertEquals(0, recipientDTOList.get(0).getId());
        assertEquals("Luisa Rutledge", recipientDTOList.get(1).getName());
        assertEquals(1, recipientDTOList.get(1).getId());
    }

    @Test
    public void shouldReturnPairsGivenRecipientsWithCommonTags() {
        List<RecipientDTO> recipientDTOList = new ArrayList<>();
        RecipientDTO recipient = new RecipientDTO();
        recipient.setId(0);
        recipient.setName("Maura Hickman");
        recipient.setTags(Arrays.asList("promo", "buyer", "clicker"));
        recipientDTOList.add(recipient);

        recipient = new RecipientDTO();
        recipient.setId(1);
        recipient.setName("Luisa Rutledge");
        recipient.setTags(Arrays.asList("shopping", "buyer", "clicker"));
        recipientDTOList.add(recipient);

        List<PairDTO> pairList = CommonTagExtractor.getPairsWithCommonTags(recipientDTOList);
        assertEquals("Maura Hickman, Luisa Rutledge - [buyer, clicker]", pairList.get(0).toString());
        assertEquals("Maura Hickman", pairList.get(0).getFirstRecipientName());
        assertEquals("Luisa Rutledge", pairList.get(0).getSecondRecipientName());
        assertEquals("buyer, clicker", pairList.get(0).concatCommonTags());
    }

    @Test
    public void shouldExtractCommonTagsGivenRecipientsWithMatchingTags() {
        RecipientDTO recipient = new RecipientDTO();
        recipient.setId(0);
        recipient.setName("Maura Hickman");
        recipient.setTags(Arrays.asList("promo", "buyer", "clicker"));

        RecipientDTO pairRecipient = new RecipientDTO();
        pairRecipient.setId(1);
        pairRecipient.setName("Luisa Rutledge");
        pairRecipient.setTags(Arrays.asList("shopping", "buyer", "clicker"));
        List<String> commonTags = CommonTagExtractor.extractCommonTags(recipient, pairRecipient);

        assertEquals(Arrays.asList("buyer", "clicker"), commonTags);
    }
}