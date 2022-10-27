package com.kun.broccoli.model.cookbook;

public class SingleCookBookSearchResponseResult {
    private String status;
    private String msg;
    private CookBook result;

    public CookBook getCookBook() {
        return result;
    }
}
