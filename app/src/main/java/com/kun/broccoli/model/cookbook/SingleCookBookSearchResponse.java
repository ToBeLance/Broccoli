package com.kun.broccoli.model.cookbook;

public class SingleCookBookSearchResponse {
    private String code;//返回码
    private String charge;
    private String msg;//返回结果的状态信息
    private SingleCookBookSearchResponseResult result;//返回结果

    public SingleCookBookSearchResponseResult getResult() {
        return result;
    }
}
