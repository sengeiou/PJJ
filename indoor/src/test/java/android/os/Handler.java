package android.os;

public class Handler {
    public Handler() {
    }

    public Handler(Looper looper) {

    }

    public void post(Runnable runnable) {
        runnable.run();
    }

    public void postDelayed(Runnable runnable, int deleay) {
        try {
            Thread.sleep(deleay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runnable.run();
    }
}
