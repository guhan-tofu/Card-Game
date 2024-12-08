

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardTest {

    // Reset the idCounter to 0 before each test to ensure consistent behavior.
    @Before
    public void resetIdCounter() {
        try {
            var field = Card.class.getDeclaredField("idCounter");
            field.setAccessible(true);
            field.setInt(null, 0);
        } catch (Exception e) {
            fail("Failed to reset idCounter: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor() {
        Card card = new Card(10);
        assertEquals("Card value should match the value provided to the constructor.", 10, card.getValue());
        assertEquals("Card ID should start at 0 for the first instance.", 0, card.getId());
    }

    @Test
    public void testGetValue() {
        Card card = new Card(5);
        assertEquals("getValue() should return the correct card value.", 5, card.getValue());
    }

    @Test
    public void testDontGetValue() {
        Card card = new Card(5);
        assertNotEquals("getValue() should not return the incorrect card value.", 6, card.getValue());
    }

    @Test
    public void testGetId() {
        Card card1 = new Card(3);
        Card card2 = new Card(7);
        assertEquals("First card ID should be 0.", 0, card1.getId());
        assertEquals("Second card ID should be 1.", 1, card2.getId());
    }

    @Test
    public void testToString() {
        Card card = new Card(42);
        assertEquals("toString() should return the string representation of the card value.", "42", card.toString());
    }

    @Test
    public void testUniqueIds() {
        Card card1 = new Card(10);
        Card card2 = new Card(20);
        Card card3 = new Card(30);

        assertNotEquals("Each card should have a unique ID.", card1.getId(), card2.getId());
        assertNotEquals("Each card should have a unique ID.", card2.getId(), card3.getId());
        assertNotEquals("Each card should have a unique ID.", card1.getId(), card3.getId());
    }
}
