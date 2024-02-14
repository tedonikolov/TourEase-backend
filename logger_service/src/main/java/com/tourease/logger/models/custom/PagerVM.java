package com.tourease.logger.models.custom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;


@Getter
@Setter
@NoArgsConstructor
public class PagerVM<T> {

    private int page ;
    private int pagesCount ;
    private int itemsPerPage ;
    private long totalCount;
    private long itemsCount;

    public PagerVM(Page<T> page) {
        this.page = page.getNumber() + 1;
        this.pagesCount = page.getTotalPages();
        this.itemsPerPage = page.getPageable().getPageSize();
        this.totalCount = page.getTotalElements();
        this.itemsCount = page.getNumberOfElements();
    }

}
