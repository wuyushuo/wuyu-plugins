/*--------------------------------------------------------------------------
 *  Copyright (c) 2009-2020, www.wuyushuo.com All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the wuyushuo developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class ClassUtil {

    private ClassUtil() {
        // 静态类不可实例化
    }

    /**
     * 查找方法
     * @param clazz 类
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return 方法
     */
    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        }
        catch (NoSuchMethodException ex) {
            return findDeclaredMethod(clazz, methodName, paramTypes);
        }
    }

    /**
     * 查找所有方法
     * @param clazz 类
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @return Method
     */
    public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        }
        catch (NoSuchMethodException ex) {
            if (clazz.getSuperclass() != null) {
                return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
            }
            return null;
        }
    }

    /**
     * 获得对象数组的类数组
     * @param objects 对象数组
     * @return 类数组
     */
    public static Class<?>[] getClasses(Object... objects){
        Class<?>[] classes = new Class<?>[objects.length];
        for (int i = 0; i < objects.length; i++) {
            classes[i] = objects[i].getClass();
        }
        return classes;
    }

    /**
     * 扫面该包路径下所有class文件
     *
     * @return 类集合
     */
    public static Set<Class<?>> scanPackage() {
        return scanPackage(StringUtil.EMPTY, null);
    }

    /**
     * 扫面该包路径下所有class文件
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @return 类集合
     */
    public static Set<Class<?>> scanPackage(String packageName) {
        return scanPackage(packageName, null);
    }

    /**
     * 扫描指定包路径下所有包含指定注解的类
     * @param packageName 包路径
     * @param annotationClass 注解类
     * @return 类集合
     */
    public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
        return scanPackage(packageName, new ClassFilter() {
            @Override
            public boolean accept(Class<?> clazz) {
                return clazz.isAnnotationPresent(annotationClass);
            }
        });
    }

    /**
     * 扫描指定包路径下所有指定类的子类
     * @param packageName 包路径
     * @param superClass 父类
     * @return 类集合
     */
    public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
        return scanPackage(packageName, new ClassFilter() {
            @Override
            public boolean accept(Class<?> clazz) {
                return superClass.isAssignableFrom(clazz) && !superClass.equals(clazz);
            }
        });
    }

    /**
     * 扫面包路径下满足class过滤器条件的所有class文件，</br>
     * 如果包路径为 com.abs + A.class 但是输入 abs会产生classNotFoundException</br>
     * 因为className 应该为 com.abs.A 现在却成为abs.A,此工具类对该异常进行忽略处理,有可能是一个不完善的地方，以后需要进行修改</br>
     *
     * @param packageName 包路径 com | com. | com.abs | com.abs.
     * @param classFilter class过滤器，过滤掉不需要的class
     * @return 类集合
     */
    public static Set<Class<?>> scanPackage(String packageName, ClassFilter classFilter) {
        if(StringUtil.isBlank(packageName)) {
            packageName = StringUtil.EMPTY;
        }
        packageName = getWellFormedPackageName(packageName);

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String classPath : getClassPaths(packageName)) {
            //bug修复，由于路径中空格和中文导致的Jar找不到
            classPath = URLUtil.decode(classPath, CharsetUtil.systemCharset());

            // 填充 classes
            fillClasses(classPath, packageName, classFilter, classes);
        }

        //如果在项目的ClassPath中未找到，去系统定义的ClassPath里找
        if(classes.isEmpty()) {
            for (String classPath : getJavaClassPaths()) {
                //bug修复，由于路径中空格和中文导致的Jar找不到
                classPath = URLUtil.decode(classPath, CharsetUtil.systemCharset());

                // 填充 classes
                fillClasses(classPath, new File(classPath), packageName, classFilter, classes);
            }
        }
        return classes;
    }

    /**
     * 获得指定类中的Public方法名<br>
     * 去重重载的方法
     * @param clazz 类
     */
    public final static Set<String> getMethods(Class<?> clazz) {
        HashSet<String> methodSet = new HashSet<String>();
        Method[] methodArray = clazz.getMethods();
        for (Method method : methodArray) {
            String methodName = method.getName();
            methodSet.add(methodName);
        }
        return methodSet;
    }

    /**
     * 获得ClassPath
     * @return ClassPath集合
     */
    public static Set<String> getClassPathResources(){
        return getClassPaths(StringUtil.EMPTY);
    }

    /**
     * 获得ClassPath
     * @param packageName 包名称
     * @return ClassPath路径字符串集合
     */
    public static Set<String> getClassPaths(String packageName){
        String packagePath = packageName.replace(StringUtil.DOT, StringUtil.SLASH);
        Enumeration<URL> resources;
        try {
            resources = getClassLoader().getResources(packagePath);
        } catch (IOException e) {
            throw new RuntimeException("Loading classPath " + packagePath + " error!", e);
        }
        Set<String> paths = new HashSet<String>();
        while(resources.hasMoreElements()) {
            paths.add(resources.nextElement().getPath());
        }
        return paths;
    }

    /**
     * @return 获得Java ClassPath路径，不包括 jre
     */
    public static String[] getJavaClassPaths() {
        String[] classPaths = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        return classPaths;
    }

    /**
     * 转换基本类型
     * @param clazz 被转换为基本类型的类，必须为包装类型
     * @return 基本类型类
     */
    public static Class<?> castToPrimitive(Class<?> clazz) {
        if(null == clazz || clazz.isPrimitive()) {
            return clazz;
        }

        BasicType basicType;
        try {
            basicType = BasicType.valueOf(clazz.getSimpleName().toUpperCase());
        }catch(Exception e) {
            return clazz;
        }

        //基本类型
        switch (basicType) {
            case BYTE:
                return byte.class;
            case SHORT:
                return short.class;
            case INTEGER:
                return int.class;
            case LONG:
                return long.class;
            case DOUBLE:
                return double.class;
            case FLOAT:
                return float.class;
            case BOOLEAN:
                return boolean.class;
            case CHAR:
                return char.class;
            default:
                return clazz;
        }
    }

    public enum BasicType {
        BYTE, SHORT, INT, INTEGER, LONG, DOUBLE, FLOAT, BOOLEAN, CHAR, CHARACTER, STRING;
    }

    /**
     * @return 当前线程的class loader
     */
    public static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获得class loader<br>
     * 若当前线程class loader不存在，取当前类的class loader
     * @return 类加载器
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if(classLoader == null) {
            classLoader = ClassUtil.class.getClassLoader();
        }
        return classLoader;
    }

    /**
     * 实例化对象
     * @param clazz 类名
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String clazz) {
        try {
            return (T) Class.forName(clazz).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Instance class " + clazz + " error!");
        }
    }

    /**
     * 实例化对象
     * @param clazz 类
     * @return 对象
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Instance class " + clazz + " error!");
        }
    }

    /**
     * 实例化对象
     * @param clazz 类
     * @return 对象
     */
    public static <T> T newInstance(Class<T> clazz, Object... params) {
        if(null == params || 0 == params.length){
            return newInstance(clazz);
        }

        try {
            return clazz.getDeclaredConstructor(getClasses(params)).newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException("Instance class " + clazz + " error!", e);
        }
    }

    /**
     * 加载类
     * @param <T>
     * @param className 类名
     * @param isInitialized 是否初始化
     * @return 类
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> loadClass(String className, boolean isInitialized) {
        Class<T> clazz;
        try {
            clazz = (Class<T>) Class.forName(className, isInitialized, getClassLoader());
        }catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getLocalizedMessage(),e);
        }
        return clazz;
    }

    /**
     * 加载类并初始化
     * @param <T>
     * @param className 类名
     * @return 类
     */
    public static <T> Class<T> loadClass(String className) {
        return loadClass(className, true);
    }


    /**
     * 执行方法<br>
     * 可执行Private方法，也可执行static方法<br>
     * 执行非static方法时，必须满足对象有默认构造方法<br>
     * @param <T>
     * @param className 类名，完整类路径
     * @param methodName 方法名
     * @param args 参数，必须严格对应指定方法的参数类型和数量
     * @return 返回结果
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(String className, String methodName,Object... args){
        Class<Object> clazz = loadClass(className);
        try {
            Method method = clazz.getDeclaredMethod(methodName, getClasses(args));
            int modifiers = method.getModifiers();
            if(Modifier.isPrivate(modifiers)){
                method.setAccessible(true);
            }
            if(Modifier.isStatic(modifiers)){
                return (T) method.invoke(null, args);
            }else{
                return (T) method.invoke(clazz.newInstance(), args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 新建代理对象<br>
     * 动态代理类对象用于动态创建一个代理对象，可以在调用接口方法的时候动态执行相应逻辑
     * @param interfaceClass 被代理接口
     * @param invocationHandler 代理执行类，此类用于实现具体的接口方法
     * @return 被代理接口
     */
    @SuppressWarnings("unchecked")
    public static <T> T newProxyInstance(Class<T> interfaceClass, InvocationHandler invocationHandler){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, invocationHandler);
    }

    //--------------------------------------------------------------------------------------------------- Private method start
    /**
     * 文件过滤器，过滤掉不需要的文件<br>
     * 只保留Class文件、目录和Jar
     */
    private static FileFilter fileFilter = new FileFilter(){
        @Override
        public boolean accept(File pathname) {
            return isClass(pathname.getName()) || pathname.isDirectory() || isJarFile(pathname);
        }
    };

    /**
     * 改变 com -> com. 避免在比较的时候把比如 completeTestSuite.class类扫描进去，如果没有"."</br>
     * 那class里面com开头的class类也会被扫描进去,其实名称后面或前面需要一个 ".",来添加包的特征
     *
     * @param packageName
     * @return 格式化后的包名
     */
    private static String getWellFormedPackageName(String packageName) {
        return packageName.lastIndexOf(StringUtil.DOT) != packageName.length() - 1 ? packageName + StringUtil.DOT : packageName;
    }

    /**
     * 填充满足条件的class 填充到 classes<br>
     * 同时会判断给定的路径是否为Jar包内的路径，如果是，则扫描此Jar包
     *
     * @param path Class文件路径或者所在目录Jar包路径
     * @param packageName 需要扫面的包名
     * @param classFilter class过滤器
     * @param classes List 集合
     */
    private static void fillClasses(String path, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
        //判定给定的路径是否为Jar
        int index = path.lastIndexOf(FileUtil.JAR_PATH_EXT);
        if(index != -1) {
            //Jar文件
            path = path.substring(0, index + FileUtil.JAR_FILE_EXT.length());	//截取jar路径
            path = StringUtil.removePrefix(path, FileUtil.PATH_FILE_PRE);	//去掉文件前缀
            processJarFile(new File(path), packageName, classFilter, classes);
        }else {
            fillClasses(path, new File(path), packageName, classFilter, classes);
        }
    }

    /**
     * 填充满足条件的class 填充到 classes
     *
     * @param classPath 类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
     * @param file Class文件或者所在目录Jar包文件
     * @param packageName 需要扫面的包名
     * @param classFilter class过滤器
     * @param classes List 集合
     */
    private static void fillClasses(String classPath, File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
        if (file.isDirectory()) {
            processDirectory(classPath, file, packageName, classFilter, classes);
        } else if (isClassFile(file)) {
            processClassFile(classPath, file, packageName, classFilter, classes);
        } else if (isJarFile(file)) {
            processJarFile(file, packageName, classFilter, classes);
        }
    }

    /**
     * 处理如果为目录的情况,需要递归调用 fillClasses方法
     *
     * @param directory 目录
     * @param packageName 包名
     * @param classFilter 类过滤器
     * @param classes 类集合
     */
    private static void processDirectory(String classPath, File directory, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
        for (File file : directory.listFiles(fileFilter)) {
            fillClasses(classPath, file, packageName, classFilter, classes);
        }
    }

    /**
     * 处理为class文件的情况,填充满足条件的class 到 classes
     *
     * @param classPath 类文件所在目录，当包名为空时使用此参数，用于截掉类名前面的文件路径
     * @param file class文件
     * @param packageName 包名
     * @param classFilter 类过滤器
     * @param classes 类集合
     */
    private static void processClassFile(String classPath, File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
        if(false == classPath.endsWith(File.separator)) {
            classPath += File.separator;
        }
        String path = file.getAbsolutePath();
        if(StringUtil.isEmpty(packageName)) {
            path = StringUtil.removePrefix(path, classPath);
        }
        final String filePathWithDot = path.replace(File.separator, StringUtil.DOT);

        int subIndex = -1;
        if ((subIndex = filePathWithDot.indexOf(packageName)) != -1) {
            final int endIndex = filePathWithDot.lastIndexOf(FileUtil.CLASS_EXT);

            final String className = filePathWithDot.substring(subIndex, endIndex);
            fillClass(className, packageName, classes, classFilter);
        }
    }

    /**
     * 处理为jar文件的情况，填充满足条件的class 到 classes
     *
     * @param file jar文件
     * @param packageName 包名
     * @param classFilter 类过滤器
     * @param classes 类集合
     */
    private static void processJarFile(File file, String packageName, ClassFilter classFilter, Set<Class<?>> classes) {
        try {
            for (JarEntry entry : Collections.list(new JarFile(file).entries())) {
                if (isClass(entry.getName())) {
                    final String className = entry.getName().replace(StringUtil.SLASH, StringUtil.DOT).replace(FileUtil.CLASS_EXT, StringUtil.EMPTY);
                    fillClass(className, packageName, classes, classFilter);
                }
            }
        } catch (Throwable ex) {
            ;
        }
    }

    /**
     * 填充class 到 classes
     *
     * @param className 类名
     * @param packageName 包名
     * @param classes 类集合
     * @param classFilter 类过滤器
     */
    private static void fillClass(String className, String packageName, Set<Class<?>> classes, ClassFilter classFilter) {
        if (className.startsWith(packageName)) {
            try {
                final Class<?> clazz = Class.forName(className, false, getClassLoader());
                if (classFilter == null || classFilter.accept(clazz)) {
                    classes.add(clazz);
                }
            } catch (Throwable ex) {
                //Log.error(log, ex, "Load class [{}] error!", className);
                //Pass Load Error.
            }
        }
    }

    /**
     * @param file 文件
     * @return 是否为类文件
     */
    private static boolean isClassFile(File file) {
        return isClass(file.getName());
    }

    /**
     * @param fileName 文件名
     * @return 是否为类文件
     */
    private static boolean isClass(String fileName) {
        return fileName.endsWith(FileUtil.CLASS_EXT);
    }

    /**
     * @param file 文件
     * @return是否为Jar文件
     */
    private static boolean isJarFile(File file) {
        return file.getName().endsWith(FileUtil.JAR_FILE_EXT);
    }
    //--------------------------------------------------------------------------------------------------- Private method end

    /**
     * 类过滤器，用于过滤不需要加载的类<br>
     */
    public interface ClassFilter {
        boolean accept(Class<?> clazz);
    }
}
