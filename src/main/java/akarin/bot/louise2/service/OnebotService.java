package akarin.bot.louise2.service;

import akarin.bot.louise2.domain.onebot.event.PostEvent;
import akarin.bot.louise2.domain.onebot.model.api.Message;

/**
 * @author akarin
 * @version 1.0
 * @description Onebot 协议的 Service 接口
 * @date 2025/6/6 13:40
 */
public interface OnebotService extends OnebotAccountService, OnebotMessageService {

    void reply(PostEvent event, Message message);

}
