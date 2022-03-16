package info.bfly.crowd.mall.controller;

import info.bfly.archer.common.controller.EntityHome;
import info.bfly.archer.system.controller.LoginUserInfo;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.core.util.ArithUtil;
import info.bfly.crowd.mall.MallConstants;
import info.bfly.crowd.mall.model.Inventory;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.mall.service.InventoryService;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.crowd.orders.model.GoodsCache;
import info.bfly.crowd.orders.service.GoodsCacheService;
import info.bfly.crowd.orders.service.OrderService;
import info.bfly.crowd.user.service.UserAddressService;
import info.bfly.p2p.invest.service.InvestService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Scope(ScopeType.VIEW)
public class MallStageHome extends EntityHome<MallStage> implements Serializable{

    private static final long serialVersionUID = 1L;
    
    @Resource
    private  MallStageService   mallStageService;
    
    @Resource
    private  InventoryService   inventoryService;

    @Resource 
    private GoodsCacheService goodsCacheService;
    
    @Resource
    private UserAddressService userAddressService;
    


    @Resource
    private InvestService investService;
    
    @Resource
    private OrderService orderService;
    
    @Resource
    private LoginUserInfo loginUserInfo;
    
    public String mallStageType;
    
    private String mallStageId;
    
    
    private Double totalAmount = 0d;
    
    private String chooseAddressId;
    
    
    
    private List<GoodsCache> chooseGoodsCacheList;
    
    private List<GoodsCache> showGoodsCacheList = new ArrayList<GoodsCache>();
    
    public void  chooseGoodsCacheList(){
    	chooseGoodsCacheList = new ArrayList<GoodsCache>();
    	List<GoodsCache> chooseList =goodsCacheService.QueryGoodsCacheByMallStageId(this.getId());
    	if(null!=chooseList&&chooseList.size()!=0)
    		chooseGoodsCacheList.addAll(chooseList);
    }

    public String getMallStageId() {
		return mallStageId;
	}

	public void setMallStageId(String mallStageId) {
		this.mallStageId = mallStageId;
	}

	public MallStageHome(){
        setUpdateView(FacesUtil.redirect(MallConstants.View.MALL_STAGE_LIST));
    }
    
	

	/**
     * 提交私人订制
     */
    public String mallStagePrivateSubmit() {

        if (null == loginUserInfo.getLoginUserId())
            return "pretty:memberLogin";

        Map<String, String> goodsCacheMap = new HashMap<String, String>();
        Double totalAmount = 0d;
        for (GoodsCache goodsCache : this.getInstance().getGoodsCache()) {
            goodsCacheMap.put(goodsCache.getId(), goodsCache.getNum()
                    .toString());
            totalAmount = totalAmount + goodsCache.getNum()
                    * goodsCache.getMoney();
        }
        // 判断提交的金额是否超过最低订制金额
        int result = ArithUtil.compareTo(totalAmount, this.getInstance()
                .getStageInventory().getMinFee());
        if (result < 0)
            FacesUtil.addErrorMessage("总价没有超过最底订制金额，请重新选择！");
        String mallStageCacheId = mallStageService.setPrivateMallStage(
                this.getId(), goodsCacheMap);

        return FacesUtil.redirect("/themes/default/public/loan/extendedprice?mallStageCacheId="
                + mallStageCacheId + "&mallStageId=" + this.getId());

    }
	
    
	
    @Override
    public MallStage createInstance() {
        MallStage mallStage = new MallStage();
        mallStage.setStageInventory(new Inventory());
        return mallStage;
    }
    
    @Transactional(readOnly = false)
    public void save(MallStage mallstage,Integer maxNum,Double fee ,Integer min_fee,String type_id){
        mallStageService.save(mallstage, maxNum, fee, min_fee,type_id);
    }
    
    @Transactional(readOnly = false)
    public void save(String loan_id,String crowd_id){
        mallStageService.save(loan_id,crowd_id);
    }
    
    public void save(MallStage mallStage){
        mallStageService.save(mallStage);
    }
    
    @Transactional(readOnly = false)
    @Override
    public String save(){
        MallStage mallStage = new MallStage();
        mallStage = getInstance();
        inventoryService.save(mallStage.getStageInventory());
        save(mallStage);
        
        if(StringUtils.isNotEmpty(this.getId())){
        goodsCacheService.cleanByMallStageId(mallStage.getId());
        goodsCacheService.updateGoodsCacheAll(chooseGoodsCacheList, mallStage.getId());
        }else{
            for(int i = 0; i < chooseGoodsCacheList.size(); i++){
                GoodsCache goodcache = new GoodsCache();
                goodcache = chooseGoodsCacheList.get(i);
                goodcache.setMallStage(mallStage);
                goodsCacheService.updateGoodsCache(goodcache);
            }
        }
        return FacesUtil.redirect(getSaveView());

    }
    
    
    
    public String getMallStageType() {
        return mallStageType;
    }

    public void setMallStageType(String mallStageType) {
        this.mallStageType = mallStageType;
    }

	public List<GoodsCache> getChooseGoodsCacheList() {
		return chooseGoodsCacheList;
	}

	public void setChooseGoodsCacheList(List<GoodsCache> chooseGoodsCacheList) {
		this.chooseGoodsCacheList = chooseGoodsCacheList;
	}

	public List<GoodsCache> getShowGoodsCacheList() {
		List<GoodsCache> notChooseList = goodsCacheService.getNotChooseGoodsCacheList();
		if(null!=notChooseList&&notChooseList.size()!=0)
			showGoodsCacheList.addAll(notChooseList);
		List<GoodsCache> chooseList = goodsCacheService.QueryGoodsCacheByMallStageId(this.getId());
		if(null!=chooseList&&chooseList.size()!=0)
			showGoodsCacheList.addAll(chooseList);
		
		return showGoodsCacheList;
	}

	public void setShowGoodsCacheList(List<GoodsCache> showGoodsCacheList) {
		this.showGoodsCacheList = showGoodsCacheList;
	}

    public Double getTotalAmount() {
        Double amount = 0d;
        if(StringUtils.isNotEmpty(this.getId())){
             for(GoodsCache goodsCache:this.getInstance().getGoodsCache()){
                 amount = amount + goodsCache.getMoney() * goodsCache.getNum();
                }
        }
        return amount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getChooseAddressId() {
        return chooseAddressId;
    }

    public void setChooseAddressId(String chooseAddressId) {
        this.chooseAddressId = chooseAddressId;
    }

}
