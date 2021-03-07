package com.chenyqx.myspring;

import com.chenyqx.myspring.annotation.Autowired;
import com.chenyqx.myspring.annotation.Component;
import com.chenyqx.myspring.annotation.Qualifier;
import com.chenyqx.myspring.annotation.Value;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MyAnnotationConfigApplicationContext {
    private Map<String,Object> ioc = new HashMap<>();

    public MyAnnotationConfigApplicationContext(String packagePath){
        //遍历包，找到目标类
        Set<BeanDefination> beanDefinations = findBeanDefinations(packagePath);
        //根据原材料创建bean
        createObject(beanDefinations);
        //自动装载
        autowiredObject(beanDefinations);
    }

    public void autowiredObject(Set<BeanDefination> beanDefinations){
        Iterator<BeanDefination> iterator = beanDefinations.iterator();
        while(iterator.hasNext()){
            BeanDefination beanDefination = iterator.next();
            Class clazz = beanDefination.getBeanClass();
            Field[] declaredFields= clazz.getDeclaredFields();
            for(Field declaredField:declaredFields){
                Autowired annotation = declaredField.getAnnotation(Autowired.class);
                if(annotation !=null){
                    Qualifier qualifier = declaredField.getAnnotation(Qualifier.class);
                    if(qualifier != null){
                        //byName
                        try {
                            String beanName = qualifier.value();
                            Object bean = getBean(beanName);
                            String fieldName = declaredField.getName();
                            String methodName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
                            Method method = clazz.getMethod(methodName, declaredField.getType());
                            Object object = getBean(beanDefination.getBeanName());
                            method.invoke(object, bean);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }else {
                        //byType
                        try {
                            String typeName = declaredField.getType().getName().replaceAll(declaredField.getType().getPackage().getName()+".","");
                            String beanName = typeName.substring(0,1).toLowerCase() + typeName.substring(1);
                            Object bean = getBean(beanName);
                            String fieldName = declaredField.getName();
                            String methodName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
                            Method method = clazz.getMethod(methodName, declaredField.getType());
                            Object object = getBean(beanDefination.getBeanName());
                            method.invoke(object, bean);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void createObject(Set<BeanDefination> beanDefinations){
        Iterator<BeanDefination> iterator = beanDefinations.iterator();
        while(iterator.hasNext()){
            BeanDefination beanDefination = iterator.next();
            Class clazz = beanDefination.getBeanClass();
            String beanName = beanDefination.getBeanName();
            try{
                //创建对象
                Object object = clazz.getConstructor().newInstance();
                //对象赋值
                Field[] fields = clazz.getDeclaredFields();
                for(Field field:fields){
                    Value valueAnnotation = field.getAnnotation(Value.class);
                    if(valueAnnotation != null){
                        String value = valueAnnotation.value();
                        String fieldName = field.getName();
                        String methodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
                        Method method = clazz.getMethod(methodName,field.getType());
                        //完成数据类型转换
                        Object var = null;
                        switch(field.getType().getName()){
                            case "java.lang.Integer":
                                var = Integer.parseInt(value);
                                break;
                            case "java.lang.String":
                                var = value;
                                break;
                            case "java.lang.Float":
                                var = Float.parseFloat(value);
                                break;
                            default:
                                break;
                        }
                        method.invoke(object,var);
                    }
                }
                ioc.put(beanName,object);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public Object getBean(String beanName){
        return ioc.get(beanName);
    }

    public Set<BeanDefination> findBeanDefinations(String packagePath){
        Set<BeanDefination> beanDefinations = new HashSet<>();
        //1.获取包下所有类
        Set<Class<?>> classes = Mytools.getClasses(packagePath);
        Iterator<Class<?>> iterator = classes.iterator();
        while(iterator.hasNext()){
            //2.找到添加了注解的类
            Class<?> clazz = iterator.next();
            Component component = clazz.getAnnotation(Component.class);
            if(component != null){
                String beanName = component.value();
                if("".equals(beanName)){
                    String className = clazz.getName().replaceAll(clazz.getPackage().getName()+".","");
                    beanName = className.substring(0,1).toLowerCase() + className.substring(1);
                }
                BeanDefination beanDefination = new BeanDefination(beanName,clazz);
                //3.将类封装成BeanDefination
                beanDefinations.add(beanDefination);
            }
        }
        return beanDefinations;
    }
}
