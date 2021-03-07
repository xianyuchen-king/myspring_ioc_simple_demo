package com.chenyqx.myspring.entity;

import com.chenyqx.myspring.annotation.Component;
import com.chenyqx.myspring.annotation.Value;
import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;

@Data
@Component
public class Order {
    @Value("xxx123")
    private String orderId;
    @Value("1000.0")
    private Float price;
}
