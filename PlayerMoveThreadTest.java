import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PlayerMoveThreadTest {

    private PlayerMoveThread playerMoveThread;
   
    @BeforeAll
    void setUp() {
        try {
        // Manually instantiate Deck objects and PlayerMoveThread
        Deck leftDeck = new Deck(10);
        Deck rightDeck = new Deck(11);
        playerMoveThread = new PlayerMoveThread(leftDeck, rightDeck);
        } catch (IOException e) {
            // Handle the exception (you could log it or fail the test)
            fail("IOException occurred while setting up test: " + e.getMessage());
        }
    }

    @BeforeEach
    void resetPlayerState() {
        // Reset the player's state (e.g., clear the hand)
        Deck leftDeck = playerMoveThread.getLeftDeck();
        Deck rightDeck = playerMoveThread.getRightDeck();
        leftDeck.clearDeck();  // Assuming `clearHand` method exists
        rightDeck.clearDeck();
        playerMoveThread.clearHand();
    }

    @AfterAll
    void deleteFiles() {
        playerMoveThread.deletePlayerFile("player1_output.txt");
        Deck leftDeck = playerMoveThread.getLeftDeck();
        leftDeck.deleteDeckFile("deck11_output.txt");
        Deck rightDeck = playerMoveThread.getRightDeck();
        rightDeck.deleteDeckFile("deck12_output.txt");

        
    }


    @Test
    void testDoBoth() throws IOException {
        //Deck leftDeck = new Deck(10);
        //Deck rightDeck = new Deck(11);
        //playerMoveThread = new PlayerMoveThread(leftDeck, rightDeck);
        Deck leftDeck = playerMoveThread.getLeftDeck();
        Deck rightDeck = playerMoveThread.getRightDeck();
        Card card1 = new Card(9);
        leftDeck.addCard(card1); // One card added to the left deck
        Card card2 = new Card(11);
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.doBoth(1);

        // Assert that a card was drawn from the left deck
        assertEquals(0, leftDeck.getSize(), "A card should be removed from the left deck");

        // Assert that a card was discarded to the right deck
        assertEquals(1, rightDeck.getSize(), "A card should be added to the right deck");

        // Assert that the output file contains the expected log
        File outputFile = new File("player1_output.txt");
        assertTrue(outputFile.exists(), "Output file should exist");

        // Verify the content of the output file
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();

            assertNotNull(line1, "First log entry should not be null");
            assertTrue(line1.contains("draws a"), "First log entry should contain draw action");

            assertNotNull(line2, "Second log entry should not be null");
            assertTrue(line2.contains("discards a"), "Second log entry should contain discard action");
        }
    }

    @Test
    void testDrawCard() {
        // Manually create a Card object to simulate drawing a card
        Card card = new Card(1);  // Card with id 1 and value 5
        Deck leftDeck = playerMoveThread.getLeftDeck();
        // Simulate the drawing of a card from leftDeck
        leftDeck.addCard(card);  // Add the card to the left deck
        playerMoveThread.drawCard();  // Assuming drawCard will move it to the player's hand

        // Ensure the card is added to the player's hand
        assertEquals(1, playerMoveThread.getCardValues()[0]);
    }

    

    @Test
    void testDiscardCard() {
        // Manually create a Card object
        Card card1 = new Card(7);
        playerMoveThread.addCardToHand(card1);  // Add cards to the hand
        // Card card2 = new Card(8);
        // playerMoveThread.addCardToHand(card2);
        
        Card card5 = new Card(2);  // Card with id 2 and value 3
        Deck rightDeck = playerMoveThread.getRightDeck();
        // Simulate the drawing of a card from leftDeck
        rightDeck.addCard(card5);   
        playerMoveThread.discardCard();
        assertEquals(0, playerMoveThread.getCardValues()[0]);  // Hand should be empty now
    }


    @Test
    void testaddCardtoHand() {
        Card card1 = new Card(2);
        Card card2 = new Card(2);
        Card card3 = new Card(3);
        Card card4 = new Card(4);
        playerMoveThread.addCardToHand(card1);  // Add cards to the hand
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.addCardToHand(card3);
        playerMoveThread.addCardToHand(card4);
        // Get the first three card values
        int[] handSlice = Arrays.copyOfRange(playerMoveThread.getCardValues(), 0, 3);

        // Assert the sliced array
        assertArrayEquals(new int[]{2, 2, 3}, handSlice, "The first three cards should be correct."); 
        assertFalse(Arrays.equals(new int[]{2, 2, 4}, handSlice), "The first three cards should not be correct"); 
    }

    @Test
    void testWinningHand() {
        // Manually create a winning hand (same value cards)
        Card card1 = new Card(2);
        Card card2 = new Card(2);
        Card card3 = new Card(2);
        Card card4 = new Card(2);
        playerMoveThread.addCardToHand(card1);  // Add cards to the hand
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.addCardToHand(card3);
        playerMoveThread.addCardToHand(card4);


        // Check if the player has a winning hand
        assertTrue(playerMoveThread.isWinningHand());
    }

    @Test
    void testNotWinningHand() {
        Card card1 = new Card(2);
        Card card2 = new Card(1);
        Card card3 = new Card(2);
        Card card4 = new Card(3);
        playerMoveThread.addCardToHand(card1);  // Add cards to the hand
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.addCardToHand(card3);
        playerMoveThread.addCardToHand(card4);

        // Check if the player does not have a winning hand
        assertFalse(playerMoveThread.isWinningHand());
    }




}
