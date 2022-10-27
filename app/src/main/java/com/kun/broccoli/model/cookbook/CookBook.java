package com.kun.broccoli.model.cookbook;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "cookBook")
public class CookBook {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String id;//该菜谱id
    private String classid;//该菜谱的分类id
    private String name;//该菜谱的名字
    private String peoplenum;//该菜谱的食用人数
    private String preparetime;//该菜谱准备食材的使用时间
    private String cookingtime;//该菜谱烹饪食材的时间
    private String content;//该菜谱的描述
    private String pic;//图片
    private String tag;//表签
    @Ignore
    private List<Material> material;//材料
    @Ignore
    private List<MakeProcess> process;//烹饪步骤

    public CookBook(String id, String classid, String name, String peoplenum, String preparetime, String cookingtime, String content, String pic, String tag) {
        this.id = id;
        this.classid = classid;
        this.name = name;
        this.peoplenum = peoplenum;
        this.preparetime = preparetime;
        this.cookingtime = cookingtime;
        this.content = content;
        this.pic = pic;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public String getClassid() {
        return classid;
    }

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

    public List<Material> getMaterial() {
        return material;
    }

    public List<MakeProcess> getProcess() {
        return process;
    }

    @Override
    public String toString() {
        return "CookBook{" +
                "id='" + id + '\'' +
                ", classid='" + classid + '\'' +
                ", name='" + name + '\'' +
                ", peoplenum='" + peoplenum + '\'' +
                ", preparetime='" + preparetime + '\'' +
                ", cookingtime='" + cookingtime + '\'' +
                ", content='" + content + '\'' +
                ", pic='" + pic + '\'' +
                ", tag='" + tag + '\'' +
                ", material=" + material +
                ", process=" + process +
                '}';
    }
}
