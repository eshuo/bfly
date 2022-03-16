package info.bfly.p2p.trusteeship.service.impl;

import info.bfly.archer.user.UserConstants;
import info.bfly.archer.user.model.VerifyHistory;
import info.bfly.archer.user.service.VerifyHistoryService;
import info.bfly.core.annotations.Log;
import info.bfly.p2p.trusteeship.TrusteeshipConstants;
import info.bfly.p2p.trusteeship.model.TrusteeshipOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Description: 资金托管，关联操作 BO
 */
@Service("trusteeshipOperationBO")
public class TrusteeshipOperationBO {
    @Resource
    HibernateTemplate ht;

    @Autowired
    VerifyHistoryService verifyHistoryService;
    @Log
    Logger               log;


    /**
     * 根据 操作类型（type） 操作的唯一标识（markId） 操作者（operator） 托管方(trusteeship)，查询，数据必须唯一。
     * 联合主键：操作类型+操作唯一标识+托管方
     *
     * @param type        操作类型
     * @param markId      操作的唯一标识
     * @param operator    操作者
     * @param trusteeship 托管方
     * @return 操作对象
     */
    public TrusteeshipOperation get(String type, String markId, String operator, String trusteeship) {
        DetachedCriteria criteria = DetachedCriteria.forClass(TrusteeshipOperation.class);
        if (type == null) {
            criteria.add(Restrictions.isNull("type"));
        } else {
            criteria.add(Restrictions.eq("type", type));
        }
        if (markId == null) {
            criteria.add(Restrictions.isNull("markId"));
        } else {
            criteria.add(Restrictions.eq("markId", markId));
        }
        if (operator == null) {
            criteria.add(Restrictions.isNull("operator"));
        } else {
            criteria.add(Restrictions.eq("operator", operator));
        }
        if (trusteeship == null) {
            criteria.add(Restrictions.isNull("trusteeship"));
        } else {
            criteria.add(Restrictions.eq("trusteeship", trusteeship));
        }
        List<TrusteeshipOperation> tos = (List<TrusteeshipOperation>) ht.findByCriteria(criteria);
        if (tos.size() > 1) {
            // 找到多个，抛异常。
            throw new DuplicateKeyException("type:" + type + " markId:" + markId + " operator:" + operator + " trusteeship:" + trusteeship + "  duplication!");
        }
        if (tos.size() == 0) {
            return null;
        }
        TrusteeshipOperation to = tos.get(0);
        return to;
    }

    /**
     * 获取minute分钟之前发出的且到目前为止尚未收到回调的所有请求
     *
     * @param minute 请求距离目前的时间间隔（分钟）
     * @return
     */
    public List<TrusteeshipOperation> getUnCallbackOperations(int minute) {
        Date date = DateUtils.addMinutes(new Date(), -minute);
        String hql = "from TrusteeshipOperation to where to.status=? and to.requestTime<?";
        return (List<TrusteeshipOperation>) ht.find(hql, TrusteeshipConstants.OperationStatus.SENDED, date);
    }

    /**
     * 获取待处理的回调请求
     */
    public List<TrusteeshipOperation> getWaitCallbackOperation() {
        String hql = "from TrusteeshipOperation to where to.status=? and to.type=?";
        return (List<TrusteeshipOperation>) ht.find(hql, TrusteeshipConstants.OperationStatus.CALLBACK, TrusteeshipConstants.OperationStatus.CALLBACK);
    }

    /**
     * 操作类型（type） 操作的唯一标识（markId） 操作者（operator） 托管方(trusteeship)
     * 上述四者一致，则认为是同一条数据。
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(TrusteeshipOperation to) {
        if (StringUtils.isEmpty(to.getType())) {
            throw new IllegalArgumentException("trusteshipOperation.type can not be empty!");
        }
        if (StringUtils.isEmpty(to.getMarkId())) {
            throw new IllegalArgumentException("trusteshipOperation.markId can not be empty!");
        }
        if (StringUtils.isEmpty(to.getOperator())) {
            throw new IllegalArgumentException("trusteshipOperation.operator can not be empty!");
        }
        if (StringUtils.isEmpty(to.getTrusteeship())) {
            throw new IllegalArgumentException("trusteshipOperation.trusteeship can not be empty!");
        }

        ht.saveOrUpdate(to);
    }


    @Transactional
    public TrusteeshipOperation get(String trusteeshipOperationId) {
        return ht.get(TrusteeshipOperation.class, trusteeshipOperationId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TrusteeshipOperation success(String operation) {
        return callback(operation, TrusteeshipConstants.OperationStatus.PASSED);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TrusteeshipOperation error(String operation) {
        return callback(operation, TrusteeshipConstants.OperationStatus.ERROR);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TrusteeshipOperation already(String operation) {
        return callback(operation, TrusteeshipConstants.OperationStatus.ALREADY);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TrusteeshipOperation error(String operation, String errormsg) {
        VerifyHistory verifyHistory = new VerifyHistory();
        verifyHistory.setStatus(UserConstants.VerifyStatus.FAIL);
        verifyHistory.setVerifyTarget(operation);
        verifyHistory.setVerifyType(UserConstants.VerifyType.CallBack);
        verifyHistory.setVerifyTime(new Date());
        verifyHistory.setVerifyMessage(errormsg);
        verifyHistoryService.addVerifyHistory(verifyHistory);
        return callback(operation, TrusteeshipConstants.OperationStatus.ERROR);
    }

    private TrusteeshipOperation callback(String operationId, String status) {
        TrusteeshipOperation operation = get(operationId);
        if (operation == null) {
            return null;
        }
        if (operation.getType().equals(TrusteeshipConstants.OperationType.CALLBACK)) {
            callback(operation.getMarkId(), status);
        }
        operation.setStatus(status);
        save(operation);
        return operation;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TrusteeshipOperation refuse(String operation) {
        return callback(operation, TrusteeshipConstants.OperationStatus.REFUSED);
    }

    /**
     * 等待后续操作
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TrusteeshipOperation waiting(String operation) {
        return callback(operation, TrusteeshipConstants.OperationStatus.WAITING);
    }
}
