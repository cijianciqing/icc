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
 * @since 2023-08-15
 */
@TableName("channel")
public class ChannelEntity extends CJBaseColmns {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;

    @TableField("`name`")
    private String name;

    @TableField("`code`")
    private String code;

    @TableField("device_id")
    private String deviceId;

    /**
     * 状态 0:关闭 1:开启 
     */
    @TableField("stat")
    private Integer stat;

    /**
     * 是否已经接入 ：1-已接入， 0-未接入 
     */
    @TableField("access")
    private Integer access;

    /**
     * 0	离线;1	在线
     */
    @TableField("is_online")
    private Integer isOnline;

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
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public Integer getStat() {
        return stat;
    }

    public void setStat(Integer stat) {
        this.stat = stat;
    }
    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
    }
    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return "ChannelEntity{" +
            "id=" + id +
            ", name=" + name +
            ", code=" + code +
            ", deviceId=" + deviceId +
            ", stat=" + stat +
            ", access=" + access +
            ", isOnline=" + isOnline +
        "}";
    }
}
