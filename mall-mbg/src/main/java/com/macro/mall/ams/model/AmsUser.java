package com.macro.mall.ams.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author dongjb
 * @since 2021-01-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ams_user")
@ApiModel(value="AmsUser对象", description="")
public class AmsUser implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private String name;

    private String loginno;

    private String password;

    private Integer levels;

    private Integer status;

    private Integer sort;


}
