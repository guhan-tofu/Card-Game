import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardImplTest {

    private CardImplementor cardImplementor;
    private final int numOfPlayers = 4;

    @BeforeEach
    void setUp() {
        cardImplementor = new CardImplementor();
    }

    @Test
    void testCreatePlayers() {
        cardImplementor.createPlayers(numOfPlayers);
        assertEquals(numOfPlayers, CardImplementor.myDecks.size(), "Number of decks should match number of players.");
        assertEquals(numOfPlayers, CardImplementor.myPlayers.size(), "Number of players should match input.");
    }

    @Test
    void testShowPlayerDetails() {
        cardImplementor.createPlayers(numOfPlayers);
        String playerDetails = cardImplementor.showPlayerDetails(0);
        assertNotNull(playerDetails, "Player details should not be null.");
        assertTrue(playerDetails.contains("Left deck:"), "Player details should include left deck info.");
        assertTrue(playerDetails.contains("Right deck:"), "Player details should include right deck info.");
    }

    @Test
    void testLoadCardsFromFile() throws IOException {
        // Create a temporary file with card values
        Path tempFile = Files.createTempFile("cards", ".txt");
        List<String> cardValues = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32");
        Files.write(tempFile, cardValues, StandardOpenOption.WRITE);

        cardImplementor.loadCardsFromFile(tempFile.toString(), numOfPlayers);

        Files.deleteIfExists(tempFile); // Clean up temporary file

        List<Card> myCards = cardImplementor.getMyCards();

        assertEquals(8 * numOfPlayers, myCards.size(), "Loaded card count should match expected count.");
    }

    @Test
    void testShowCardValues() {
        cardImplementor.createPlayers(numOfPlayers);
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        assertDoesNotThrow(() -> cardImplementor.showCardValues(), "ShowCardValues should not throw any exceptions.");
    }

    @Test
    void testDistributeToPlayers() {
        cardImplementor.createPlayers(numOfPlayers);
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToPlayers(numOfPlayers);

        for (PlayerMoveThread player : CardImplementor.myPlayers) {
            assertEquals(4, player.getCardValues().length, "Each player should have 4 cards in hand.");
        }
    }

    @Test
    void testDistributeToDecks() {
        cardImplementor.createPlayers(numOfPlayers);
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToDecks(numOfPlayers);

        for (Deck deck : CardImplementor.myDecks) {
            assertEquals(4, deck.getSize(), "Each deck should have 4 cards.");
        }
    }

    @Test
    void testShowCardsInDeck() {
        cardImplementor.createPlayers(numOfPlayers);
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToDecks(numOfPlayers);
        assertDoesNotThrow(() -> cardImplementor.showCardsInDeck(0), "showCardsInDeck should not throw any exceptions.");
    }

    @Test
    void testShowCardsInHand() {
        cardImplementor.createPlayers(numOfPlayers);
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToPlayers(numOfPlayers);
        assertDoesNotThrow(() -> cardImplementor.showCardsInHand(0), "showCardsInHand should not throw any exceptions.");
    }

    private String createTempCardFile() {
        try {
            Path tempFile = Files.createTempFile("cards", ".txt");
            StringBuilder cardValues = new StringBuilder();

            for (int i = 1; i <= 8 * numOfPlayers; i++) {
                cardValues.append(i).append("\n");
            }

            Files.writeString(tempFile, cardValues.toString());
            return tempFile.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary card file.", e);
        }
    }
}
