package com.example.qlexpressdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 条件表
 * </p>
 *
 * @author author
 * @since 2022-06-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("condition_info")
@ApiModel(value="ConditionInfo对象", description="条件表")
public class ConditionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "表达式 ")
    @TableField("expression")
    private String expression;

    @ApiModelProperty(value = "自引用id ")
    @TableField("parent_id")
    private Integer parentId;

    @ApiModelProperty(value = "响应信息")
    @TableField("result_info")
    private String resultInfo;

    @TableField("context_info")
    private String contextInfo;

    @TableField("param_info_id")
    private Integer paramInfoId;

    @TableField("rule_id")
    private Integer ruleId;


}
