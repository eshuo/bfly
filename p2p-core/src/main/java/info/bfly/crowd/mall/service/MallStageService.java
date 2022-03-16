package info.bfly.crowd.mall.service;

import java.util.List;
import java.util.Map;

import info.bfly.crowd.mall.controller.MallStageHome;
import info.bfly.crowd.mall.model.MallStage;
import info.bfly.crowd.orders.model.MallStageCache;

/**
 * 
 * @author hdy
 *
 */

public interface MallStageService {
    /**
     * @author Administrator
     * @根据id保存实体
     * @param id
     */
    void save(String id);
    /**
     * @description:根据实体保存
     * @param mallstagehome
     */
    void save(MallStageHome mallstagehome);
    /**
     * @description：根据实体保存
     * @param mallstagehome
     */
    void save(MallStage mallstage);
    /**
     * @author Administrator
     * @param mallstagehome
     * @Description:根据传入的参数以及实体做保存
     * @param maxNum
     * @param fee
     * @param min_fee
     * @param type_id
     */
    void save(MallStage mallstage,Integer maxNum,Double fee ,Integer min_fee,String type_id);
    /**
     * @Description:根据传入的两个Id先查询再做保存
     * @param loan_id
     * @param crowd_id
     */
    void save(String loan_id,String crowd_id);
    /**
     * @Description:根据传入的Id查出一个List结果集
     * @param crowd_id
     * @return
     */
    List<MallStage> QueryMallStageById(String crowd_id);
    
    /**
     * 
    * @Title: selectMallStageById 
    * @Description: 查询档位
    * @param @param id
    * @param @return    设定文件 
    * @return MallStage    返回类型 
    * @throws
     */
    MallStage selectMallStageById(String id);
    /**
     * 
    * @Title: setPrivateMallStage 
    * @Description:  只针对订制模式，订制模式提交 
    * @param @param stageId
    * @param @param goodsCacheMap    设定文件 
    * @return void    返回类型 
    * @throws
     */
    String setPrivateMallStage(String stageId,Map<String,String> goodsCacheMap);

    /**
     * @Descprition :根据项目编号获取档位和配置MallStage.getStageInventory列表
     * @param  loan_id
     * @return List
     */
    List<MallStage> QueryMallStageByLoanId(String loan_id);
    /**
     * @Description 根据档位id查询出用户不重复的订单数量
     * @param mallStageId
     */
    Integer supportNum (String Id);
    /**
     * @Description 根据档位id查询出用户重复的订单数量 即已被订购的订单数量
     * @param mallStageId
     */
    Integer beBoughtNum(String Id);
    
    /**
     * 
    * @Title: countMallTotalPrice 
    * @Description:  计算当前档位的总价
    * @param @param mallStageCacheId
    * @param @return    设定文件 
    * @return Double    返回类型 
    * @throws
     */
    Double countMallTotalPrice(String mallStageCacheId,String mallStageId);
    
    /**
     * 
    * @Title: getMallStageCache 
    * @Description:  查询mallSatgeCache数据
    * @param @param mallStageCacheId
    * @param @return    设定文件 
    * @return MallStageCache    返回类型 
    * @throws
     */
    MallStageCache getMallStageCache(String mallStageCacheId);
    
    /**
     * 
    * @Title: getMallStage 
    * @Description:  查询mallStage数据
    * @param @param mallStageId
    * @param @return    设定文件 
    * @return MallStage    返回类型 
    * @throws
     */
    MallStage getMallStage(String mallStageId);
    /**
     * 根据项目id查询已支付或者已完成的金额
     * @param loanId
     * @return
     */
    Double getTotalOrderCountByLoanId(String loanId);
    
    
    /**
     * 
    *
    *清空此项目下和档位的关联关系
    *
    * @Title: update 
    * @param mallStage    设定文件 
    * @return void    返回类型
     */
    void cleanAllMallStageByLoanId(String loanId);
    /**
     * 重新关联此项目下的关联关系
     * @param mallStageList
     * @param loanId
     */
    void updateMallStageAll(List<MallStage> mallStageList,String loanId);
    
    /**
     * 
    *
    * 获取未关联的档位
    *
    * @Title: getNotChooseMallStageList 
    * @return    设定文件 
    * @return List<MallStage>    返回类型
     */
    List<MallStage> getNotChooseMallStageList();
    /**
     * 
    *
    * 根据项目id获取档位
    *
    * @Title: getMallStageByLoanId 
    * @param loanId
    * @return    设定文件 
    * @return List<MallStage>    返回类型
     */
    List<MallStage> getMallStageByLoanId(String loanId);
    
  }

 