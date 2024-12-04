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
        // Create a temporary file with valid card values
        Path tempFile = Files.createTempFile("cards", ".txt");
        List<String> cardValues = List.of(
            "1", "1", "1", "1", 
            "2", "2", "2", "2", 
            "3", "3", "3", "3", 
            "4", "4", "4", "4", 
            "5", "5", "5", "5", 
            "6", "6", "6", "6", 
            "7", "7", "7", "7", 
            "8", "8", "8", "8"
        );

        Files.write(tempFile, cardValues, StandardOpenOption.WRITE);

        // Act
        cardImplementor.loadCardsFromFile(tempFile.toString(), numOfPlayers);

        Files.deleteIfExists(tempFile); // Clean up temporary file

        // Assert
        List<Card> myCards = cardImplementor.getMyCards();
        assertEquals(8 * numOfPlayers, myCards.size(), "Loaded card count should match expected count.");
    }

    @Test
    void testShowCardValues() {
        // Arrange
        cardImplementor.createPlayers(numOfPlayers);
        String tempCardFile = createTempCardFile();

        // Act & Assert
        assertDoesNotThrow(() -> cardImplementor.loadCardsFromFile(tempCardFile, numOfPlayers),
            "LoadCardsFromFile should not throw any exceptions.");
        assertDoesNotThrow(() -> cardImplementor.showCardValues(),
            "ShowCardValues should not throw any exceptions.");
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

            // Create valid card values for the test
            for (int i = 1; i <= 8; i++) {
                for (int j = 0; j < numOfPlayers; j++) {
                    cardValues.append(i).append("\n");
                }
            }

            Files.writeString(tempFile, cardValues.toString());
            return tempFile.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary card file.", e);
        }
    }
}
