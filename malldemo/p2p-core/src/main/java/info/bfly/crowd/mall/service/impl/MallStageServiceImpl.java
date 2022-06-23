package info.bfly.crowd.mall.service.impl;



import info.bfly.core.util.ArithUtil;
import info.bfly.core.util.MapUtil;
import info.bfly.crowd.mall.controller.MallStageHome;
import info.bfly.crowd.mall.model.Inventory;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.mall.service.InventoryService;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.model.MallStageCache;
import info.bfly.crowd.orders.service.GoodsCacheService;
import info.bfly.crowd.orders.service.impl.MallStageCacheBO;
import info.bfly.p2p.loan.model.Loan;
import info.bfly.p2p.loan.service.LoanService;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("mallStageService")
public class MallStageServiceImpl implements MallStageService{
    
    @Resource
    HibernateTemplate ht;
    
    @Resource
    private MallStageBo mallStageBo;
    
    @Resource
    private  InventoryService   inventoryService;
    
    @Resource
    private  MallStageService   mallstageservice;
    
    @Resource 
    private GoodsCacheService goodscacheservice;
    
    @Resource
    private MallStageCacheBO stageCacheBo;
    @Resource
    private LoanService loanService;
    



    @Override
    public void save(String id) {
        // TODO Auto-generated method stub
        ht.saveOrUpdate(id, MallStage.class);;
        
    }


    @Override
    public void save(MallStageHome mallstagehome) {
        ht.saveOrUpdate(mallstagehome);
        
    }


    @Override
    public void save(MallStage mallstage) {
        /*MallStage mallStage =ht.get(MallStage.class, mallstage.getId());
        if(null == mallStage.getLoan().getId()){
            ht.saveOrUpdate(mallstage);
        }else{
          List<MallStage>  mallStage2 = (List<MallStage>) ht.find("from MallStage mallstage where mallstage.loan.id = ? ", mallStage.getLoan().getId());
            for(int i=0;i<mallStage2.size();i++){
                mallStage= ht.get(MallStage.class, mallStage2.get(i).getId());
                ht.delete(mallStage);
                ht.saveOrUpdate(mallstage);
                
            }
        }*/
      if(mallstage.getId()==null){
            mallstage.setId(mallStageBo.generateId());
        }
        mallstage.setCommitTime(new Date());
        ht.saveOrUpdate(mallstage);
        ht.flush();
    }


    @Override
    public void save(MallStage mallstage, Integer maxNum, Double fee, Integer min_fee,String type_id) {
        Inventory inventory = new Inventory();
        if(mallstage.getStageInventory().getId()==null){
            mallstage.getStageInventory().setId(mallStageBo.generateId());
        }
        if(mallstage.getId()==null){
            mallstage.setId(mallStageBo.generateId());
        }
        inventory.setId(mallstage.getStageInventory().getId());
        inventory.setMaxNum(maxNum);
        inventory.setFee(fee);
        inventory.setMinFee(min_fee);
        inventory.setMallStage(mallstage);
        inventory.getMallStage().setId(mallstage.getId());
 
        inventoryService.save(inventory);
        if(mallstage.getId() == null){
            mallstage.setId(inventory.getMallStage().getId());    
        }
      //TODO 这里应该传入一个List的ids，暂时先做成单个id传入，默认值为“1”;之后改为前台传入的list
        List<GoodsCache> goodscaches =goodscacheservice.QueryGoodsCacheById("1");
        mallstage.setGoodsCache(goodscaches);
       
        mallstageservice.save(mallstage);
        
        goodscaches.get(0).setMallStage(mallstage);
        
        goodscacheservice.save(goodscaches.get(0));
        
    }

    @Override
    public void save(String loan_id, String crowd_id) {
        //TODO 这里应该传入一个List的ids，暂时先做成单个id传入，默认值为“1”;之后改为前台传入的list
        List<MallStage> mallstages =  mallstageservice.QueryMallStageById("1");
        Loan loan = new Loan();
        loan.setMallstages(mallstages);
        loan.setId(mallStageBo.generateId());
        ht.saveOrUpdate(loan);
        
        mallstages.get(0).setLoan(loan);
        ht.saveOrUpdate(mallstages.get(0));
        
    }
    
    @Override
    public List<MallStage> QueryMallStageById(String crowd_id) {
        // TODO Auto-generated method stub
        List<MallStage> mallstages = (List<MallStage>)ht.find("from MallStage mallstage where mallstage.id= ?", crowd_id);
        return mallstages;
    }




    @Override
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public String setPrivateMallStage(String stageId,
            Map<String, String> goodsCacheMap) {
        MallStage mallStage =   this.selectMallStageById(stageId);
        String stagetCacheId = stageCacheBo.generateId();
        
        if(null != mallStage){
            MallStageCache stageCache = new MallStageCache();
            String  cache ="";
            if(null != mallStage.getStageInventory().getMinFee() && 0 != mallStage.getStageInventory().getMinFee()){
                //订制模式
                cache =MapUtil.hashMapToJsonString(goodsCacheMap);
            }else{
                //固定模式
                Map<String,String> map = new HashMap<String, String>();
                for(GoodsCache goodsCache:mallStage.getGoodsCache()){
                    map.put(goodsCache.getId(), goodsCache.getNum().toString());
                }
                cache = MapUtil.hashMapToJsonString(map);
            }
            stageCache.setId(stagetCacheId);
            stageCache.setCache(cache);
            stageCache.setMallStage(mallStage);
            ht.save(stageCache);
            ht.flush();
            return stagetCacheId;
        }
        
        return null;
    }


