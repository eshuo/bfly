package info.bfly.pay.p2p.user.listener;

import info.bfly.pay.p2p.user.event.MessageEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author Wangs
 * Date：2017/6/9 0009
 * Description：消息发送监听
 */
@Async
@Component
public class MessageListener implements ApplicationListener<MessageEvent> {


    @Override
    public void onApplicationEvent(MessageEvent event) {


        //TODO 调用发布信息


    }
}
