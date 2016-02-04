/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, www.wuyushuo.com All rights reserved. 
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the yinyuetai developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.pager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class Page <T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** data row total */
    private long recordTotal;

    /** each page data number */
    private long offset;

    /** current page number */
    private int size;

    /** page data package is list collection */
    private List<T> items = null;

    /** total page numbers */
    private long pageTotal;

    /** current page is first page or not */
    private boolean isFirstPage = false;

    /** current page is last page or not */
    private boolean isLastPage = false;

    /** default page number is size 10 */
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    public Page() {
        this(new ArrayList<T>(), 0, 1);
    }

    /**
     * construction with parameters
     * @param list
     * @param recordTotal
     * @param offset
     */
    public Page(List<T> list, long recordTotal, long offset) {
        setPage(list, recordTotal, offset, DEFAULT_PAGE_SIZE);
    }

    /**
     * construction with parameters
     * @param list
     * @param recordTotal
     * @param offset
     * @param size
     */
    public Page(List<T> list, long recordTotal, long offset, int size) {
        setPage(list, recordTotal, offset, size);
    }

    /**
     * The core operation
     * @param list
     * @param recordTotal
     * @param offset
     * @param size
     */
    private void setPage(List<T> list, long recordTotal, long offset, int size) {
        long pageCount = (recordTotal - 1) / size + 1;
        if (offset == 1) {
            setFirstPage(true);
        }
        if (offset == pageCount) {
            setLastPage(true);
        }
        this.items = list;
        this.pageTotal = pageCount;
        this.offset = offset;
        this.size = size;
        this.recordTotal = recordTotal;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getTotalPage() {
        if (this.getSize() > 0) {
            return ((recordTotal - 1) / this.getSize() + 1);
        }
        return 0;
    }

    public boolean isNextPage() {
        return ((this.pageTotal > 1) && (this.offset < this.pageTotal));
    }

    public boolean isPrePage() {
        return ((this.pageTotal > 1) && (this.offset < this.pageTotal));
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public long getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public long getOffset() {
        return this.offset;
    }

    public long getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

}
