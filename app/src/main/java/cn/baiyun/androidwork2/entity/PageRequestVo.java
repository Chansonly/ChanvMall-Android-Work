package cn.baiyun.androidwork2.entity;





public class PageRequestVo {
    private Integer currentPage;
    private Integer pageSize;
    private String condition;

    public PageRequestVo(Integer currentPage, Integer pageSize, String condition) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.condition = condition;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "PageRequestVo{" +
                "currentPage=" + currentPage +
                ", pageSize=" + pageSize +
                ", condition='" + condition + '\'' +
                '}';
    }
}
