package akarin.bot.louise2.domain.onebot.model.notification;

import lombok.Data;

/**
 * @author akarin
 * @version 1.0
 * @description 文件上传
 * @date 2025/6/11 11:10
 */
@Data
public class FileUpload {

    private String id;

    private String name;

    private Long size;

    private Long busid;

}
