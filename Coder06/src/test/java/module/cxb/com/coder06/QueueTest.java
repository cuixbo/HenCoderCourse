package module.cxb.com.coder06;

import org.junit.Test;

import java.util.PriorityQueue;

public class QueueTest {
    private int queueSize = 10;
    private PriorityQueue<Integer> queue = new PriorityQueue<Integer>(queueSize);

    @Test
    public void main() {
        QueueTest test = new QueueTest();
        Producer producer = test.new Producer();
        Consumer consumer = test.new Consumer();
        Timmer timmer = test.new Timmer();
//        producer.start();
//        consumer.start();
        timmer.start();

        try {
            tet();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    int tet() throws NullPointerException{
        int y=4;
        System.out.println("start");
        try {
            y++;
            String a=null;
            int x=a.length();
            y++;
        }catch (NullPointerException e){
            System.out.println("NullPointerException");
            throw e;
        }
        y=y*5;
        System.out.println("next");
        return y;
    }

    public class Consumer extends Thread {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == 0) {
                        try {
                            System.out.println("队列空，等待数据");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.poll();          //每次移走队首元素
                    queue.notify();
                    System.out.println("从队列取走一个元素，队列剩余" + queue.size() + "个元素");
                }
            }
        }
    }

    public class Timmer extends Thread {

        @Override
        public void run() {
            consume();
        }

        private void consume() {
            try {
               Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
    }

    public class Producer extends Thread {

        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                synchronized (queue) {
                    while (queue.size() == queueSize) {
                        try {
                            System.out.println("队列满，等待有空余空间");
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            queue.notify();
                        }
                    }
                    queue.offer(1);        //每次插入一个元素
                    queue.notify();
                    System.out.println("向队列取中插入一个元素，队列剩余空间：" + (queueSize - queue.size()));
                }
            }
        }
    }
}