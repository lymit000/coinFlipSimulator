import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class parallel extends RecursiveTask<int[]> {
    private static final ForkJoinPool POOL = new ForkJoinPool();
    int lo, hi, SEQUENTIAL_CUTOFF;
    int[] trialData;


    public parallel(int[] trialData, int lo, int hi, int SEQUENTIAL_CUTOFF) {
        this.trialData = trialData;
        this.lo = lo;
        this.hi = hi;
        this.SEQUENTIAL_CUTOFF = SEQUENTIAL_CUTOFF;
    }

    @Override
    protected int[] compute() {
        if (hi - lo <= SEQUENTIAL_CUTOFF) {
            return sequential(trialData, lo, hi);
        }

        int mid = lo + (hi - lo) / 2;

        parallel left = new parallel(trialData, lo, mid, SEQUENTIAL_CUTOFF);
        parallel right = new parallel(trialData, mid, hi, SEQUENTIAL_CUTOFF);

        left.fork();

        int[] rightResult = right.compute();
        int[] leftResult = left.join();

//        POOL.invoke(new MergeGridTask(leftResult, rightResult,  0, Math.max(rightResult[0].length, leftResult[0].length), 0, Math.max(rightResult.length, leftResult.length)));

        return leftResult;
    }

    private int[] sequential(int[] trialData, int lo, int hi) {
        Random rand = new Random();
        for (int i = lo; i < hi; i++) {
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
            trialData[i] = total;
        }
        return trialData;
    }


}
