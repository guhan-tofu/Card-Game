//import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
//import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
    CardTest.class,
    DeckTest.class,
    PlayerMoveThreadTest.class,
    CardImplTest.class})



public class TestSuite {
    
}