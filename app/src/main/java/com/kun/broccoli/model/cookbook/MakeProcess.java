package com.kun.broccoli.model.cookbook;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "make_process")
public class MakeProcess {
    @PrimaryKey
    @NonNull
    private String processid;
    private String recipename;
    private String pcontent;
    private String pic;

    public MakeProcess(@NonNull String processid, String recipename, String pcontent, String pic) {
        this.processid = processid;
        this.recipename = recipename;
        this.pcontent = pcontent;
        this.pic = pic;
    }

    @NonNull
    public String getProcessid() {
        return processid;
    }

    public String getRecipename() {
        return recipename;
    }

    public String getPcontent() {
        return pcontent;
    }

    public String getPic() {
        return pic;
    }

    @Override
    public String toString() {
        return "Process{" +
                "processid='" + processid + '\'' +
                ", recipename='" + recipename + '\'' +
                ", pcontent='" + pcontent + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
