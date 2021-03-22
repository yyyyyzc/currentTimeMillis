package test.clock;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2020-11-04
 *
 * @author yangzc
 **/
@BenchmarkMode(Mode.SampleTime)
@Warmup(iterations = 2)
@Measurement(iterations = 1, time = 5)
@Threads(8)
//@Fork(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SystemTime {

//    private static final int iterSize = 100000000;
    private static final int iterSize = 50000000;

    @Benchmark
    public long testSystemMilliSpeed() {
        long sum = 0;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < iterSize; i++) {
            sum += System.currentTimeMillis();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("[System.currentTimeMillis()] Sum = " + sum + "; time spent = " + (t2 - t1) +
                " ms; or " + (t2 - t1) * 1.0E6 / iterSize + " ns / iter");
        return sum;
    }

    @Benchmark
    public long testBufferClock() {
        long sum = 0;
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < iterSize; i++) {
            sum += SystemClock.now();
        }
        long t2 = System.currentTimeMillis();
        System.out.println("[BufferClock] Sum = " + sum + "; time spent = " + (t2 - t1) +
                "ms; or " + (t2 - t1) * 1.0E6 / iterSize + " ns / iter");
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
//        Options options = new OptionsBuilder()
//                .include(SystemTime.class.getSimpleName())
//                .output("C:\\logs\\benchmark_8_Threads.log")
//                .build();
//        new Runner(options).run();

        new SystemTime().testBufferClock();
        new SystemTime().testBufferClock();
        new SystemTime().testBufferClock();
    }

}