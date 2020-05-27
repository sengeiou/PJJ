package com.pjj.xsp.module;

import com.pjj.xsp.utils.Log;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ProductionConsumptionModule<T> implements Runnable {
    private ConcurrentLinkedQueue<T> linkedQueue = new ConcurrentLinkedQueue<>();
    private TaskRunnable taskRunnable;
    private boolean loopTag = true;
    private boolean waiteTag;
    private final Object object = new Object();

    public ProductionConsumptionModule() {
        taskRunnable = new TaskRunnable();
    }

    public void addQueue(T t) {
        linkedQueue.add(t);
        if (!waiteTag) {
            waiteTag = true;
            notifyThread();
        }
    }

    private T getLastNode() {
        return linkedQueue.poll();
    }

    public void recycle() {
        loopTag = false;
        linkedQueue.clear();
        synchronized (object) {
            object.notify();
        }
    }

    public void startSelf() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (loopTag) {
            if (linkedQueue.isEmpty()) {
                synchronized (object) {
                    try {
                        Log.e("TAG", "run: wait");
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                T lastNode = getLastNode();
                taskRunnable.setT(lastNode);
                if (null != onProductuinConsumptionListener) {
                    onProductuinConsumptionListener.startRun();
                }
                Log.e("TAG", "run: thread run1 " + loopTag + ", waiteTag=" + waiteTag);
                synchronized (object) {
                    try {
                        waiteTag = true;
                        Log.e("TAG", "run: thread run2 " + loopTag + ", waiteTag=" + waiteTag);
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        waiteTag = false;
                    }
                }
            }
            Log.e("TAG", "run: thread run3=" + loopTag + ", waiteTag=" + waiteTag);
        }
    }

    private OnProductuinConsumptionListener<T> onProductuinConsumptionListener;

    public void setOnProductuinConsumptionListener(OnProductuinConsumptionListener<T> onProductuinConsumptionListener) {
        this.onProductuinConsumptionListener = onProductuinConsumptionListener;
    }

    public void notifyThread() {
        if (waiteTag) {
            waiteTag = false;
            synchronized (object) {
                object.notify();
            }
        }
    }

    /***
     * 用于切换线程
     * @return
     */
    public Runnable changePerform() {
        return taskRunnable;
    }

    public interface OnProductuinConsumptionListener<T> {
        void performTask(T t);

        void startRun();
    }

    private class TaskRunnable implements Runnable {
        private T t;

        public TaskRunnable() {
        }

        public void setT(T t) {
            this.t = t;
        }

        @Override
        public void run() {
            if (null != onProductuinConsumptionListener) {
                onProductuinConsumptionListener.performTask(t);
            }
        }
    }
}
