package ru.javawebinar.basejava;

public class MainDeadLock {

    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println(getThreadName() + " start");
            method(LOCK1, LOCK2, 1000);
        }).start();

        new Thread(() -> {
            System.out.println(getThreadName() + " start");
            method(LOCK2, LOCK1, 1000);
        }).start();
    }

    private static String getThreadName() {
        return Thread.currentThread().getName();
    }

    private static void method(Object firstLock, Object secondLock, long millis) {
        synchronized (firstLock) {
            System.out.println(getThreadName() + " get " + getName(firstLock) + " in method");
            sleep(millis);
            synchronized (secondLock) {
                System.out.println(getThreadName() + " get " + getName(secondLock) + " in method");
            }
        }
    }

    private static String getName(Object lock) {
        return LOCK1.equals(lock) ? "LOCK1" : "LOCK2";
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
