package info.bfly.archer.picture.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 认证材料类型
 *
 * @author Administrator
 *
 */
@Entity
@Table(name = "autc_mtr_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
public class AutcMtrType implements Serializable {
    // Fields
    private static final long serialVersionUID = 5069931066487027433L;
    private String id;
    private String name;
    // FIXME:加一个字段：信息修改以后，是否需要重新审核，是否掉借款权限。
    private Boolean isChangeableAfterVerify          = false;
    private Boolean isNeedReverifyAfterChanged       = true;
    private Boolean isRemoveBorrowerRoleAfterChanged = false;
    /**
     * 最大上传数量
     */
    private Integer maxNumber                        = 1;
    /**
     * 最少上传数量
     */
    private Integer minNumber                        = 0;
    private String description;
    /**
     * 指导说明性文字
     */
    private String guide;
    /**
     * 样例图片
     */
    private String examplePictrue;

    /**
     * default constructor
     */
    public AutcMtrType() {
    }

    public AutcMtrType(String id) {
        this.id = id;
    }

    @Column(name = "description", length = 500)
    public String getDescription() {
        return description;
    }

    @Column(name = "example_picture", length = 1000)
    public String getExamplePictrue() {
        return examplePictrue;
    }

    @Column(name = "guide", length = 600)
    public String getGuide() {
        return guide;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false, length = 32)
    public String getId() {
        return id;
    }

    @Column(name = "max_number", nullable = false)
    public Integer getMaxNumber() {
        return maxNumber;
    }

    @Column(name = "min_number", nullable = false)
    public Integer getMinNumber() {
        return minNumber;
    }

    @Column(name = "name", length = 100)
    public String getName() {
        return name;
    }

    @Column(name = "changeableAfterVerify", columnDefinition = "boolean default false")
    public Boolean getIsChangeableAfterVerify() {
        return isChangeableAfterVerify;
    }

    @Column(name = "needReverifyAfterChanged")
    public Boolean getIsNeedReverifyAfterChanged() {
        return isNeedReverifyAfterChanged;
    }

    @Column(name = "removeBorrowerRoleAfterChanged")
    public Boolean getIsRemoveBorrowerRoleAfterChanged() {
        return isRemoveBorrowerRoleAfterChanged;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExamplePictrue(String examplePictrue) {
        this.examplePictrue = examplePictrue;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaxNumber(Integer maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void setMinNumber(Integer minNumber) {
        this.minNumber = minNumber;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setIsChangeableAfterVerify(Boolean isChangeableAfterVerify) {
        this.isChangeableAfterVerify = isChangeableAfterVerify;
    }


    public void setIsNeedReverifyAfterChanged(Boolean isNeedReverifyAfterChanged) {
        this.isNeedReverifyAfterChanged = isNeedReverifyAfterChanged;
    }


    public void setIsRemoveBorrowerRoleAfterChanged(Boolean isRemoveBorrowerRoleAfterChanged) {
        this.isRemoveBorrowerRoleAfterChanged = isRemoveBorrowerRoleAfterChanged;
    }
}
