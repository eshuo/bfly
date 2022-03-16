package info.bfly.archer.event;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Item;
import info.bfly.archer.node.controller.NodeList;
import info.bfly.archer.node.model.Node;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class InfoPush {
    @Resource
    public NodeList nodeList;

    public Channel createXML(List<Node> nodes) {
        Channel channel = new Channel("rss_2.0");
        channel.setLink("www.demo.com");// 网站主页链接
        channel.setEncoding("utf-8");// RSS文件编码
        channel.setLanguage("zh-cn");// RSS使用的语言
        channel.setPubDate(new Date());// RSS发布时间
        channel.setUri("www.demo.com");
        List<Item> items = new ArrayList<Item>();// 这个list对应rss中的item列表
        for (Node node : nodes) {
            Item item = new Item();// 新建Item对象，对应rss中的<item></item>
            item.setAuthor("friend");// 对应<item>中的<author></author>
            item.setTitle(node.getTitle());// 对应<item>中的<title></title>
            item.setGuid(new Guid());// GUID=Globally Unique Identifier
            // 为当前新闻指定一个全球唯一标示，这个不是必须的
            item.setLink("node/" + node.getCategoryTerms().get(0).getId() + "/" + node.getId());
            item.setUri("node/" + node.getCategoryTerms().get(0).getId() + "/" + node.getId());
            item.setPubDate(node.getCreateTime());// 这个<item>对应的发布时间
            item.setComments("注释");// 代表<item>节点中的<comments></comments>
            // 新建一个Description，它是Item的描述部分
            Description description = new Description();
            description.setValue(node.getDescription());// <description>中的内容
            item.setDescription(description);// 添加到item节点中
            items.add(item);// 代表一个段落<item></item>，
        }
        channel.setItems(items);
        // 用WireFeedOutput对象输出rss文本
        // WireFeedOutput out = new WireFeedOutput();
        /*
         * try { System.out.println(out.outputString(channel)); } catch
         * (IllegalArgumentException e) { log.debug(e.getMessage()); } catch
         * (FeedException e) { log.debug(e.getMessage()); }
         */
        return channel;
    }

    public NodeList getNodeList() {
        return nodeList;
    }

    public void setNodeList(NodeList nodeList) {
        this.nodeList = nodeList;
    }
}
