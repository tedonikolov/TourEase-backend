package com.tourease.core.models.custom;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IndexVM<T> {

    private List<T> items;
    private PagerVM<T> pager;

    public IndexVM(Page<T> page) {
        this.items = page.getContent();
        this.pager = new PagerVM<>(page);
    }
}
