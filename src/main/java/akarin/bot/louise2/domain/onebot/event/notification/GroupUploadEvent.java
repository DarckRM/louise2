package akarin.bot.louise2.domain.onebot.event.notification;

import akarin.bot.louise2.domain.onebot.model.notification.FileUpload;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author akarin
 * @version 1.0
 * @description 文件上传通知事件
 * @date 2025/6/11 11:07
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupUploadEvent extends NotificationEvent {
    private FileUpload file;
}
