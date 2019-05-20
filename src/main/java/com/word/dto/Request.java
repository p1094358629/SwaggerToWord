package com.word.dto;

/**
 * Created by XiuYin.Cui on 2018/1/11.
 */
public class Request {

    /**
     * 参数名
     */
    private String name;

    /**
     * 数据类型
     */
    private String type;

    /**
     * 数据是否是对象,是就存对象名
     */
    private String  IBean;

    /**
     * 是否必填
     */
    private Boolean require;

    /**
     * 说明
     */
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequire() {
        return require;
    }

    public void setRequire(Boolean require) {
        this.require = require;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIBean() {
        return IBean;
    }

    public void setIBean(String IBean) {
        this.IBean = IBean;
    }
}
