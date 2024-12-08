
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class DeckTest {

    private Deck deck;

    // Make a new Deck for test cases - runs once before all tests
    @BeforeClass
    public static void setUpBeforeClass() {
        // No need for setup at class level in this case, but could be added here if needed
    }

    // Clear the deck before every test case
    @Before
    public void resetDeck() {
        int deckId = 1;
        deck = new Deck(deckId);
        deck.clearDeck();
    }

    // Delete files after all tests are finished
    @AfterClass
    public static void deleteFiles() {
        Deck deck = new Deck(1);  // Assuming static method, otherwise create deck object if needed
        deck.deleteDeckFile("deck2_output.txt");
    }

    @Test
    public void testDeckFileCreation() {
        int deckId = 1;
        File deckFile = new File("deck" + (deckId + 1) + "_output.txt");

        assertTrue("Deck file should be created.", deckFile.exists());
        assertTrue("Deck file should be deleted after test.", deckFile.delete());
    }

    @Test
    public void testAddCardAndSize1() {

        Card card1 = new Card(10);
        Card card2 = new Card(20);

        deck.addCard(card1);
        deck.addCard(card2);

        assertEquals("Deck size should be 2 after adding two cards.", 2, deck.getSize());
        assertNotEquals("Deck size should not be 3 after adding two cards.", 3, deck.getSize());
    }

    @Test
    public void testAddCardAndSize2() {

        Card card1 = new Card(10);
        Card card2 = new Card(20);
        Card card3 = new Card(30);
        Card card4 = new Card(40);

        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        deck.addCard(card4);

        assertEquals("Deck size should be 4 after adding four cards.", 4, deck.getSize());
        assertNotEquals("Deck size should not be 2 after adding four cards.", 2, deck.getSize());
    }

    @Test
    public void testDrawCard1() {

        Card card1 = new Card(10);
        Card card2 = new Card(20);

        deck.addCard(card1);
        deck.addCard(card2);

        Card drawnCard = deck.drawCard();
        assertNotNull("Drawn card should not be null.", drawnCard);
        assertEquals("The value of the drawn card should match the first added card.", 10, drawnCard.getValue());
        assertEquals("Deck size should be 1 after drawing a card.", 1, deck.getSize());
    }

    @Test
    public void testDrawCard2() {

        Card card1 = new Card(10);
        Card card2 = new Card(20);
        Card card3 = new Card(30);
        Card card4 = new Card(40);

        deck.addCard(card1);
        deck.addCard(card2);
        deck.addCard(card3);
        deck.addCard(card4);

        deck.drawCard();
        Card drawnCard2 = deck.drawCard();
        assertNotNull("Drawn card should not be null.", drawnCard2);
        assertEquals("The value of the drawn card should match the second added card.", 20, drawnCard2.getValue());
        assertEquals("Deck size should be 2 after drawing a card.", 2, deck.getSize());
    }

    @Test
    public void testWriteAllCardsToFile() throws IOException {

        int deckId = 1;
        Card card1 = new Card(10);
        Card card2 = new Card(20);

        deck.addCard(card1);
        deck.addCard(card2);

        deck.writeAllCardsToFile();

        File deckFile = new File("deck" + (deckId + 1) + "_output.txt");
        assertTrue("Deck file should exist after writing.", deckFile.exists());

        // Read the file and verify its content
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(deckFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        assertFalse("Deck file should not be empty.", lines.isEmpty());
        assertTrue("Deck file should contain the values of the added cards.", lines.get(0).contains("10") && lines.get(0).contains("20"));
    }

    @Test
    public void testShowCards() {

        // Reset card counter to ensure IDs start from 0
        Card.resetCardCounter();

        Card card1 = new Card(10);
        Card card2 = new Card(20);

        deck.addCard(card1);
        deck.addCard(card2);

        // Redirect output to capture printed cards
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            deck.showCards();

            String output = outContent.toString().trim().replace("\r\n", "\n");
            String expectedOutput = "card id: 0 card value: 10\ncard id: 1 card value: 20";
            assertEquals("The output of showCards() does not match the expected output.", expectedOutput, output);
        } finally {
            System.setOut(originalOut);
        }
}

}
