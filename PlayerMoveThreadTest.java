

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

public class PlayerMoveThreadTest {

    private static PlayerMoveThread playerMoveThread;

    // Creating a player with respective decks assigned for testing
    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            Deck leftDeck = new Deck(10);
            Deck rightDeck = new Deck(11);
            playerMoveThread = new PlayerMoveThread(leftDeck, rightDeck);
        } catch (IOException e) {
            fail("IOException occurred while setting up test: " + e.getMessage());
        }
    }

    // Clearing all cards in decks and hand of player before each test case
    @Before
    public void resetPlayerState() {
        Deck leftDeck = playerMoveThread.getLeftDeck();
        Deck rightDeck = playerMoveThread.getRightDeck();
        leftDeck.clearDeck();
        rightDeck.clearDeck();
        playerMoveThread.clearHand();
    }

    // After finishing all test cases deletes the txt file created for the player created
    @AfterClass
    public static void deleteFiles() {
        playerMoveThread.deletePlayerFile("player1_output.txt");
        playerMoveThread.deletePlayerFile("player5_output.txt");
        playerMoveThread.deletePlayerFile("player10_output.txt");
        Deck leftDeck = playerMoveThread.getLeftDeck();
        leftDeck.deleteDeckFile("deck11_output.txt");
        Deck rightDeck = playerMoveThread.getRightDeck();
        rightDeck.deleteDeckFile("deck12_output.txt");
    }

    //Test for doBoth method
    @Test
    public void testDoBoth() throws IOException {
        Deck leftDeck = playerMoveThread.getLeftDeck();
        Deck rightDeck = playerMoveThread.getRightDeck();
        Card card1 = new Card(9);
        leftDeck.addCard(card1);
        Card card2 = new Card(11);
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.doBoth(1);

        // Assert that a card was drawn from the left deck
        assertEquals(0, leftDeck.getSize());

        // Assert that a card was discarded to the right deck
        assertEquals(1, rightDeck.getSize());

        // Assert that the output file contains the expected log
        File outputFile = new File("player1_output.txt");
        assertTrue(outputFile.exists());

        // Verify the content of the output file
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line1 = reader.readLine();
            String line2 = reader.readLine();

            assertNotNull(line1);
            assertTrue(line1.contains("draws a"));

            assertNotNull(line2);
            assertTrue(line2.contains("discards a"));
        }
    }

    //Checks if the card is drawn to the hand of the player
    @Test
    public void testDrawCard1() {
        Card card = new Card(1);
        Deck leftDeck = playerMoveThread.getLeftDeck();

        leftDeck.addCard(card);
        playerMoveThread.drawCard();

        // Ensure the card is added to the player's hand
        assertEquals(1, playerMoveThread.getCardValues()[0]);
    }

    //Checks if the cards are drawn to the hand of the player
    @Test
    public void testDrawCard2() {
        Card card = new Card(1);
        Card card2 = new Card(2);
        Deck leftDeck = playerMoveThread.getLeftDeck();

        leftDeck.addCard(card);
        leftDeck.addCard(card2);
        playerMoveThread.drawCard();
        playerMoveThread.drawCard();

        // Ensure the card is added to the player's hand
        assertNotEquals(2, playerMoveThread.getCardValues()[0]);
        assertEquals(2, playerMoveThread.getCardValues()[1]);
    }

    //Check if the discardCard properly discards the card from the hand to the rightDeck 
    @Test
    public void testDiscardCard1() {
        Card card1 = new Card(7);
        playerMoveThread.addCardToHand(card1);
        playerMoveThread.discardCard();
        Deck rightDeck = playerMoveThread.getRightDeck();

        assertEquals(1, rightDeck.getSize());
        assertEquals(0, playerMoveThread.getCardValues()[0]);
    }

    //Check if the discardCard properly discards the first card from the hand to the rightDeck 
    @Test
    public void testDiscardCard2() {
        Card card1 = new Card(7);
        Card card2 = new Card(8);

        playerMoveThread.addCardToHand(card1);
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.discardCard();

        assertEquals(8, playerMoveThread.getCardValues()[0]);
        assertNotEquals(7, playerMoveThread.getCardValues()[0]);
    }

    //Check if the discardCard properly discards the cards from the hand to the rightDeck 
    @Test
    public void testDiscardCard3() {
        Card card1 = new Card(7);
        Card card2 = new Card(8);
        Deck rightDeck = playerMoveThread.getRightDeck();

        playerMoveThread.addCardToHand(card1);
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.discardCard();
        playerMoveThread.discardCard();

        assertEquals(2, rightDeck.getSize());
        assertEquals(0, playerMoveThread.getCardValues()[0]);
    }

    //Checks if the cards are correctly added to the hand and if the values are also correct
    @Test
    public void testAddCardToHand() {
        Card card1 = new Card(2);
        Card card2 = new Card(2);
        Card card3 = new Card(3);
        Card card4 = new Card(4);
        playerMoveThread.addCardToHand(card1);
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.addCardToHand(card3);
        playerMoveThread.addCardToHand(card4);
        int[] handSlice = Arrays.copyOfRange(playerMoveThread.getCardValues(), 0, 3);

        assertArrayEquals(new int[]{2, 2, 3}, handSlice);
        assertFalse(Arrays.equals(new int[]{2, 2, 4}, handSlice));
    }

    //Check if the isWinningHand correctly returns True in case of a win
    @Test
    public void testWinningHand() {
        Card card1 = new Card(2);
        Card card2 = new Card(2);
        Card card3 = new Card(2);
        Card card4 = new Card(2);

        playerMoveThread.addCardToHand(card1);
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.addCardToHand(card3);
        playerMoveThread.addCardToHand(card4);

        assertTrue(playerMoveThread.isWinningHand());
    }

    //Check if the isWinningHand is still False in case of negative numbers
    @Test
    public void testNotWinningHand1() {
        Card card1 = new Card(2);
        Card card2 = new Card(2);
        Card card3 = new Card(-2);
        Card card4 = new Card(2);

        playerMoveThread.addCardToHand(card1);
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.addCardToHand(card3);
        playerMoveThread.addCardToHand(card4);

        assertFalse(playerMoveThread.isWinningHand());
    }

    //Check if the isWinningHand is kept False in case of a not winning hand
    @Test
    public void testNotWinningHand2() {
        Card card1 = new Card(2);
        Card card2 = new Card(1);
        Card card3 = new Card(2);
        Card card4 = new Card(3);

        playerMoveThread.addCardToHand(card1);
        playerMoveThread.addCardToHand(card2);
        playerMoveThread.addCardToHand(card3);
        playerMoveThread.addCardToHand(card4);

        assertFalse(playerMoveThread.isWinningHand());
    }
}
