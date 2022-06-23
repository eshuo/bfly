package info.bfly.crowd.orders.model;
/** 
 * @author  zeminshao: 
 * @date 创建时间：2017年4月25日 上午8:36:11 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  */
public class GoodsCacheItem {
    
    private String goodsCacheId;
    
    private String num;
    
    public GoodsCacheItem(){
        
    }
    
    public GoodsCacheItem(String goodsCacheId, String num) {
        super();
        this.goodsCacheId = goodsCacheId;
        this.num = num;
    }

    public String getGoodsCacheId() {
        return goodsCacheId;
    }

    public void setGoodsCacheId(String goodsCacheId) {
        this.goodsCacheId = goodsCacheId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
    
    
}
