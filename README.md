# RUNNING THE TEST SUITE

## Requirements

- **Java Development Kit (JDK)**: Ensure you have JDK installed on your system (JDK 23 or later is required).
- **JUnit 4 and Hamcrest libraries**: The required libraries are provided in the `lib/` folder.

# How to Run Test

### Step 1: Extract the Project

1. Extract the `cardsTest.zip` file into your desired working directory.
2. Open a terminal and navigate to the directory containing the extracted files.

`cd/to/Card-Game`

### Step 2: Run Test Suite
1. Run the TestSuite class using the JUnit runner. Use the following command based on your operating system:


Windows :
`java -cp "lib/*;." org.junit.runner.JUnitCore TestSuite`

macOS/Linux :
`java -cp "lib/*:." org.junit.runner.JUnitCore TestSuite`