package com.kglory.tms.web.model.dto;

/**
 * @author bomi
 *
 */
public class FrameSizeDto extends SearchDto {

    private long graphItem;

    private int endRowSize;

    private String sortSelect;

    public long getGraphItem() {
        return graphItem;
    }

    public void setGraphItem(long graphItem) {
        this.graphItem = graphItem;
    }

    public int getEndRowSize() {
        return endRowSize;
    }

    public void setEndRowSize(int endRowSize) {
        this.endRowSize = endRowSize;
    }

    public String getSortSelect() {
        return sortSelect;
    }

    public void setSortSelect(String sortSelect) {
        this.sortSelect = sortSelect;
    }
}
