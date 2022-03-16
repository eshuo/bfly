package info.bfly.crowd.mall.controller;

import info.bfly.archer.common.controller.EntityQuery;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.crowd.mall.MallConstants;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.mall.service.MallStageService;
import info.bfly.crowd.orders.model.MallStageCache;
import info.bfly.p2p.loan.service.LoanService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
/**
 * 
 * @author Administrator
 *
 */
@Component
@Scope(ScopeType.VIEW)
public class MallStageList extends EntityQuery<MallStage> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8083449113509804855L;
    private static final String lazyModelCountHql = "select count(distinct mallstage) from MallStage mallstage ";

    private static final String lazyModelHql = "select distinct mallstage from MallStage mallstage";
    
    @Resource
    private MallStageService mallStageService;
    
    @Resource
    private LoanService loanService;

    private Date searchCommitMinTime;


    private Date searchCommitMaxTime;
    
    
    private List<MallStage> showMallStageList = new ArrayList<MallStage>();
    
    private List<MallStage> chooseMallStageList;
    
    private String loanId;
    
    
    

    public MallStageList() {
        setCountHql(lazyModelCountHql);
        setHql(lazyModelHql);
        final String[] RESTRICTIONS = {
                "mallstage.id like #{mallStageList.example.id}",
                "mallstage.stageName like #{mallStageList.example.stageName}" };
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
    }
    
    @Override
    protected void initExample() {
        MallStage mallstage = new MallStage();
        mallstage.setMallStageCache(new MallStageCache());
        setExample(mallstage);
    }
    
    public void chooseMallStageList(){
        chooseMallStageList = new ArrayList<MallStage>();
        List<MallStage> chooseList = mallStageService.getMallStageByLoanId(loanId);
        if(null != chooseList && chooseList.size() != 0)
            chooseMallStageList.addAll(chooseList);
    }
    
    public List<MallStage> getShowMallStageList() {
        List<MallStage> notChooseList = mallStageService.getNotChooseMallStageList();
        
        if(null != notChooseList && notChooseList.size() != 0)
            showMallStageList.addAll(notChooseList);
        
        List<MallStage> chooseList = mallStageService.getMallStageByLoanId(loanId);
        
        if(null != chooseList && chooseList.size() != 0)
            showMallStageList.addAll(chooseList);
        
        return showMallStageList;
    }
    
    public String updateStage(){
        mallStageService.cleanAllMallStageByLoanId(loanId);
        mallStageService.updateMallStageAll(chooseMallStageList,loanId);
        return FacesUtil.redirect(MallConstants.View.MALL_LIST);
        
    }

    public Date getSearchCommitMinTime() {
        return searchCommitMinTime;
    }

    public void setSearchCommitMinTime(Date searchCommitMinTime) {
        this.searchCommitMinTime = searchCommitMinTime;
    }

    public Date getSearchCommitMaxTime() {
        return searchCommitMaxTime;
    }

    public void setSearchCommitMaxTime(Date searchCommitMaxTime) {
        this.searchCommitMaxTime = searchCommitMaxTime;
    }


    public void setShowMallStageList(List<MallStage> showMallStageList) {
        this.showMallStageList = showMallStageList;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public List<MallStage> getChooseMallStageList() {
        return chooseMallStageList;
    }

    public void setChooseMallStageList(List<MallStage> chooseMallStageList) {
        this.chooseMallStageList = chooseMallStageList;
    }


}
