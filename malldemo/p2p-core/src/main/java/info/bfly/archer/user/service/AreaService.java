package info.bfly.archer.user.service;

import info.bfly.archer.user.model.Area;
import info.bfly.archer.user.model.AreaToJson;

import java.util.List;

public interface AreaService {
    List<Area> findAllCities(String provinceId);

    List<Area> findAllCounties(String cityId);

    Area findProvincebyCity(Area area);

    List<Area> getAllProvinces();

    Area getAreaById(String id);
    
    /**
     * @Description 通过档位id锁定项目获取其配送范围
     * @param mallStageId
     * @return
     */
    List<Object> getAreaByMallStageId(String mallStageId);
    
    /**
     * 
    *
    * 获取所有市
    *
    * @Title: getAllCities 
    * @return    设定文件 
    * @return List<Area>    返回类型
     */
    List<Area> getAllCities();
    /**
     * 
    *
    * 获取所有县
    *
    * @Title: getAllCounties 
    * @return    设定文件 
    * @return List<Area>    返回类型
     */
    List<Area> getAllCounties();
    /**
     * 返回给前端json形式包装
     * @return
     */
    AreaToJson getAreaToJson();
    
}
