package com.chenyqx.dao;

import java.util.ArrayList;
import java.util.List;

public class HelloDaoImpl2 implements HelloDao {
    @Override
    public List<String> findAll() {
        List list = new ArrayList<String>();
        list.add("yqx");
        list.add("hello");
        return list;
    }
}
