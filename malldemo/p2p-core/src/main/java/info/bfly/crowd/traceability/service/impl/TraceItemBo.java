package info.bfly.crowd.traceability.service.impl;

import info.bfly.core.util.IdGenerator;
import info.bfly.crowd.traceability.model.TraceItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("traceItemBo")
public class TraceItemBo {
    @Autowired
    IdGenerator idGenerator;

    /**
     * 
     * @Title: generateId
     * @Description: 获取序列号
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public String generateId() {
        return idGenerator.nextId(TraceItem.class, "");
    }
}
