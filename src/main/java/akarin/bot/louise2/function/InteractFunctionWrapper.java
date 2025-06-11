package akarin.bot.louise2.function;

import akarin.bot.louise2.domain.onebot.event.api.PostEventInterface;

/**
 * @author akarin
 * @version 1.0
 * @description 交互式函数包装器
 * @date 2025/6/11 13:39
 */
public class InteractFunctionWrapper<T> {

    private boolean waiting = true;

    private final InteractFunction<T> function;

    public InteractFunctionWrapper(InteractFunction<T> function) {
        this.function = function;
    }

    public synchronized void waiting() {
        try {
            while (waiting) {
                this.wait();
            }
            waiting = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void wakeup() {
        waiting = false;
        this.notifyAll();
    }

    public void execute(PostEventInterface event) {
        function.execute(event);
    }

}
