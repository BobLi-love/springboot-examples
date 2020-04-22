package com.bob.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    private static Logger logger = LoggerFactory.getLogger(Test.class);
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        RingBufferWheel wheel = new RingBufferWheel(pool);
        for (int i = 0; i < 100; i++) {
            if (i == 5 || i == 10 || i == 35) {
                RingBufferWheel.Task task = new Job(i);
                task.setKey(i);
                wheel.addTask(task);
            }
        }
        wheel.start();
        logger.info("task size =={} ", wheel.taskSize());
        wheel.stop(false);

    }

    private static class Job extends RingBufferWheel.Task{
        private int number;

        public Job(int number) {
            this.number = number;
        }

        public void run() {
            logger.info("number=={}", number);
        }
    }
}
