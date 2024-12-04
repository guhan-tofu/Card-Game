import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public abstract class BasicThread extends Thread {

    private BufferedWriter out;

    BasicThread(String filename) throws IOException{
        this.out = new BufferedWriter( new FileWriter( filename ) );
    }
    

    // public void run() {
    //     for(;;) { // infinite loop
    //         //System.out.println("Thread "+ Thread.currentThread().getName() + " is waiting");
    //         try { 
    //             Thread.sleep(1000); // sleeps every 1 second each iteration
    //         } catch(InterruptedException e) {}
    //     }
    // }
    

}
