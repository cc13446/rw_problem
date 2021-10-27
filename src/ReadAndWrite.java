import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadAndWrite {
    //读者的数量
    public static final int readCount = 3;
    //写者的数量
    public static final int writeCount = 2;

    public static void main(String[] args) throws InterruptedException {

        Semaphore readCountSemaphore = new Semaphore(1);
        Semaphore writeSemaphore = new Semaphore(1);

        List<ReadThread> readThreads = new LinkedList<>();
        List<WriteThread> writeThreads = new LinkedList<>();

        AtomicInteger read = new AtomicInteger(0);
        for (int i = 0; i < readCount; i++) {//生成读者
            readThreads.add(new ReadThread(i, read, readCountSemaphore, writeSemaphore));
        }
        for (int i = 0; i < writeCount; i++) {//生成读者
            writeThreads.add(new WriteThread(i, writeSemaphore));
        }
        readThreads.forEach(ReadThread::start);
        writeThreads.forEach(WriteThread::start);

        Thread.sleep(200000);

        readThreads.forEach(ReadThread::closeThread);
        writeThreads.forEach(WriteThread::closeThread);
    }
}