package akarin.bot.louise2.function;

import akarin.bot.louise2.domain.onebot.event.api.PostEventInterface;
import lombok.Getter;

/**
 * @author akarin
 * @version 1.0
 * @description 交互式函数包装器
 * @date 2025/6/11 13:39
 */
public class InteractFunctionWrapper<T> {

    @Getter
    private boolean active = true;

    private boolean waiting = true;

    private final InteractFunction<T> function;

    @Getter
    private T result;

    public InteractFunctionWrapper(InteractFunction<T> function) {
        this.function = function;
    }

    public synchronized InteractFunctionWrapper<T> waiting() {
        try {
            while (waiting) {
                this.wait();
            }
            waiting = true;
            return this;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return this;
    }

    public synchronized void wakeup() {
        waiting = false;
        this.notifyAll();
    }

    public void execute(PostEventInterface event) {
        result = function.execute(event);
    }

    public void inactive() {
        this.active = false;
    }

    public void active() {
        this.active = true;
    }

}
