import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
    CardImplTest.class,
    CardTest.class, 
    DeckTest.class,
    PlayerMoveThread.class
})
public class TestSuite {
}