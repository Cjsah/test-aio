package net.cjsah.main;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        AtomicBoolean stop = new AtomicBoolean(false);
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                log.info("count: {}", i + 1);
                synchronized (lock) {
                    try {
                        lock.wait(1000);
                        if (stop.get()) return;
                    } catch (InterruptedException e) {
                        log.error("Interrupted Error", e);
                    }
                }
            }
            // ..... GameStart()
        });

        thread.start();
        TimeUnit.SECONDS.sleep(3);
        log.info("stop");
        stop.set(true);
        synchronized (lock) {
            lock.notifyAll();
        }
    }



}
