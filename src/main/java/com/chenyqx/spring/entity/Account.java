package com.chenyqx.spring.entity;


import com.chenyqx.myspring.entity.Order;
import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;

@Data
//@Component
public class Account {
//    @Value("1")
    private String id;
//    @Value("张三")
    private String name;
//    @Value("22")
    private Integer age;
//    @Autowired
    private Order order;
}
