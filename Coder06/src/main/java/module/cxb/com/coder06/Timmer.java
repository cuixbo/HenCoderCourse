package module.cxb.com.coder06;

public class Timmer extends Thread {

    @Override
    public void run() {
        consume();
    }

    private void consume() {
        try {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("sleep"+i+"asdfas");
//                    Log.e("xbc","sleep");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println(e.getLocalizedMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
    }
}
