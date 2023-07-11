package com.nanshan.icc.generated.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nanshan.icc.config.mybatis.CJBaseColmns;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author cj
 * @since 2023-07-09
 */
@TableName("device")
public class DeviceEntity extends CJBaseColmns {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;

    @TableField("`name`")
    private String name;

    @TableField("`code`")
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "DeviceEntity{" +
            "id=" + id +
            ", name=" + name +
            ", code=" + code +
        "}";
    }
}
