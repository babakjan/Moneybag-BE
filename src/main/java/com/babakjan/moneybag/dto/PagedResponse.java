package com.babakjan.moneybag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    private List<T> items;

    private Integer page; //page index starting from 0

    private Integer size; //items per page

    private Long totalElements; //total elements in all pages

    private Integer pageCount; //number of pages
}
