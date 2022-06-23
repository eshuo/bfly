/**   
* @Title: MallHome.java 
* @Package info.bfly.order.loan.controller 
* @Description: TODO(用一句话描述该文件做什么) 
* @author zeminshao  
* @date 2017年4月14日 下午1:35:35 
* @version V1.0   
*/package info.bfly.crowd.mall.controller;


import info.bfly.archer.user.model.Area;
import info.bfly.archer.user.service.AreaService;
import info.bfly.core.annotations.ScopeType;
import info.bfly.core.jsf.util.FacesUtil;
import info.bfly.crowd.mall.MallConstants;
import info.bfly.crowd.mall.service.MallService;
import info.bfly.p2p.loan.controller.LoanHome;
import info.bfly.p2p.loan.model.Loan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月14日 下午1:35:35 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
/** 
 * @ClassName: MallHome 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月14日 下午1:35:35 
 *  
 */
@Component
@Scope(ScopeType.VIEW)
public class MallHome extends LoanHome implements Serializable {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -917720743959947842L;
	
	@Resource
	private MallService mallService;
	
	private Area rootArea;
	
	private Area sonArea;
	
	private Area footArea;
	
	private List<Area> mallAreas;
	
	private String mallId;
	
	
    private List<Area> allProvinces;
    private List<Area> allCities;
    private List<Area> allCounties;
	
    @Autowired
    HibernateTemplate baseService;
    
    @Autowired
    private AreaService areaService;
	
    
    public MallHome(){
        this.rootArea = new Area();
        this.setUpdateView(MallConstants.View.MALL_LIST);
        this.setSaveView(MallConstants.View.MALL_LIST);
    }
    

    
    public void findAllCities() {
        allCounties = null;
        if (rootArea == null) {
            allCities = null;
        } else {
            allCities = areaService.findAllCities(rootArea.getId());
            if (allCities.size() > 0) {
                //selectedCity = allCities.get(0);
                findAllCounties();
            }
        }
    }
    
    public void findAllCounties() {
        footArea = null;
        if (sonArea == null) {
            setAllCounties(null);
        } else {
            setAllCounties(areaService.findAllCounties(sonArea.getId()));
        }
    }
    
    /**
     * 初始化实体类
     */
	@Override
	protected void initInstance() {
	 if (isIdDefined()) {
            setInstance(baseService.get(Loan.class, getId()));
        } else {
            setInstance(createInstance());
        }
	};
	
	
	/**
	 * 
	* @Title: initLoanAreas 
	* @Description:  初始化配送范围列表
	* @param @param value    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void initMallAreas(List<Area> value) {
        if (mallAreas == null) {
        	mallAreas = new ArrayList<Area>();
        	if (value != null) {
        		mallAreas.addAll(value);
        	}
        }
    }
	
	
	/**
	 * 
	* @Title: returnAllName 
	* @Description:  显示所有配送范围中文名,从大到小
	* @param @param mallArea
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String returnAllName(Area area){
	    if(null ==area)
	        return "";
	    
		if (null == area.getParent()) {
			return area.getName();
		} else{
			return returnAllName(area.getParent()) +"-" + area.getName();
		}
		
	}
	
	/**
	 * 
	* @Title: updateMall 
	* @Description:  保存配送范围
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String updateMall(){
		this.getInstance().setAreas(mallAreas);
		mallService.updateMall(this.getInstance());
		return FacesUtil.redirect(this.getUpdateView());
	}
	
	/**
	 * 
	* @Title: deleteMall 
	* @Description:  删除配送范围
	* @param @param area
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public void deleteMallArea(Area area){
		mallService.removeMallArea(this.getInstance(), area);
		mallAreas.remove(area);
	}
	
	/**
	 * 
	* @Title: handleMallArea 
	* @Description:  新增配送范围
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
    public void handleMallArea() {
        if(null != footArea){
            if(!getMallAreas().contains(footArea))
                getMallAreas().add(footArea);
        }else if(null != sonArea){
            if(!getMallAreas().contains(sonArea))
                getMallAreas().add(sonArea);
        }else if(null != rootArea){
            if(!getMallAreas().contains(rootArea))
                getMallAreas().add(rootArea);
        }

    }
    
    /**
     * 
    * @Title: countMallSupportNum 
    * @Description:  
    * @param @param loanId
    * @param @return    设定文件 
    * @return Integer    返回类型 
    * @throws
     */
    public Long countMallSupportNum(String loanId){
        return mallService.countMallSupportNum(loanId);
    }


	public Area getRootArea() {
        return rootArea;
    }

    public void setRootArea(Area rootArea) {
        this.rootArea = rootArea;
    }

    public Area getSonArea() {
        return sonArea;
    }

    public void setSonArea(Area sonArea) {
        this.sonArea = sonArea;
    }

    public Area getFootArea() {
        return footArea;
    }

    public void setFootArea(Area footArea) {
        this.footArea = footArea;
    }

    public List<Area> getMallAreas() {
		return mallAreas;
	}


	public void setMallAreas(List<Area> mallAreas) {
		this.mallAreas = mallAreas;
	}


	public String getMallId() {
		return mallId;
	}


	public void setMallId(String mallId) {
		this.mallId = mallId;
	}



    public List<Area> getAllProvinces() {
        if (allProvinces == null) {
            allProvinces = areaService.getAllProvinces();
            if (rootArea == null) {
                rootArea = allProvinces.get(0);
            }
            findAllCities();
            
            findAllCounties();
        }
        return allProvinces;
    }



    public void setAllProvinces(List<Area> allProvinces) {
        this.allProvinces = allProvinces;
    }



    public List<Area> getAllCounties() {
        return allCounties;
    }



    public void setAllCounties(List<Area> allCounties) {
        this.allCounties = allCounties;
    }



    public List<Area> getAllCities() {
        return allCities;
    }



    public void setAllCities(List<Area> allCities) {
        this.allCities = allCities;
    }
	

	

}
