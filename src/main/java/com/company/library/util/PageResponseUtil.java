package com.company.library.util;

import org.springframework.data.domain.Page;

public class PageResponseUtil {

    private PageResponseUtil() {
    }

    public static <T> Object getPageResponse(Page<T> page) {
        return new Object() {
            public final int totalPages = page.getTotalPages();
            public final long totalElements = page.getTotalElements();
            public final int currentPage = page.getNumber();
            public final int pageSize = page.getSize();
            public final java.util.List<T> content = page.getContent();
            public final boolean isLast = page.isLast();
            public final boolean isEmpty = page.isEmpty();
        };
    }
}
