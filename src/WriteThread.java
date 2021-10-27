import java.util.Random;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class WriteThread extends Thread {
    //编号
    private final int id;
    //写者信号量
    private final Semaphore writeSemaphore;

    private final AtomicBoolean close;

    public WriteThread(int id, Semaphore semaphore) {
        this.id = id;
        this.writeSemaphore = semaphore;
        this.close = new AtomicBoolean(false);
    }
    public void run() {
        try {
            while (!close.get()) {
                writeSemaphore.acquire();

                System.out.println("写者" + this.id + "正在写...");
                Thread.sleep((long) (new Random().nextFloat() * 4000));
                System.out.println("写者" + this.id + "写完了...");

                writeSemaphore.release();
                Thread.sleep((long) (new Random().nextFloat() * 1000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closeThread() {
        this.close.set(true);
    }
}
