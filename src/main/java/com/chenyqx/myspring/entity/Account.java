package com.chenyqx.myspring.entity;

import com.chenyqx.myspring.annotation.Autowired;
import com.chenyqx.myspring.annotation.Component;
import com.chenyqx.myspring.annotation.Qualifier;
import com.chenyqx.myspring.annotation.Value;
import lombok.Data;

@Data
@Component
public class Account {
    @Value("1")
    private String id;
    @Value("张三")
    private String name;
    @Value("22")
    private Integer age;
    @Autowired
//    @Qualifier("myOrder")
    private Order myorder;
}
