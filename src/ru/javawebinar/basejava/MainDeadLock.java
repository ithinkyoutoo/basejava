package ru.javawebinar.basejava;

public class MainDeadLock {

    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println(getName() + " start");
            method1();
        }).start();

        new Thread(() -> {
            System.out.println(getName() + " start");
            method2();
        }).start();
    }

    private static void method1() {
        synchronized (LOCK1) {
            System.out.println(getName() + " get LOCK1 in method1");
            sleep(1000);
            synchronized (LOCK2) {
                System.out.println(getName() + " get LOCK2 in method1");
            }
        }
    }

    private static void method2() {
        synchronized (LOCK2) {
            System.out.println(getName() + " get LOCK2 in method2");
            sleep(1000);
            synchronized (LOCK1) {
                System.out.println(getName() + " get LOCK1 in method2");
            }
        }
    }

    private static String getName() {
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
