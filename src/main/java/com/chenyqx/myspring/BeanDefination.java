package com.chenyqx.myspring;

import lombok.Getter;
import lombok.Setter;


public class BeanDefination {
    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    private String beanName;
    private Class beanClass;

    public BeanDefination(String beanName,Class beanClass){
        this.beanName = beanName;
        this.beanClass = beanClass;
    }
}
