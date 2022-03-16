/**   
* @Title: AreaItem.java 
* @Package info.bfly.archer.user.model 
* @Description:用一句话描述该文件做什么 
* @author zeminshao  
* @date 2017年4月27日 下午2:42:15 
* @version V1.0   
*/
package info.bfly.archer.user.model;

/** 
 * @ClassName: AreaItem 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author zeminshao
 * @date 2017年4月27日 下午2:42:15 
 *  
 */
public class AreaItem {
    
    private String id;
    
    private String value;
    
    private String parentId = "0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    
    
}
