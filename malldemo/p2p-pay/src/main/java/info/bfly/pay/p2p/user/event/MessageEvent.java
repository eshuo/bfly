package info.bfly.pay.p2p.user.event;

import info.bfly.archer.user.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * @Author Wangs
 * Date：2017/6/9 0009
 * Description：消息发送Event
 */
public class MessageEvent extends ApplicationEvent {


    private Map map;

    private String type;

    private User user;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public MessageEvent(Object source, Map map, String type, User user) {
        super(source);
        this.map = map;
        this.type = type;
        this.user = user;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
