import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class DeckTest {


    @BeforeEach
    void setUp() {
        Card.resetCardCounter();
    }

    @Test
    public void testDeckFileCreation() {
        int deckId = 1;
        Deck deck = new Deck(deckId);
        File deckFile = new File("deck"+(deckId+1) + "_output.txt");

        assertTrue(deckFile.exists(), "Deck file should be created.");
        // Clean up the created file after test
        assertTrue(deckFile.delete(), "Deck file should be deleted after test.");
    }

    @Test
    public void testAddCardAndSize() {
        Deck deck = new Deck(2);
        Card card1 = new Card(10);
        Card card2 = new Card(20);

        deck.addCard(card1);
        deck.addCard(card2);

        assertEquals(2, deck.getSize(), "Deck size should be 2 after adding two cards.");
        assertNotEquals(3, deck.getSize(), "Deck size should be 2 after adding two cards.");
    }

    @Test
    public void testDrawCard() {
        Deck deck = new Deck(3);
        Card card1 = new Card(10);
        Card card2 = new Card(20);

        deck.addCard(card1);
        deck.addCard(card2);

        Card drawnCard = deck.drawCard();
        assertNotNull(drawnCard, "Drawn card should not be null.");
        assertEquals(10, drawnCard.getValue(), "The value of the drawn card should match the first added card.");
        assertEquals(1, deck.getSize(), "Deck size should be 1 after drawing a card.");
    }

    @Test
    public void testWriteAllCardsToFile() throws IOException {
        int deckId = 4;
        Deck deck = new Deck(deckId);
        Card card1 = new Card(10);
        Card card2 = new Card(20);

        deck.addCard(card1);
        deck.addCard(card2);

        deck.writeAllCardsToFile();

        File deckFile = new File("deck"+(deckId+1) + "_output.txt");
        assertTrue(deckFile.exists(), "Deck file should exist after writing.");

        // Read the file and verify its content
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(deckFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        assertFalse(lines.isEmpty(), "Deck file should not be empty.");
        assertTrue(lines.get(0).contains("10") && lines.get(0).contains("20"),
                "Deck file should contain the values of the added cards.");

        // Clean up the created file after test
        assertTrue(deckFile.delete(), "Deck file should be deleted after test.");
    }

    @Test
    public void testShowCards() {
        // Arrange
        Deck deck = new Deck(87);
        Card card1 = new Card(10);
        Card card2 = new Card(20);

        deck.addCard(card1);
        deck.addCard(card2);

        // Redirect output to capture printed cards
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            // Act
            deck.showCards();

            // Assert
            String output = outContent.toString().trim().replace("\r\n", "\n");
            String expectedOutput = """
                card id: 0 card value: 10
                card id: 1 card value: 20
                """.trim().replace("\r\n", "\n");
            assertEquals(expectedOutput, output, "The output of showCards() does not match the expected output.");
        } finally {
            // Restore the original system output
            System.setOut(originalOut);
        }
    }
}
