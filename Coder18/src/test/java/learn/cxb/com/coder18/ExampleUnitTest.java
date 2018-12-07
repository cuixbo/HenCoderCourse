package learn.cxb.com.coder18;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static int a = 0;
    /**
     * 这是一个标志位
     */
    private static boolean flag = false;
    private  Object obj = new Object();

    @Test
    public void addition_isCorrect() {


        BThread threadB = new BThread();
        threadB.start();

        AThread thread = new AThread();
        thread.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread.interrupt();//温和中断，只是改变了状态
        Thread.interrupted();//返回中断状态，并重置中断的状态。
        thread.isInterrupted();//只是返回中断状态，并不重置状态

//        System.out.println(thread.isInterrupted());//中断和重置状态为未中断
//        System.out.println(thread.isInterrupted());
//        System.out.println(thread.isInterrupted());
//        System.out.println(Thread.interrupted());


        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public class AThread extends Thread {
        @Override
        public void run() {

            for (int i = 0; i < 1000; i++) {
//                if (Thread.interrupted()) {
//                    return;
//                }

                System.out.println(Thread.interrupted());//返回中断状态，并改变中断状态。
                System.out.println(isInterrupted());

                try {

                    //isInterrupted();
                    sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                System.out.println(i);
            }


        }
    }

    public class BThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 100000; i++) {
                System.out.println(i);
            }


        }
    }



}