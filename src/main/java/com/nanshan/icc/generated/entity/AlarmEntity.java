package com.nanshan.icc.generated.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nanshan.icc.config.mybatis.CJBaseColmns;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author cj
 * @since 2023-07-09
 */
@TableName("alarm")
public class AlarmEntity extends CJBaseColmns {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String id;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("alarm_date")
    private LocalDateTime alarmDate;

    @TableField("alarm_type_name")
    private String alarmTypeName;

    @TableField("alarm_position")
    private String alarmPosition;

    @TableField("node_code")
    private String nodeCode;

    @TableField("`column_name`")
    private Integer columnName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public LocalDateTime getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(LocalDateTime alarmDate) {
        this.alarmDate = alarmDate;
    }
    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }
    public String getAlarmPosition() {
        return alarmPosition;
    }

    public void setAlarmPosition(String alarmPosition) {
        this.alarmPosition = alarmPosition;
    }
    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }
    public Integer getColumnName() {
        return columnName;
    }

    public void setColumnName(Integer columnName) {
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return "AlarmEntity{" +
            "id=" + id +
            ", alarmDate=" + alarmDate +
            ", alarmTypeName=" + alarmTypeName +
            ", alarmPosition=" + alarmPosition +
            ", nodeCode=" + nodeCode +
            ", columnName=" + columnName +
        "}";
    }
}
