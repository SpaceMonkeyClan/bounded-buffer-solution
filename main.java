import java.util.concurrent.Semaphore;
import java.util.Random;

public class main {
    static final int BUFFER_SIZE = 10; // default buffer size
    static final int ROUND = 20 ; //default run 20 round
    static int [] buffer = new int[BUFFER_SIZE];
    static int limit = ROUND; // after this time, program end
    static int next_in = 0 , next_out = 0;
    static Semaphore emptyBuffer = new Semaphore(buffer.length);//Semaphore constructor
    static Semaphore occupiedBuffer = new Semaphore(0);
    static int count = 0;
    static boolean done = false; // use to control last round
    static Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Runnable(){
            public void run(){
                try {
                    producer();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(new Runnable(){
            public void run(){
                try {
                    consumer();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    public static void producer() throws InterruptedException{
        while (!done){
            // generate a random number k1 between 1 and a half of buffer size
            // int k1 = random.nextInt(BUFFER_SIZE/3 + 1);
            // int k1 = (int) (Math.random() * buffer.length / 3) + 1;
            int k1 = random.nextInt(BUFFER_SIZE/2)+1;
            // for i from 0 to k1 -1
            for(int i = 0; i < k1-1; i++){
                if(emptyBuffer.availablePermits()>0){
                    // acquire one permit from emptyBuffer
                    emptyBuffer.acquire();
                    // set corresponding buffer value to be 1
                    buffer[i] = 1;
                    // release one permit to occupiedBuffer
                    occupiedBuffer.release();
                }else{
                    break;
                }
            }

            // update next_in
            // from below - next_out = (next_out + k2) % buffer.length;
            next_in = (next_in + k1) % buffer.length;
            // next_in++;
            // print out message
            System.out.println("Producer is OK thus far: " + next_in);
            // update limit
            limit--;
            // if limit <= 0 then
            if(limit <= 0){
                System.out.println("Producer exits system without any race problem.");
                done = true; // once producer done, force consumer done
            }
            // let thread sleep for a random time interval
            // notify
            Thread.sleep((int) (Math.random()*900 + 100));
        }
    }

    public static void consumer() throws InterruptedException {
        while (!done){
            // let consumer sleep for a random time interval
            // let thread sleep for a random time interval
            //notify();
            Thread.sleep((int) (Math.random()*900 + 100));
            // generate random number k2 between 1 and half or 1 3rd like v1 of buffer size
            // int k2 = random.nextInt(BUFFER_SIZE/3 + 1);
            // int k2 = (int) (Math.random() * buffer.length / 3) + 1;
            int k2 = random.nextInt(BUFFER_SIZE/2) + 1;
            int data;
            // for i from 0 to k2 - 1
            for(int i = 0; i < k2-1; i++){
                // acquire one permit from occupiedBuffer
                occupiedBuffer.acquire();
                // read one data from corresponding buffer location
                data=buffer[i];
                /* based on data value, print out error message or clear buffer However, shall have no error message when run the program */
                if(data != 1){
                    System.out.println("No value available in the buffer.");
                    occupiedBuffer.release();
                    // release one permit to occupiedBuffer
                }

            }
            //e++;
            // update next_out
            next_out = (next_out + k2) % buffer.length;
            // print out message
            System.out.println("Consumer is OK thus far: " + next_out);
            // update limit
            limit--;
            // if limit <= 0 then
            if(limit <= 0) {
                System.out.println("Consumer exits system without any race problem.");
                done = true; // once the consumer is done, force producer done
                // System.exit(0); // exit the program
            }
            //notify();
            Thread.sleep((int) (Math.random()*900 + 100));
        }
    }

}