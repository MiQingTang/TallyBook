package com.example.administrator.myapplication.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2015/12/21.
 * item的对应可序化队列属性
 */
public class ChannelItem extends BmobObject implements Serializable {
    private String IMG;
    private String MODELTYPE_ID;
    private int IS_SELECT;
    private int SEQUENCE;
    private String NAME;
    String username;
    private String szType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSzType() {
        return szType;
    }

    public void setSzType(String szType) {
        this.szType = szType;
    }

    public ChannelItem(int i, String tuijian, int i1, int i2, String username) {
        NAME = tuijian;
        this.username = username;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public void setMODELTYPE_ID(String MODELTYPE_ID) {
        this.MODELTYPE_ID = MODELTYPE_ID;
    }

    public void setIS_SELECT(int IS_SELECT) {
        this.IS_SELECT = IS_SELECT;
    }

    public void setSEQUENCE(int SEQUENCE) {
        this.SEQUENCE = SEQUENCE;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getIMG() {
        return IMG;
    }

    public String getMODELTYPE_ID() {
        return MODELTYPE_ID;
    }

    public int getIS_SELECT() {
        return IS_SELECT;
    }

    public int getSEQUENCE() {
        return SEQUENCE;
    }

    public String getNAME() {
        return NAME;
    }

    @Override
    public String toString() {
        return "ChannelItem{" +
                "IMG='" + IMG + '\'' +
                ", MODELTYPE_ID='" + MODELTYPE_ID + '\'' +
                ", IS_SELECT=" + IS_SELECT +
                ", SEQUENCE=" + SEQUENCE +
                ", NAME='" + NAME + '\'' +
                '}';
    }
}
//
//    private static final long serialVersionUID = -6465237897027410019L;
//    //栏目对应的ID
//    public Integer id;
//    //栏目对应的Name
//    public String name;
//    //栏目在整体重的排列顺序
//    public Integer orderId;
//    //栏目是否选中
//    public Integer selected;
//
//    public ChannelItem(Integer id, String name, Integer orderId, Integer selected) {
//        this.id = id;
//        this.name = name;
//        this.orderId = orderId;
//        this.selected = selected;
//    }
//
//    public static long getSerialVersionUID() {
//        return serialVersionUID;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Integer getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(Integer orderId) {
//        this.orderId = orderId;
//    }
//
//    public Integer getSelected() {
//        return selected;
//    }
//
//    public void setSelected(Integer selected) {
//        this.selected = selected;
//    }
