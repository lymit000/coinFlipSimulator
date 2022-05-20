import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class main {

    private static final Random rand = new Random();
    private static final ForkJoinPool POOL = new ForkJoinPool();

    public static void main(String[] args){
        int totalTrials = 100;
        BigInteger average = new BigInteger(String.valueOf(0));
        for (int i = 0; i < totalTrials; i++){
            int[] trialData = new int[10000];
            POOL.invoke(new parallel(trialData, 0, trialData.length, 10));
            BigInteger singleAverage = findAverage(trialData);
//            System.out.println("The average is " + singleAverage);
            average = average.add(singleAverage);
        }
        System.out.println("Average of Averages is " + average.divide(BigInteger.valueOf(totalTrials)));

//        compareSeqVSParallel();
    }



    public static void compareSeqVSParallel() {
        final int TOTAL = 1000;

        long simpleTimer = 0;
        long complexTimer = 0;


        int[] trialData = new int[TOTAL];
        int[] seqData = new int[TOTAL];

        int numberOfTrials = 100;
        for (int i = 0; i <= numberOfTrials; i++) {
            long sequentialStart = System.nanoTime();
            runGame(TOTAL);
            long simpleEnd = System.nanoTime();
            simpleTimer += (simpleEnd - sequentialStart);


            if (100 <= i) {
                simpleTimer += (simpleEnd - sequentialStart);
            }


            long complexStart = System.nanoTime();
            POOL.invoke(new parallel(trialData, 0, trialData.length, 10));
            long complexEnd = System.nanoTime();
            complexTimer += (complexEnd - complexStart);

            if (100 <= i) {
                complexTimer += (complexEnd - complexStart);
            }
        }

        if (simpleTimer > complexTimer) {
            System.out.println("complex is faster by " + (simpleTimer - complexTimer) / 1000000.00);
        } else {
            System.out.println("sequential is faster by " + (complexTimer - simpleTimer) / 1000000.00);
        }
        System.out.println("sequential is " + simpleTimer / 1000000.00);
        System.out.println("complex is " + complexTimer / 1000000.00);
    }

    public static int coinFlipper() {
        int counter = 0;
        int total = 1;
        while (counter != 10) {
            if (rand.nextBoolean()) {
                counter++;
            } else {
                counter = 0;
                total++;
            }
        }
        return total;
    }

    public static int[] runGame(int totalTrials) {
        int[] trials = new int[totalTrials];

        for (int i = 0; i < totalTrials; i++) {
            trials[i] = coinFlipper();
        }
        return trials;
    }

    public static BigInteger findAverage(int[] trials) {
        BigInteger average = new BigInteger(String.valueOf(0));
        for (int i = 0; i < trials.length; i++) {
            BigInteger test = new BigInteger(String.valueOf(trials[i]));
            average = average.add(test);
        }
        average = average.divide(BigInteger.valueOf(trials.length));
        return average;
    }
}
