

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//Run all the test classes at the same time
@RunWith(Suite.class)
@Suite.SuiteClasses({
    CardTest.class,
    DeckTest.class,
    PlayerMoveThreadTest.class,
    CardImplTest.class})



public class TestSuite {
    
}