package ext.common.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import wt.session.SessionThread;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * 会话线程池
 */
public class SessionThreadPoolExecutor extends ThreadPoolExecutor implements DisposableBean {
    public static final Logger logger = LoggerFactory.getLogger(SessionThreadPoolExecutor.class);

    /**
     * 创建SessionThreadPool
     * 
     * @param corePoolSize 核心线程数
     * @param maximumPoolSize 最大
     * @param keepAliveTime 存活时间
     * @param unit 单位
     * @param workQueue 阻塞对队列
     * @param threadFactory 线程工厂
     * @param handler 异常处理器
     */
    public SessionThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
        BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(new SessionThread(command));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(new SessionThread(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (Objects.isNull(task)) {
            throw new NullPointerException();
        }
        RunnableFuture<T> runnableFuture = newTaskFor(task);
        execute(runnableFuture);
        return runnableFuture;
    }

    /**
     * 实现SpringBean容器中的销毁Bean方法
     * 
     * @throws Exception
     */
    @Override
    public void destroy() {
        // 关闭线程池
        this.shutdown();
        try {
            if (!this.awaitTermination(3, TimeUnit.SECONDS)) {
                // 启动一个关闭线程
                this.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.error("关闭线程池失败！", e);
        }
    }
}
