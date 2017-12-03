import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 *
 * 业务场景：
 *      有20个人去售票厅买票，但是窗口只有2个，那么同时能够买票的只能有2个人；
 *      当2个人中任意一个人买好票离开之后，等待的18个人🈶️又会有一个人可以占用窗口买票。
 *
 * 拆解：
 *      20个人就是20个线程；
 *      2个窗口就是资源。
 *
 * 实际含义：
 *      怎么控制同一时间并发数为2 ？
 *
 * Semaphore信号量：目的就是控制并发访问某个资源的线程数 ！
 */
public class SemaphoreDemo {

    /**
     * 执行任务类：
     *
     * 获取信号量，释放信号量
     *
     */
    class SemaphoreRunnable implements Runnable {

        private Semaphore semaphore; // 信号量
        private int user; // 记录第几个用户

        /**
         * 构造方法
         * @param semaphore
         * @param user
         */
        public SemaphoreRunnable(Semaphore semaphore, int user) {
            this.semaphore = semaphore;
            this.user = user;
        }

        public void run() {
            try {
                // 获取信号量
                semaphore.acquire();

                System.out.println("用户【" + user + "】进入窗口，准备买票...");
                Thread.sleep((long)(Math.random()*10000));
                System.out.println("用户【" + user + "】买票完成，即将离开...");
                Thread.sleep((long)(Math.random()*10000));
                System.out.println("用户【" + user + "】离开售票窗口...");

                // 释放信号量
                semaphore.release();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void execute(){
        // 定义窗口数量
        final Semaphore semaphore = new Semaphore(2);
        // 线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 模拟20个用户
        for (int i = 0; i < 20; i++) {
            // 买票
            threadPool.execute(new SemaphoreRunnable(semaphore, i + 1));
        }
        // 关闭线程池 释放资源
        threadPool.shutdown();
    }

    /**
     * 主函数
     * @param args
     */
    public static void main(String[] args){
        SemaphoreDemo semaphoreDemo = new SemaphoreDemo();
        semaphoreDemo.execute();
    }

}

