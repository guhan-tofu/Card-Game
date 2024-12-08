import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class RunTestSuite {
    public static void main(String[] args) {
        System.out.println("Running TestSuite...");
        Result result = JUnitCore.runClasses(TestSuite.class);

        // Display test results
        System.out.println("Total Tests Run: " + result.getRunCount());
        System.out.println("Total Failures: " + result.getFailureCount());

        // Print details of each failure
        for (Failure failure : result.getFailures()) {
            System.out.println("Test failed: " + failure.toString());
        }

        // Indicate if all tests passed
        if (result.wasSuccessful()) {
            System.out.println("All tests passed successfully!");
        } else {
            System.out.println("Some tests failed.");
        }
    }
}
