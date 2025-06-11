package akarin.bot.louise2.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author akarin
 * @version 1.0
 * @description 线程池工具
 * @date 2025/6/11 10:20
 */
@Slf4j
@Component
public class LouiseThreadPool {
    private final ExecutorService executor;
    private final AtomicInteger threadNum = new AtomicInteger(0);

    public LouiseThreadPool() {
        int corePoolSize = 6;
        int maxPoolSize = 24;
        int keepAliveTime = 60;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        LinkedBlockingDeque<Runnable> queue = new LinkedBlockingDeque<>(1000);

        this.executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, queue,
                r -> new Thread(r, "louise-exec-" + threadNum.getAndIncrement()));
    }

    // 提交任务
    public void submit(Runnable task) {
        executor.submit(task);
    }

    public void status() {
        log.debug("活跃线程: {}; 已完成任务: {}; 总任务: {}", getActiveCount(), getCompletedTaskCount(), getTaskCount());
    }

    // 关闭线程池
    public void shutdown() {
        try {
            executor.shutdown(); // 关闭线程池
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) { // 等待所有任务完成
                executor.shutdownNow(); // 强制关闭
            }
        } catch (InterruptedException e) {
            executor.shutdownNow(); // 强制关闭
            Thread.currentThread().interrupt(); // 重新设置中断状态
        }
    }

    // 检查线程池是否已经关闭
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    // 获取当前活动线程数
    public int getActiveCount() {
        return ((ThreadPoolExecutor) executor).getActiveCount();
    }

    // 获取已完成任务数
    public long getCompletedTaskCount() {
        return ((ThreadPoolExecutor) executor).getCompletedTaskCount();
    }

    // 获取总任务数
    public long getTaskCount() {
        return ((ThreadPoolExecutor) executor).getTaskCount();
    }
}
