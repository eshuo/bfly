package info.bfly.archer.user.service.impl;

import info.bfly.archer.common.service.impl.BaseServiceImpl;
import info.bfly.archer.user.model.Area;
import info.bfly.archer.user.model.AreaItem;
import info.bfly.archer.user.model.AreaToJson;
import info.bfly.archer.user.service.AreaService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;

@Service(value = "areaService")
@SuppressWarnings("unchecked")
public class AreaServiceImpl extends BaseServiceImpl<Area> implements AreaService {
    @Resource
    private HibernateTemplate ht;

    @Override
    public List<Area> findAllCities(String provinceId) {
        // 如果是直辖市的话，城市显示区
        // if ("110000".equals(provinceId)) {
        // return ht.find("from Area area where area.parent.id = 110100");
        // } else if ("120000".equals(provinceId)) {
        // return ht.find("from Area area where area.parent.id = 120100");
        // } else if ("500000".equals(provinceId)) {
        // return ht.find("from Area area where area.parent.id = 500100");
        // } else if ("310000".equals(provinceId)){
        // return ht.find("from Area area where area.parent.id = 310100");
        // } else {
        return (List<Area>) ht.find("from Area area where area.parent.id = '" + provinceId + "'");
        // }
    }

    @Override
    public List<Area> findAllCounties(String cityId) {
        return (List<Area>) ht.find("from Area area where area.parent.id = '" + cityId + "'");
    }

    /**
     * 根据城市id去查询省份
     */
    @Override
    public Area findProvincebyCity(Area area) {
        Area areaProvince = (Area) ht.find("from Area where id = ?", area.getParent().getId()).get(0);
        return areaProvince;
    }

    @Override
    public List<Area> getAllProvinces() {
        return (List<Area>) ht.find("from Area area where area.parent.id = null");
    }

    @Override
    public Area getAreaById(String id) {
        return ht.get(Area.class, id);
    }

    @Override
    public List<Object> getAreaByMallStageId(String mallStageId) {
        List<Object> mallAddr = (List<Object>)ht.find("from Area area where area.loan.mallstage.id = ?", mallStageId);
        return mallAddr;
    }
    
    
    @Override
    public AreaToJson getAreaToJson() {
        AreaToJson areaToJson = new AreaToJson();
        
       List<Area> provincesList =  this.getAllProvinces();
       List<Area> cityList =  this.getAllCities();
       List<Area> countiesList = this.getAllCounties();
       
       List<AreaItem> jsonList = null;
       AreaItem item = null;
       //省
       jsonList = new ArrayList<AreaItem>();
       for(Area area:provincesList){
           item = new AreaItem();
           item.setId(area.getId());
           item.setValue(area.getName());
           item.setParentId("0");
           jsonList.add(item);
       }
       
       areaToJson.setProvinces(jsonList);
       //市
       jsonList = new ArrayList<AreaItem>();
       for(Area area:cityList){
           item = new AreaItem();
           item.setId(area.getId());
           item.setValue(area.getName());
           item.setParentId(area.getParent().getId());
           jsonList.add(item);
       }
       areaToJson.setsCitys(jsonList);
       //县
       jsonList = new ArrayList<AreaItem>();
       for(Area area:countiesList){
           item = new AreaItem();
           item.setId(area.getId());
           item.setValue(area.getName());
           item.setParentId(area.getParent().getId());
           jsonList.add(item);
       }
       areaToJson.setDistrict(jsonList);
       
       return areaToJson;
    }

    @Override
    public List<Area> getAllCities() {
        return (List<Area>) ht.find("from Area area where area.parent like '%0000' ");
    }

    @Override
    public List<Area> getAllCounties() {
        return (List<Area>) ht.find("from Area area where area.id not  like '%00' ");
    }
}
