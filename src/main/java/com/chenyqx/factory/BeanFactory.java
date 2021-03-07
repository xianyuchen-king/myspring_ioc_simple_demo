package com.chenyqx.factory;

import com.chenyqx.dao.HelloDao;
import com.chenyqx.dao.HelloDaoImpl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class BeanFactory {
    private static Properties properties;
    private static HashMap<String,Object> cacheMap = new HashMap<>();

    static {
        properties = new Properties();
        try {
            properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("factory.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object getDao(String beanName){
        if(!cacheMap.containsKey(beanName)) {
            synchronized (BeanFactory.class) {
                if(!cacheMap.containsKey(beanName)) {
                    String value = properties.getProperty(beanName);
                    //通过反射创建对象
                    try {
                        Class clazz = Class.forName(value);
                        Object obj = clazz.getConstructor(null).newInstance(null);
                        cacheMap.put(beanName, obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return cacheMap.get(beanName);
    }
}
