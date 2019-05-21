package com.word.dto;

/*********************************************************
 * 文件名称： BeanProp.java
 * 系统名称： 区块链系统 V1.0
 * 模块名称： com.word.dto
 * 功能说明： 出入参对象
 * 开发人员： xuym26145
 * 开发时间： 2019/5/20 19:41
 * 修改记录： 程序版本 修改日期 修改人员 修改单号 修改说明
 *********************************************************/
public class BeanProp {
    /**
     * 参数名
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 数据类型
     */
    private String type;
    /**
     *  参数是否是对象,是就存对象名
     */
    private String IBean;
    /**
     * 是否必输
     */
    private Boolean required;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getIBean() {
        return IBean;
    }

    public void setIBean(String IBean) {
        this.IBean = IBean;
    }
}
