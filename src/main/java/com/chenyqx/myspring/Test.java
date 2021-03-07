package com.chenyqx.myspring;

public class Test {
    public static void main(String[] args) {
        MyAnnotationConfigApplicationContext annotationConfigApplicationContext =
                new MyAnnotationConfigApplicationContext("com.chenyqx.myspring.entity");
        System.out.println(annotationConfigApplicationContext.getBean("account"));
        System.out.println(annotationConfigApplicationContext.getBean("order"));
    }
}
