package com.chenyqx.myspring;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Mytools {
    public static Set<Class<?>> getClasses(String pack){
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        //是否循环迭代
        boolean recursive = true;
        String packageName = pack;
        String packageDirName = packageName.replace('.','/');
        Enumeration<URL> dirs;
        try{
            dirs =
                    Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while(dirs.hasMoreElements()){
                //获取下一个元素
                URL url = dirs.nextElement();
                //获取协议名称
                String protocol = url.getProtocol();
                if("file".equals(protocol)){
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(),"UTF-8");
                    findClassesInPackageByFile(packageName, filePath, recursive, classes);
                }else if("jar".equals(protocol)){
                    //如果是jar包文件
                    //定义一个jarFile
                    System.out.println("jar类型的扫描");
                    JarFile jar;
                    try{
                        jar = ((JarURLConnection)url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        findClassesInPackageByJar(packageName,entries,packageDirName,recursive,classes);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return classes;
    }

    private static void findClassesInPackageByFile(String packageName,String packagePath,final boolean recursive,Set<Class<?>> classes){
        //获取此包的目录，建立一个file
        File dir = new File(packagePath);
        //如果不存在，也不是一个目录就直接返回
        if(!dir.exists() || !dir.isDirectory()){
            return;
        }
        File[] dirFiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        //循环所有文件
        for(File file : dirFiles){
            if(file.isDirectory()){
                findClassesInPackageByFile(packageName + "." +file.getName(),file.getAbsolutePath(),recursive,classes);
            }else {
                //截掉.class获取类名
                String className = file.getName().substring(0,file.getName().length() - 6);
                try{
                    //添加到集合中
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName +
                            '.' + className));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static void findClassesInPackageByJar(String packageName, Enumeration<JarEntry> entries, String packageDirName, final boolean recursive, Set<Class<?>> classes) {
        while (entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            if(name.charAt(0) == '/'){
                name = name.substring(0);
            }
            //如果前半部分与包名相同
            if(name.startsWith(packageDirName)){
                int idx = name.lastIndexOf('/');
                // 如果以"/"结尾 是一个包
                if(idx != -1){
                    // 获取包名 把"/"替换成".
                    packageName = name.substring(0,idx).replace('/','.');
                }
                //如果可以迭代下去，并且是一个包
                if((idx != -1) || recursive){
                    packageName = packageDirName.substring(0,idx).replace('/','.');
                }
                //如果可以迭代下去并且是一个包
                if((idx != -1) || recursive){
                    //如果是一个.class文件而且不是目录
                    if(name.endsWith(".class") && !entry.isDirectory()){
                        // 去掉后面的".class" 获取真正的类名
                        String className = name.substring(packageName.length() + 1,name.length() - 6);
                        try{
                            // 添加到classes
                            classes.add(Class.forName(packageName + '.' +className));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