    @Override
    public MallStage selectMallStageById(String id) {
        if(StringUtils.isNotEmpty(id))
            return ht.get(MallStage.class, id);
        return null;
    }



    @Override
    public List<MallStage> QueryMallStageByLoanId(String loanId) {
        // TODO Auto-generated method stub
        List<MallStage> mallstages = (List<MallStage>)ht.find("from MallStage mallstage where mallstage.loan.id = ?", loanId);
        return mallstages;
    }


    @Override
    public Integer supportNum(String Id) {
      //支持人数supportNum
          List<Object> supportNum = (List<Object>) ht.find("select count(order1.user) from Order order1 left join order1.orderCaches caches where (order1.orderStatus = 'payed' or order1.orderStatus = 'finished') and caches.mallStageCache.mallStage.id = ?",
                Id);
          Integer Num = 0;
          Num = Integer.parseInt(supportNum.get(0).toString());
        return Num;
    }


    @Override
    public Integer beBoughtNum(String Id) {
        List<Object> beBoughtNum = (List<Object>) ht
                .find("select count(order1.id) from  Order order1 left join order1.orderCaches caches where (order1.orderStatus = 'payed' or order1.orderStatus = 'finished') and caches.mallStageCache.mallStage.id = ? ",
                        Id);
        // 根据档位id查询该档位的最大购买份额
        MallStage mallStage =  this.getMallStage(Id);
        int maxNum = mallStage.getStageInventory().getMaxNum();
        int bebought = 0;
        bebought = Integer.parseInt(beBoughtNum.get(0).toString());
        // 剩余份数=最大购买份额-已售份数
        Integer overplus = maxNum - bebought;
        if (overplus >= 0) {
            return overplus;
        } else {
            return 0;
        }

    }


    @Override
    public Double countMallTotalPrice(String mallStageCacheId,
            String mallStageId) {

        Double totoalPrice = 0.0d;

        if (StringUtils.isNotEmpty(mallStageCacheId)) {
            MallStageCache mallStageCache = ht.get(MallStageCache.class,
                    mallStageCacheId);

            if (null != mallStageCache) {
                Map<String, String> cacheMap = new HashMap<String, String>();
                try {
                    cacheMap = MapUtil.jsonStringToHashMap(mallStageCache
                            .getCache());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                Integer num = 0;
                for (String goodsCacheId : cacheMap.keySet()) {
                    num = Integer.valueOf(cacheMap.get(goodsCacheId));
                    totoalPrice = totoalPrice
                            + (ht.get(GoodsCache.class, goodsCacheId)
                                    .getMoney() * num);
                }
                return ArithUtil.round(totoalPrice, 2);
            }

        }
        MallStage mallStage = ht.get(MallStage.class, mallStageId);
        if (null == mallStage)
            return totoalPrice;
        totoalPrice = mallStage.getStageInventory().getFee();

        return ArithUtil.round(totoalPrice, 2);
    }


    @Override
    public MallStageCache getMallStageCache(String mallStageCacheId) {
        return ht.get(MallStageCache.class, mallStageCacheId);
    }


    @Override
    public MallStage getMallStage(String mallStageId) {
        return ht.get(MallStage.class, mallStageId);
    }
    
    @Override
    public Double getTotalOrderCountByLoanId(String loanId) {
        
        if(StringUtils.isEmpty(loanId))
            return 0d;
        List<Object> list = (List<Object>) ht
                .find("Select coalesce(sum(order1.invest.money),0) from  Order order1 left join order1.orderCaches caches where caches.mallStageCache.mallStage.loan.id = ? and (order1.orderStatus = 'payed' or order1.orderStatus = 'finished')",
                        loanId);
        return Double.parseDouble(list.get(0).toString());
        
    }
    
    
    @Override
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void cleanAllMallStageByLoanId(String loanId) {
        if(null != loanId){
            ht.clear();
            ht.bulkUpdate("update from MallStage mallStage set mallStage.loan = null where mallStage.loan.id = ?", loanId);
            ht.flush();
        }
        
    }

    @Override
    @Transactional(readOnly=false,rollbackFor=Exception.class)
    public void updateMallStageAll(List<MallStage> mallStageList,String loanId){
        Loan loan = loanService.get(loanId);
        MallStage stage = null;
        ht.clear();
        for(MallStage mallStage:mallStageList){
            stage = ht.get(MallStage.class, mallStage.getId());
            stage.setLoan(loan);
            ht.update(stage);
        }
    }
    
    @Override
    public List<MallStage> getNotChooseMallStageList() {
        return (List<MallStage>) ht.find("Select mallStage from MallStage mallStage where mallStage.loan is null", null);
    }


    @Override
    public List<MallStage> getMallStageByLoanId(String loanId) {
        return (List<MallStage>) ht.find("Select mallStage from MallStage mallStage where mallStage.loan.id = ?", loanId);
    }


    
}
