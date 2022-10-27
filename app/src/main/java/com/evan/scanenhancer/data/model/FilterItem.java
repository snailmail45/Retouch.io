package com.evan.scanenhancer.data.model;

import com.evan.scanenhancer.util.Filter;

public class FilterItem {
    private final String name;
    private final int preview;
    private final Filter filter;

    public FilterItem(String name, int preview, Filter filter) {
        this.name = name;
        this.preview = preview;
        this.filter = filter;
    }

    public String getName() {
        return name;
    }

    public int getPreview() {
        return preview;
    }

    public Filter getFilter() {
        return filter;
    }
}
