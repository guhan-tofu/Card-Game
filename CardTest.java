


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardTest {

    @BeforeEach
    void resetIdCounter() {
        // Reset the static idCounter to 0 before each test to ensure consistent behavior.
        try {
            var field = Card.class.getDeclaredField("idCounter");
            field.setAccessible(true);
            field.setInt(null, 0);
        } catch (Exception e) {
            fail("Failed to reset idCounter: " + e.getMessage());
        }
    }

    @Test
    void testConstructor() {
        Card card = new Card(10);
        assertEquals(10, card.getValue(), "Card value should match the value provided to the constructor.");
        assertEquals(0, card.getId(), "Card ID should start at 0 for the first instance.");
    }

    @Test
    void testGetValue() {
        Card card = new Card(5);
        assertEquals(5, card.getValue(), "getValue() should return the correct card value.");
    }

    @Test
    void testDontGetValue() {
        Card card = new Card(5);
        assertNotEquals(6, card.getValue(), "getValue() should not return the correct card value.");
    }

    @Test
    void testGetId() {
        Card card1 = new Card(3);
        Card card2 = new Card(7);
        assertEquals(0, card1.getId(), "First card ID should be 0.");
        assertEquals(1, card2.getId(), "Second card ID should be 1.");
    }

    @Test
    void testToString() {
        Card card = new Card(42);
        assertEquals("42", card.toString(), "toString() should return the string representation of the card value.");
    }

    @Test
    void testUniqueIds() {
        Card card1 = new Card(10);
        Card card2 = new Card(20);
        Card card3 = new Card(30);

        assertNotEquals(card1.getId(), card2.getId(), "Each card should have a unique ID.");
        assertNotEquals(card2.getId(), card3.getId(), "Each card should have a unique ID.");
        assertNotEquals(card1.getId(), card3.getId(), "Each card should have a unique ID.");
    }
}
