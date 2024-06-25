package ru.javawebinar.basejava;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainConcurrent {

    public static final int THREADS_NUMBER = 10000;
    //    private int counter;
    private final AtomicInteger atomicCounter = new AtomicInteger();
//    private static final Lock lock = new ReentrantLock();
//    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
//    private static final Lock WRITE_LOCK = reentrantReadWriteLock.writeLock();
//    private static final Lock READ_LOCK = reentrantReadWriteLock.readLock();
//    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
//        @Override
//        protected SimpleDateFormat initialValue() {
//            return new SimpleDateFormat();
//        }
//    };

    public static void main(String[] args) throws InterruptedException {
        final MainConcurrent mainConcurrent = new MainConcurrent();
        CountDownLatch latch = new CountDownLatch(THREADS_NUMBER);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//        CompletionService completionService = new ExecutorCompletionService(executorService);

        for (int i = 0; i < THREADS_NUMBER; i++) {
//            Future<Integer> future = executorService.submit(() -> {
            executorService.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    mainConcurrent.inc();
//                    System.out.println(DATE_FORMAT.get().format(new Date()));
                }
                latch.countDown();
//                return 5;
            });
        }
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
//        System.out.println(mainConcurrent.counter);
        System.out.println(mainConcurrent.atomicCounter.get());
    }

    private void inc() {
//    private synchronized void inc() {
//        WRITE_LOCK.lock();
//        try {
        atomicCounter.incrementAndGet();
//        counter++;
//        } finally {
//            WRITE_LOCK.unlock();
//        }
    }
}
