package com.kun.broccoli.model.cookbook;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe")
public class Recipe {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String name;//该菜谱的名字
    private String peoplenum;//该菜谱的食用人数
    private String preparetime;//该菜谱准备食材的使用时间
    private String cookingtime;//该菜谱烹饪食材的时间
    private String content;//该菜谱的描述
    private String pic;//图片
    private String tag;//表签

    public Recipe(@NonNull String name, String peoplenum, String preparetime, String cookingtime, String content, String pic, String tag) {
        this.name = name;
        this.peoplenum = peoplenum;
        this.preparetime = preparetime;
        this.cookingtime = cookingtime;
        this.content = content;
        this.pic = pic;
        this.tag = tag;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getPeoplenum() {
        return peoplenum;
    }

    public String getPreparetime() {
        return preparetime;
    }

    public String getCookingtime() {
        return cookingtime;
    }

    public String getContent() {
        return content;
    }

    public String getPic() {
        return pic;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "name='" + name + '\'' +
                ", peoplenum='" + peoplenum + '\'' +
                ", preparetime='" + preparetime + '\'' +
                ", cookingtime='" + cookingtime + '\'' +
                ", content='" + content + '\'' +
                ", pic='" + pic + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
