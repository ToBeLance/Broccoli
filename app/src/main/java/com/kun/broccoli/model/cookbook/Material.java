package com.kun.broccoli.model.cookbook;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "material")
public class Material {
    // mname: 材料名，type： 0 辅料，1 主料，amount :用量
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String materialid;
    private String recipename;
    private String mname;
    private String amount;

    public Material(@NonNull String materialid, String recipename, String mname, String amount) {
        this.materialid = materialid;
        this.recipename = recipename;
        this.mname = mname;
        this.amount = amount;
    }

    @NonNull
    public String getMaterialid() {
        return materialid;
    }

    public String getRecipename() {
        return recipename;
    }

    public String getMname() {
        return mname;
    }

    public String getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Material{" +
                "materialid='" + materialid + '\'' +
                ", recipename='" + recipename + '\'' +
                ", mname='" + mname + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
