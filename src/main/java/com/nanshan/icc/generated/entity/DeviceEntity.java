package com.nanshan.icc.generated.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.nanshan.icc.config.mybatis.CJBaseColmns;

/**
 * <p>
 * 
 * </p>
 *
 * @author cj
 * @since 2023-08-10
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

    /**
     * 在线状态
     */
    @TableField("isOnline")
    private Integer isOnline;

    @TableField("owner_code")
    private String ownerCode;

    @TableField("deviceManufacturer")
    private String deviceManufacturer;

    /**
     * 设备ip
     */
    @TableField("deviceIp")
    private String deviceIp;

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
    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }
    public String getOwnerCode() {
        return ownerCode;
    }

    public void setOwnerCode(String ownerCode) {
        this.ownerCode = ownerCode;
    }
    public String getDeviceManufacturer() {
        return deviceManufacturer;
    }

    public void setDeviceManufacturer(String deviceManufacturer) {
        this.deviceManufacturer = deviceManufacturer;
    }
    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    @Override
    public String toString() {
        return "DeviceEntity{" +
            "id=" + id +
            ", name=" + name +
            ", code=" + code +
            ", isOnline=" + isOnline +
            ", ownerCode=" + ownerCode +
            ", deviceManufacturer=" + deviceManufacturer +
            ", deviceIp=" + deviceIp +
        "}";
    }
}
