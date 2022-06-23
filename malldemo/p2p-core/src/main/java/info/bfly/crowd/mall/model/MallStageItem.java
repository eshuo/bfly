package info.bfly.crowd.mall.model;

/**
 * Created by XXSun on 2016/12/27.
 */
public class MallStageItem {
    private String goodsCacheId;

    private String num;

    public MallStageItem() {
    }
    
    public MallStageItem(String goodsCacheId, String num) {
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