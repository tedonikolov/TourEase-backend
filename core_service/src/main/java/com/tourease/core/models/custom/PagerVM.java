package com.tourease.core.models.custom;

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
    private long totalCount;

    public PagerVM(Page<T> page) {
        this.page = page.getNumber()+1;
        this.pagesCount = page.getTotalPages();
        this.totalCount = page.getTotalElements();
    }

}
