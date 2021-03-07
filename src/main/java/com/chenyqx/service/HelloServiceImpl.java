package com.chenyqx.service;

import com.chenyqx.dao.HelloDao;
import com.chenyqx.dao.HelloDaoImpl;
import com.chenyqx.factory.BeanFactory;

import java.util.List;

public class HelloServiceImpl implements HelloService{
    private HelloDao helloDao = (HelloDao) BeanFactory.getDao("helloDao");
    @Override
    public List<String> findAll() {
        return helloDao.findAll();
    }
}
