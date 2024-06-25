package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainStream {

    private static final int[] VALUES = {1, 9, 2, 3, 8, 8, 3, 2};

    public static void main(String[] args) {
        System.out.println("values: " + Arrays.toString(VALUES) + "\n" + "minValue: " + minValue(VALUES));
        List<Integer> integers = Arrays.stream(VALUES).boxed().toList();
        System.out.println("sum: " + sum(integers) + "\n" + "oddOrEven: " + oddOrEven(integers));
    }

    private static int minValue(int[] values) {
        AtomicInteger count = new AtomicInteger();
        return Arrays.stream(values)
                .distinct()
                .peek(x -> count.getAndIncrement())
                .sorted()
                .reduce(0, (a, b) -> a + b * (int) Math.pow(10, count.getAndDecrement() - 1));
    }

    private static int sum(List<Integer> integers) {
        return integers.stream().reduce(0, Integer::sum);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        AtomicInteger sum = new AtomicInteger();
        return integers.stream()
                .peek(sum::addAndGet)
                .collect(Collectors.partitioningBy(x -> x % 2 == 0))
                .get(sum.get() % 2 != 0);
    }
}
