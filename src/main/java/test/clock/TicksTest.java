package test.clock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2021-03-22
 *
 * @author yangzc
 **/
public class TicksTest {

    public static volatile boolean flag = true;

    public static void main(String[] args) {
        TicksTest.step();
    }

    public static void step() {
        // 5亿
        long[] values = new long[500000000];
        // 实现定时500ms
        new Thread(() -> {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TicksTest.flag = false;
        }).start();

        int length = 0;
        long star = System.currentTimeMillis();
        for (int i = 0; flag && i < values.length; i++) {
            values[i] = System.currentTimeMillis();
//            values[i] = SystemClock.now();
            length = i;
        }
        long end = System.currentTimeMillis();

        // 统计结果
        int min = 0;
        int max = 0;
        long total = 0;
        int minCount = 0;
        int maxCount = 0;
        long totalCount = 0;
        int last = 0;
        int skip = 0;
        List<Integer> ticksList = new ArrayList<>(600);
        List<Integer> ticksCountList = new ArrayList<>(600);
        for (int i = 1; i < length; i++) {
            if (values[i] != values[i - 1]) {
                int step = (int) (values[i] - values[i - 1]);
                int stepCount = i - last;
                last = i;
                System.out.printf("step %09d %d %d%n", i, step, stepCount);

                // 跳过第一条
                if (skip++ > 0) {
                    total += step;
                    totalCount += stepCount;
                    ticksList.add(step);
                    ticksCountList.add(stepCount);
                    if (max < step)
                        max = step;
                    if (min == 0 || min > step)
                        min = step;
                    if (maxCount < stepCount)
                        maxCount = stepCount;
                    if (minCount == 0 || minCount > stepCount)
                        minCount = stepCount;
                }
            }
        }

        System.out.printf("time: %d, count: %d, " +
                "ticks: (avg: %d, min: %d, max: %d) " +
                "ticksCount: (avg: %d, min: %d, max: %d)",
                end - star, ticksList.size(),
                total / ticksList.size(), min, max,
                totalCount / ticksCountList.size(), minCount, maxCount);
    }

}