package info.bfly.archer.node.service.impl;

import info.bfly.archer.common.service.impl.BaseServiceImpl;
import info.bfly.archer.node.model.Node;
import info.bfly.archer.node.service.NodeService;
import info.bfly.core.annotations.Log;
import info.bfly.core.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

@Service
public class NodeServiceImpl extends BaseServiceImpl<Node> implements NodeService {
    @Log
    static Logger log;
    @Resource
    HibernateTemplate ht;

    @Override
    @Transactional(readOnly = false)
    public void save(Node node) {
        if (StringUtils.isEmpty(node.getNodeBody().getId())) {
            // 第一次创建
            node.getNodeBody().setId(IdGenerator.randomUUID());
        }
        if (node.getCreateTime() == null) {
            node.setCreateTime(new Date());
        }
        node.setUpdateTime(new Date());
        Double version = 0.1d;
        Iterator<Double> it = (Iterator<Double>) ht.iterate("select max(version) from NodeBodyHistory where node.id = ?", node.getId());
        if (it.hasNext()) {
            Double result = it.next();
            if (result != null) {
                version = new BigDecimal(version).add(new BigDecimal(result)).doubleValue();
            }
        }
        // NodeBodyHistory history = new NodeBodyHistory();
        // history.setId(IdGenerator.randomUUID());
        // history.setNode(node);
        // history.setCreateTime(node.getUpdateTime());
        // history.setVersion(version);
        // history.setBody(node.getNodeBody().getBody());
        // node.getNodeBodyHistories().add(history);
        ht.saveOrUpdate(node);
        // ht.save(history);
        // ht.save(history);
    }
}
