import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CardImplTest {

    private static CardImplementor cardImplementor;
    private static final int numOfPlayers = 4;
    private static PlayerMoveThread playerMoveThread;

    // Create 4 players before starting all test cases
    @BeforeClass
    public static void setUp() {
        cardImplementor = new CardImplementor();
        cardImplementor.createPlayers(numOfPlayers);
        playerMoveThread = CardImplementor.myPlayers.get(1);
    }

    // Delete all player and deck files created after all test cases
    @AfterClass
    public static void deleteFiles() {
        playerMoveThread.deletePlayerFile("player1_output.txt");
        playerMoveThread.deletePlayerFile("player2_output.txt");
        playerMoveThread.deletePlayerFile("player3_output.txt");
        playerMoveThread.deletePlayerFile("player4_output.txt");
        playerMoveThread.deletePlayerFile("player5_output.txt");
        playerMoveThread.deletePlayerFile("player6_output.txt");
        playerMoveThread.deletePlayerFile("player7_output.txt");
        playerMoveThread.deletePlayerFile("player8_output.txt");
        playerMoveThread.deletePlayerFile("player9_output.txt");
        playerMoveThread.deletePlayerFile("player10_output.txt");
        
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
    public void testCreatePlayers() {
        assertEquals("Number of decks should match number of players.", numOfPlayers, CardImplementor.myDecks.size());
        assertEquals("Number of players should match input.", numOfPlayers, CardImplementor.myPlayers.size());
    }

    @Test
    public void testShowPlayerDetails1() {
        String playerDetails = cardImplementor.showPlayerDetails(0);
        assertNotNull("Player details should not be null.", playerDetails);
        assertTrue("Player details should include left deck info.", playerDetails.contains("Left deck:"));
        assertTrue("Player details should include right deck info.", playerDetails.contains("Right deck:"));
    }

    @Test
    public void testShowPlayerDetails2() {
        String playerDetails = cardImplementor.showPlayerDetails(0);
        assertNotNull("Player details should not be null.", playerDetails);
    }

    @Test
    public void testLoadCardsFromFile() throws IOException {
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
        assertEquals("Loaded card count should match expected count.", 8 * numOfPlayers, myCards.size());
    }

    @Test
    public void testShowCardValues() {
        String tempCardFile = createTempCardFile();

        // Act & Assert
        try {
            cardImplementor.loadCardsFromFile(tempCardFile, numOfPlayers);
            cardImplementor.showCardValues();
        } catch (Exception e) {
            fail("LoadCardsFromFile or ShowCardValues should not throw any exceptions.");
        }
    }

    @Test
    public void testDistributeToPlayers() {
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToPlayers(numOfPlayers);

        for (PlayerMoveThread player : CardImplementor.myPlayers) {
            assertEquals("Each player should have 4 cards in hand.", 4, player.getCardValues().length);
        }
    }

    @Test
    public void testDistributeToDecks() {
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToDecks(numOfPlayers);

        for (Deck deck : CardImplementor.myDecks) {
            assertEquals("Each deck should have 4 cards.", 4, deck.getSize());
        }
    }

    @Test
    public void testShowCardsInDeck1() {
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToDecks(numOfPlayers);
        try {
            cardImplementor.showCardsInDeck(0);
        } catch (Exception e) {
            fail("showCardsInDeck should not throw any exceptions.");
        }
    }

    @Test
    public void testShowCardsInDeck2() {
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToDecks(numOfPlayers);
        try {
            cardImplementor.showCardsInDeck(5);
            cardImplementor.showCardsInDeck(-1);
        } catch (Exception e) {
            fail("should handle invalid deck IDs.");
        }
    }

    @Test
    public void testShowCardsInHand1() {
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToPlayers(numOfPlayers);
        try {
            cardImplementor.showCardsInHand(0);
        } catch (Exception e) {
            fail("showCardsInHand should not throw any exceptions.");
        }
    }

    @Test
    public void testShowCardsInHand2() {
        cardImplementor.loadCardsFromFile(createTempCardFile(), numOfPlayers);
        cardImplementor.distributeToPlayers(numOfPlayers);
        try {
            cardImplementor.showCardsInHand(7);
            cardImplementor.showCardsInHand(-4);
        } catch (Exception e) {
            fail("should handle negative deck IDs.");
        }
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
