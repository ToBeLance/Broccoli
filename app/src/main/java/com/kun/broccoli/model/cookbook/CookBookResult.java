package com.kun.broccoli.model.cookbook;

import java.util.List;

public class CookBookResult {
    private String num;//返回的菜谱数量
    private List<CookBook> list;//返回的菜谱列表

    public List<CookBook> getList() {
        return list;
    }
}
