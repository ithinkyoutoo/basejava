package ru.javawebinar.basejava;

public class MainDeadLock {

    private static final String LOCK1 = "LOCK1";
    private static final String LOCK2 = "LOCK2";

    public static void main(String[] args) {
        method(LOCK1, LOCK2, 1000);
        method(LOCK2, LOCK1, 1000);
    }

    private static void method(String firstLock, String secondLock, long millis) {
        new Thread(() -> {
            System.out.println(getThreadName() + " start");
            synchronized (firstLock) {
                System.out.println(getThreadName() + " get " + firstLock + " in method");
                sleep(millis);
                synchronized (secondLock) {
                    System.out.println(getThreadName() + " get " + secondLock + " in method");
                }
            }
        }).start();
    }

    private static String getThreadName() {
        return Thread.currentThread().getName();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
