package com.pjj.xsp.present;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

abstract public class DealWithCodeTask<T> implements Runnable {
    private BlockingQueue<T> queue;
    private Object object = new Object();
    private boolean loopTag = true;

    public DealWithCodeTask(BlockingQueue<T> blockingQueue) {
        if (null == blockingQueue) {
            blockingQueue = new LinkedBlockingQueue<>();
        }
        queue = blockingQueue;
    }

    public void addQueue(T t) {
        try {
            queue.put(t);
            removeSynchronized();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void removeSynchronized() {
        synchronized (object) {
            object.notify();
        }
    }

    private T takeQueue() {
        T take = null;
        try {
            take = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return take;
    }

    @Override
    public void run() {
        while (loopTag) {
            if (queue.isEmpty()) {
                addSynchronized();
            } else {
                T t = takeQueue();
                if (null != t) {
                    dealWithTask(t);
                }
            }
        }
    }

    private void addSynchronized() {
        synchronized (object) {
            try {
                object.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void dealWithTask(T t);

}
