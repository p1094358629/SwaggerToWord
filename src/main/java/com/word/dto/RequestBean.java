package com.word.dto;

import java.util.List;

/*********************************************************
 * 文件名称： RequestBean.java
 * 系统名称： 区块链系统 V1.0
 * 模块名称： com.word.dto
 * 功能说明： 请求入参中的对象
 * 开发人员： xuym26145
 * 开发时间： 2019/5/20 18:03
 * 修改记录： 程序版本 修改日期 修改人员 修改单号 修改说明
 *********************************************************/
public class RequestBean {
    private String name;
    private List<BeanProp> beanProps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BeanProp> getBeanProps() {
        return beanProps;
    }

    public void setBeanProps(List<BeanProp> beanProps) {
        this.beanProps = beanProps;
    }
}
