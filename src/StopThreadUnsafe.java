/**
 * ClassName: StopThreadUnsafe
 * Description: 不安全地停止一个线程demo
 * Author: lizhao
 * Date: 2018/8/23 9:12
 * Version:
 */
public class StopThreadUnsafe {

    private static final User u = new User();

    public static class User {
        private Integer id;
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("User{");
            sb.append("id=").append(id);
            sb.append(", name='").append(name).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    public static class ChangeObjectThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (u) {
                    int v = (int) (System.currentTimeMillis() / 1000);
                    //设置id
                    u.setId(v);
                    //simulate : do something
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //设置name
                    u.setName(String.valueOf(v));
                }
                Thread.yield();
            }
        }
    }

    public static class ReadObjectThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (u) {
                    if (u.getId() != null && u.getName() != null && !u.getId().equals(Integer.parseInt(u.getName()))) {
                        System.out.println(u);
                    }
                }
                Thread.yield();
            }
        }
    }

    /**
     * 演示线程使用stop()方法后不正确地停止的恶果
     * 理论上,id和name应该保持一致,就不会在控制台有任何输出,但是使用了stop()方法后
     * 线程被强行终止,导致id与name不一致
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        new ReadObjectThread().start();
        while (true) {
            Thread thread = new ChangeObjectThread();
            thread.start();
            //主线程休眠150ms,等待thread线程运行到下一循环中间(循环周期100ms)时终止其运行
            Thread.sleep(150L);
            thread.stop();
        }
    }

}
