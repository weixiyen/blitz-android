package com.blitz.app.object_models;

import junit.framework.TestCase;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by spiff on 9/5/14.
 */
public class ObjectModelSyncTest{

    public static void testSync() {

        Runnable tick = new Runnable() {
            @Override
            public void run() {
                for(int i=0; i < 5; i++) {
                    System.out.println("tick");
                    try {
                        Thread.sleep(1000L);
                    } catch(InterruptedException _) {
                        // ignore
                    }
                }

            }
        };

        Runnable tock = new Runnable() {
            @Override
            public void run() {
                for(int i=0; i < 5; i++) {
                    System.out.println("tock");
                    try {
                        Thread.sleep(900L);
                    } catch(InterruptedException _) {
                        // ignore
                    }
                }

            }
        };
        ThreadPoolExecutor exec = new ThreadPoolExecutor(2, 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue());
        exec.execute(tick);
        exec.execute(tock);
        exec.shutdown();
    }

    public static void main(String[] args) {
        testSync();
    }
}
