package com.nanshan.icc.org;

import com.dahuatech.icc.oauth.http.IccResponse;

import java.util.List;

/**
 * 设备分页查询结果
 *
 * @author 232676
 * @since 1.0.0 2020-11-03 09:59:11
 */
public class CJOrgPageResponse extends IccResponse {
  private CJOrgPageData data;

  public CJOrgPageData getData() {
    return data;
  }

  public void setData(CJOrgPageData data) {
    this.data = data;
  }

  public static class CJOrgPageData {
    private List<CJOrgInfo> pageData;
    private int currentPage;
    private int totalPage;
    private int pageSize;
    private int totalRows;


    public List<CJOrgInfo> getPageData() {
      return pageData;
    }

    public void setPageData(List<CJOrgInfo> pageData) {
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


    @Override
    public String toString() {
      return "CJAlarmlPageData{" +
              ", pageData=" + pageData +
              ", currentPage=" + currentPage +
              ", totalPage=" + totalPage +
              ", pageSize=" + pageSize +
              ", totalRows=" + totalRows +
              '}';
    }
  }

  public static class CJOrgInfo {
    private String orgCode;
    private String orgName;
    private String orgSn;
    private Integer stat;

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

    public String getOrgSn() {
      return orgSn;
    }

    public void setOrgSn(String orgSn) {
      this.orgSn = orgSn;
    }

    public Integer getStat() {
      return stat;
    }

    public void setStat(Integer stat) {
      this.stat = stat;
    }

    @Override
    public String toString() {
      return "CJOrgInfo{" +
              "orgCode='" + orgCode + '\'' +
              ", orgName='" + orgName + '\'' +
              ", orgSn='" + orgSn + '\'' +
              ", stat=" + stat +
              '}';
    }
  }

}
