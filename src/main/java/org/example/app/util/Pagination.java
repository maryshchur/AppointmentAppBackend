package org.example.app.util;

public class Pagination {
    public static Integer validatePage(Integer page) {
        return page <= 0 ? 0 : page - 1;
    }

    public static Integer validatePageSize(Integer pageSize) {
        return pageSize <= 0 ? 1 : pageSize;
    }

}
