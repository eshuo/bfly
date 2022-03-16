/**   
* @Title: AreaToJson.java 
* @Package info.bfly.archer.user.model 
* @Description:用一句话描述该文件做什么 
* @author zeminshao  
* @date 2017年4月27日 下午2:41:49 
* @version V1.0   
*/
package info.bfly.archer.user.model;

import java.io.Serializable;
import java.util.List;

/** 
 * @ClassName: AreaToJson 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月27日 下午2:41:49 
 *  
 */
public class AreaToJson implements Serializable {

    /** 
    * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
    */ 
    private static final long serialVersionUID = 314359482661089040L;
    
    private List<AreaItem> provinces;
    
    
    private List<AreaItem> sCitys;
    
    private List<AreaItem> district;

    public List<AreaItem> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<AreaItem> provinces) {
        this.provinces = provinces;
    }

    public List<AreaItem> getsCitys() {
        return sCitys;
    }

    public void setsCitys(List<AreaItem> sCitys) {
        this.sCitys = sCitys;
    }

    public List<AreaItem> getDistrict() {
        return district;
    }

    public void setDistrict(List<AreaItem> district) {
        this.district = district;
    }
    
    

}
