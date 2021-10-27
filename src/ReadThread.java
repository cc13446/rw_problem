import java.util.Random;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadThread extends Thread {
    // 读者ID
    private final int id;
    // 读者数量
    private final AtomicInteger readCount;
    // 读者数量信号量
    private final Semaphore readCountSemaphore;
    // 写者信号量
    private final Semaphore writeSemaphore;

    private final AtomicBoolean close;

    public ReadThread(int id, AtomicInteger readCount, Semaphore readCountSemaphore, Semaphore writeSemaphore) {
        this.id = id;
        this.readCount = readCount;
        this.readCountSemaphore = readCountSemaphore;
        this.writeSemaphore = writeSemaphore;
        this.close = new AtomicBoolean(false);
    }

    public void run() {
        try {
            while (!close.get()) {
                // 等待着读
                readCountSemaphore.acquire();
                //如果第一个读者，那么要考虑是否有写者，没有写者，直接读，有写者，等待写者
                if (readCount.get() == 0) {
                    // 已经具备读的条件了，读者数量加1
                    writeSemaphore.acquire();
                }
                readCount.getAndIncrement();
                readCountSemaphore.release();

                System.out.println("读者" + id + "我正在读哦...");
                Thread.sleep((long) (new Random().nextFloat() * 4000));
                System.out.println("读者" + id + "读完了...");

                readCountSemaphore.acquire();
                //读完了，读者数量减少1
                readCount.getAndDecrement();
                //没有读者了，可以写了
                if (readCount.get() == 0) {
                    writeSemaphore.release();
                }
                //释放读者信号量
                readCountSemaphore.release();
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