package com.nanshan.icc.alarm;

import com.dahuatech.icc.oauth.http.IccResponse;

import java.util.List;

/**
 * 设备分页查询结果
 *
 * @author 232676
 * @since 1.0.0 2020-11-03 09:59:11
 */
public class CJAlarmPageResponse extends IccResponse {
  private CJAlarmlPageData data;

  public CJAlarmlPageData getData() {
    return data;
  }

  public void setData(CJAlarmlPageData data) {
    this.data = data;
  }

  public static class CJAlarmlPage {
    private Long id;
    private String alarmDate;
    private String alarmPosition;
    private String alarmTypeName;
    private String nodeCode;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getAlarmDate() {
      return alarmDate;
    }

    public void setAlarmDate(String alarmDate) {
      this.alarmDate = alarmDate;
    }

    public String getAlarmPosition() {
      return alarmPosition;
    }

    public void setAlarmPosition(String alarmPosition) {
      this.alarmPosition = alarmPosition;
    }

    public String getAlarmTypeName() {
      return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
      this.alarmTypeName = alarmTypeName;
    }

    public String getNodeCode() {
      return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
      this.nodeCode = nodeCode;
    }

    @Override
    public String toString() {
      return "CJAlarmlPage{" +
              "id=" + id +
              ", alarmDate='" + alarmDate + '\'' +
              ", alarmPosition='" + alarmPosition + '\'' +
              ", alarmTypeName='" + alarmTypeName + '\'' +
              ", nodeCode='" + nodeCode + '\'' +
              '}';
    }
  }
  public static class CJAlarmlPageData {
    private int value;
    private List<CJAlarmlPage> pageData;
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private int totalRows;

    @Override
    public String toString() {
      return "CJAlarmlPageData{" +
              "value=" + value +
              ", pageData=" + pageData +
              ", currentPage=" + currentPage +
              ", totalPage=" + totalPage +
              ", pageSize=" + pageSize +
              ", totalRows=" + totalRows +
              '}';
    }

    public int getValue() {
      return value;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public List<CJAlarmlPage> getPageData() {
      return pageData;
    }

    public void setPageData(List<CJAlarmlPage> pageData) {
      this.pageData = pageData;
    }

    public int getCurrentPage() {
      return currentPage;
    }

    public void setCurrentPage(int currentPage) {
      this.currentPage = currentPage;
    }

    public int getTotalPage() {
      return totalPage;
    }

    public void setTotalPage(int totalPage) {
      this.totalPage = totalPage;
    }

    public int getPageSize() {
      return pageSize;
    }

    public void setPageSize(int pageSize) {
      this.pageSize = pageSize;
    }

    public int getTotalRows() {
      return totalRows;
    }

    public void setTotalRows(int totalRows) {
      this.totalRows = totalRows;
    }
  }
}
