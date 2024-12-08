import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardImplTest {

    private CardImplementor cardImplementor;
    private final int numOfPlayers = 4;
    private PlayerMoveThread playerMoveThread;

    // Create 4 players before starting all test cases
    @BeforeAll
    void setUp() {
        cardImplementor = new CardImplementor();
        cardImplementor.createPlayers(numOfPlayers);
        playerMoveThread = CardImplementor.myPlayers.get(1);
    }

    // Delete all player and deck files created after all test cases
    @AfterAll
    void deleteFiles() {
        playerMoveThread.deletePlayerFile("player1_output.txt");
        playerMoveThread.deletePlayerFile("player2_output.txt");
        playerMoveThread.deletePlayerFile("player3_output.txt");
        playerMoveThread.deletePlayerFile("player4_output.txt");
        playerMoveThread.deletePlayerFile("player5_output.txt");
        playerMoveThread.deletePlayerFile("player6_output.txt");
        playerMoveThread.deletePlayerFile("player7_output.txt");
        playerMoveThread.deletePlayerFile("player8_output.txt");
        playerMoveThread.deletePlayerFile("player9_output.txt");
        
        Deck leftDeck = playerMoveThread.getLeftDeck();
        leftDeck.deleteDeckFile("deck1_output.txt");
        Deck rightDeck = playerMoveThread.getRightDeck();
        rightDeck.deleteDeckFile("deck2_output.txt");
        Deck leftDeck1 = playerMoveThread.getLeftDeck();
        leftDeck1.deleteDeckFile("deck3_output.txt");
        Deck rightDeck1 = playerMoveThread.getRightDeck();
        rightDeck1.deleteDeckFile("deck4_output.txt");

        
    }


    @Test
    void testCreatePlayers() {
        assertEquals(numOfPlayers, CardImplementor.myDecks.size(), "Number of decks should match number of players.");
        assertEquals(numOfPlayers, CardImplementor.myPlayers.size(), "Number of players should match input.");
    }

    @Test
    void testShowPlayerDetails1() {
        
        String playerDetails = cardImplementor.showPlayerDetails(0);
        assertNotNull(playerDetails, "Player details should not be null.");
        assertTrue(playerDetails.contains("Left deck:"), "Player details should include left deck info.");
        assertTrue(playerDetails.contains("Right deck:"), "Player details should include right deck info.");
    }

    @Test
    void testShowPlayerDetails2() {
    
        String playerDetails = cardImplementor.showPlayerDetails(0);
        assertNotNull(playerDetails, "Player details should not be null.");
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
        
        String tempCardFile = createTempCardFile();

        // Act & Assert
        assertDoesNotThrow(() -> cardImplementor.loadCardsFromFile(tempCardFile, numOfPlayers),
            "LoadCardsFromFile should not throw any exceptions.");
        assertDoesNotThrow(() -> cardImplementor.showCardValues(),
            "ShowCardValues should not throw any exceptions.");
    }

    @Test
    void testDistributeToPlayers() {
        
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToPlayers(numOfPlayers);

        for (PlayerMoveThread player : CardImplementor.myPlayers) {
            assertEquals(4, player.getCardValues().length, "Each player should have 4 cards in hand.");
        }
    }

    @Test
    void testDistributeToDecks() {
       
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToDecks(numOfPlayers);

        for (Deck deck : CardImplementor.myDecks) {
            assertEquals(4, deck.getSize(), "Each deck should have 4 cards.");
        }
    }

    @Test
    void testShowCardsInDeck1() {
      
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToDecks(numOfPlayers);
        assertDoesNotThrow(() -> cardImplementor.showCardsInDeck(0), "showCardsInDeck should not throw any exceptions.");
    }

    @Test
    void testShowCardsInDeck2() {
        
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToDecks(numOfPlayers);
        assertDoesNotThrow(() -> cardImplementor.showCardsInDeck(5), "should handle invalid deck IDs.");
        assertDoesNotThrow(() -> cardImplementor.showCardsInDeck(-1), "should handle negative deck IDs.");
    }   

    @Test
    void testShowCardsInHand1() {
        
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToPlayers(numOfPlayers);
        assertDoesNotThrow(() -> cardImplementor.showCardsInHand(0), "showCardsInHand should not throw any exceptions.");
    }

    @Test
    void testShowCardsInHand2() {
        
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToPlayers(numOfPlayers);
        assertDoesNotThrow(() -> cardImplementor.showCardsInHand(7), "should handle invalid deck IDs.");
        assertDoesNotThrow(() -> cardImplementor.showCardsInHand(-4), "should handle negative deck IDs.");
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
