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
@TableName("org")
public class OrgEntity extends CJBaseColmns {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;

    @TableField("org_code")
    private String orgCode;

    @TableField("org_name")
    private String orgName;

    @TableField("state")
    private Integer state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "OrgEntity{" +
            "id=" + id +
            ", orgCode=" + orgCode +
            ", orgName=" + orgName +
            ", state=" + state +
        "}";
    }
}
