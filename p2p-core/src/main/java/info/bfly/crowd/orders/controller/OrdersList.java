package info.bfly.crowd.orders.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.archer.user.model.User;
import info.bfly.core.annotations.ScopeType;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.orders.model.MallStageCache;
import info.bfly.crowd.orders.model.Order;
import info.bfly.crowd.orders.model.OrderCache;
import info.bfly.crowd.user.model.UserAddress;
import info.bfly.p2p.loan.model.Loan;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Scope(ScopeType.VIEW)
public class OrdersList  extends EntityQuery<Order> implements Serializable {

    /**
     * @author WangXiaotong
     */
    private static final long serialVersionUID = 1722629665715196795L;
    
    private static final String lazyModelCountHql = "select count(distinct order1) from Order order1 left join order1.orderCaches caches";
    private static final String lazyModelHql      = "select distinct order1 from Order order1 left join order1.orderCaches caches";

    private Date   searchcommitMinTime;
    private Date   searchcommitMaxTime;
    
    private String consigneePhone;
    
    private String userName;
    
    private String loanId;
    
    public OrdersList(){
        setCountHql(OrdersList.lazyModelCountHql);
        setHql(OrdersList.lazyModelHql);
        final String[] RESTRICTIONS = {"order1.id like #{ordersList.example.id}", "order1.user.username like #{ordersList.userName}",
                "caches.mallStageCache.mallStage.loan.id = #{ordersList.loanId}",
                "order1.userAddress.consigneePhone like #{ordersList.consigneePhone}", "order1.orderStatus = #{ordersList.example.orderStatus}",
                "order1.createTime >= #{ordersList.searchcommitMinTime}", "order1.createTime <= #{ordersList.searchcommitMaxTime}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
    
    @Override
    protected void initExample() {
        Order order = new Order();
        order.setUser(new User());
        setExample(order);
    }
    
    @Override
    public void dealResultList(java.util.List<Order> resultList) {
        if (!CollectionUtils.isEmpty(resultList)) {
            OrderCache orderCache = null;
            Map<String, Object> map = null;
            UserAddress userAddress = null;
            MallStageCache mallStageCache = null;
            MallStage mallStage = null;
            Loan loan = null;
            
            for (Order order : resultList) {
                orderCache = order.getOrderCaches().get(0);
                if (StringUtils.isEmpty(orderCache.getCache()))
                    continue;

                try {
                    map = new ObjectMapper().readValue(orderCache.getCache(),
                            HashMap.class);
                } catch (JsonParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                userAddress = new UserAddress();
                mallStageCache = new MallStageCache();
                mallStage = new MallStage();
                loan = new Loan();
                
                userAddress.setId(map.get("userAddressId").toString());
                userAddress.setConsigneeName(map.get("consigneeName")
                        .toString());
                userAddress.setConsigneePhone(map.get("consigneePhone")
                        .toString());
                userAddress.setDetailAddress(map.get("detailAddress")
                        .toString());
                userAddress
                        .setAreas(new String[] { map.get("area").toString() });

                order.setUserAddress(userAddress);

                loan.setId(map.get("loanId").toString());
                loan.setName(map.get("loanName").toString());
                mallStage.setLoan(loan);

                mallStage.setId(map.get("mallStageId").toString());
                mallStage.setStageName(map.get("mallStageName").toString());
                mallStage.setStageDescribe(map.get("mallStageDescribe").toString());
                mallStageCache.setMallStage(mallStage);
            }
        }

    };
    
    public Date getSearchcommitMaxTime() {
        return searchcommitMaxTime;
    }

    public Date getSearchcommitMinTime() {
        return searchcommitMinTime;
    }
    
    public void setSearchcommitMaxTime(Date searchcommitMaxTime) {
        this.searchcommitMaxTime = searchcommitMaxTime;
    }

    public void setSearchcommitMinTime(Date searchcommitMinTime) {
        this.searchcommitMinTime = searchcommitMinTime;
    }

    /**
     * 设置查询的起始和结束时间
     */
    public void setSearchStartEndTime(Date startTime, Date endTime) {
        searchcommitMinTime = startTime;
        searchcommitMaxTime = endTime;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }
}
