import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerMoveThreadTest {

    private PlayerMoveThread playerMoveThread;

    @BeforeEach
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
        Card card = new Card(1);  // Card with id 1 and value 5
        Deck leftDeck = playerMoveThread.getLeftDeck();
        // Simulate the drawing of a card from leftDeck
        leftDeck.addCard(card);  // Add the card to the left deck
        playerMoveThread.drawCard();  
        Card card2 = new Card(2);  // Card with id 2 and value 3
        Deck rightDeck = playerMoveThread.getRightDeck();
        // Simulate the drawing of a card from leftDeck
        rightDeck.addCard(card2);  // Add the card to the left deck
        playerMoveThread.discardCard();

        // Ensure the card was discarded correctly
        //assertTrue(rightDeck.contains(card));  // Check if rightDeck contains the card
        assertEquals(0, playerMoveThread.getCardValues()[0]);  // Hand should be empty now
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


    @Test
    void testAreLastFiveMovesIdentical() {
        // Add the same move five times
        int[] move = {1, 2, 3};  // Example move
        for (int i = 0; i < 5; i++) {
            playerMoveThread.updateMoveHistory(move);
        }

        // Ensure the last five moves are identical
        assertTrue(playerMoveThread.areLastFiveMovesIdentical());
    }

    @Test
    void testAreLastFiveMovesNotIdentical() {
        // Add different moves
        int[] move1 = {1, 2, 3};
        int[] move2 = {4, 5, 6};
        playerMoveThread.updateMoveHistory(move1);
        playerMoveThread.updateMoveHistory(move2);

        // Ensure the last five moves are not identical
        assertFalse(playerMoveThread.areLastFiveMovesIdentical());
    }

}
