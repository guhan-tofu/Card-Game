import java.util.ArrayList;
import java.util.Arrays;

public class ThreadThing extends Thread{

    public static volatile ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5,6,7,8,9,9,2,4,6,6));
    @Override
    public void run(){
        
        while (!numbers.isEmpty()) { // Continue until the list is empty
            synchronized (numbers) { // Ensure thread-safe removal
                if (!numbers.isEmpty()) {
                    // Remove the first element and print the updated list
                    int removed = numbers.remove(0);
                    System.out.println("Removed: " + removed);
                    System.out.println("Remaining: " + numbers);
                }
            }

            try {
                // Pause for 1 second between removals
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Handle interruption
                System.out.println("Thread interrupted");
            }
        }

        System.out.println("All elements removed.");
    }
}