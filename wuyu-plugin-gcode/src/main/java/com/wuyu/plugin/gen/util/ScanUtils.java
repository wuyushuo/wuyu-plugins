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
 * Neither the name of the yinyuetai developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: git_wuyu@163.com (tencent qq: 2094998377)
 *--------------------------------------------------------------------------
*/
package com.wuyu.plugin.gen.util;


import com.wuyu.plugin.utils.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class ScanUtils {

    private ScanUtils(){
        throw new UnsupportedOperationException();
    }


    private static final Logger LOG = LoggerFactory.getLogger(ScanUtils.class);

    /**
     * 递归扫描生成器依赖的实体类
     * <pre>
     * 例如:
     *      实体全路径 E:/project/src/main/java/com/wuyu/domain/*.java
     *      包目录路径 com.wuyu.domain
     *      代码根路径 E:/project/src/main/java/ 这里的代码根路径从当前工程位置扫描
     * </pre>
     * @param srcPackage 项目工程中的包路径
     * @return  返回包名+类名的整合串(package + class)
     *      Eg:
     *          com.dobby.domain.Test.java
     *          com.dobby.domain.sub.InTest.java
     */
    public static List<String> scanPackageClasses(String srcPackage){
        return scanPackageClasses(srcPackage,true);
    }


    /**
     * 递归扫描生成器依赖的实体类
     * <pre>
     * 例如:
     *      实体全路径 E:/project/src/main/java/com/wuyu/domain/*.java
     *      包目录路径 com.wuyu.domain
     *      代码根路径 E:/project/src/main/java/ 这里的代码根路径从当前工程位置扫描
     * </pre>
     * @param srcPackage 项目工程中的包路径
     * @return  返回包名+类名的整合串(package + class)
     *      Eg:
     *          com.dobby.domain.Test.java
     *          com.dobby.domain.sub.InTest.java
     */
    public static List<String> scanPackageClasses(String srcPackage,boolean recursive){
        List<String> result = new ArrayList<String>();
        List<Class<?>> lists =  ClassUtil.getClassList(srcPackage, recursive);
        for(Class clz : lists){
            String clzPackageWithName = clz.getName();
            //排除内部类
            if(!clzPackageWithName.contains("$")){
                result.add(clzPackageWithName.concat(".java"));
            }
        }
        return result;
    }


    /**
     * 递归扫描生成器依赖的实体类
     * <pre>
     * 例如:
     *      实体全路径 E:/project/src/main/java/com/wuyu/domain/*.java
     *      包目录路径 com.wuyu.domain (支持com/wuyu/domain)
     *      代码根路径 E:/project/src/main/java/
     * </pre>
     * @param srcRoot 表示包所在的路径,对于应用工程,srcRoot即就是应用的代码包所在路径.
     * @param srcPackage 项目工程中的包路径
     * @return  返回包名+类名的整合串(package + class)
     *      Eg:
     *          com.dobby.domain.Test.java
     *          com.dobby.domain.sub.InTest.java
     */
    public static List<String> scanPackageClasses(String srcRoot,String srcPackage){
        return scanPackageClasses(srcRoot,srcPackage,true);
    }


    /**
     * 扫描生成器依赖的实体类
     * <pre>
     * 例如:
     *      实体全路径 E:/project/src/main/java/com/wuyu/domain/*.java
     *      包目录路径 com.wuyu.domain (支持com/wuyu/domain)
     *      代码根路径 E:/project/src/main/java/
     * </pre>
     * @param srcRoot 表示包所在的路径,对于应用工程,srcRoot即就是应用的代码包所在路径.
     * @param srcPackage 项目工程中的包路径
     * @param recursive  扫描是否支持递归,true表示支持递归,反之不支持递归
     * @return  返回包名+类名的整合串(package + class)
     *      Eg:
     *          com.dobby.domain.Test.java
     *          com.dobby.domain.sub.InTest.java
     */
    public static List<String> scanPackageClasses(String srcRoot,String srcPackage,boolean recursive){
        if(StringUtils.isBlank(srcPackage)){
            srcRoot = "";
        }
        String path = FileUtil.fileNameConcat(srcRoot, srcPackage.replaceAll("\\.", "/"));
        LOG.debug("[init] path : " + path);
        if(StringUtils.isBlank(path)){
            return null;
        }
        File dir = new File(path);
        List<String> lists = new ArrayList<String>();
        if( ! dir.exists()  ||! dir.isDirectory()){
            return null;
        }
        final boolean fileRecursive = recursive;
        File[] dirfiles = dir.listFiles(new FileFilter(){
            // 自定义文件过滤规则
            public boolean accept(File file){
                if( file.isDirectory()){ return fileRecursive; }
                String filename = file.getName();
                if(!filename.endsWith(".java")){
                    return false;
                }
                return true;
            }
        });

        for(File file:dirfiles){
            if(file.isDirectory()){
                String srcRealPath =  file.getAbsolutePath();
                int srcIndex = srcRealPath.indexOf(srcRoot);
                if(-1 == srcIndex){
                    srcIndex = srcRealPath.indexOf(srcRoot.replaceAll("\\/","\\\\"));
                }
                String subSrcPackage = srcRealPath.substring(srcIndex + srcRoot.length()+1).replaceAll("\\/", ".");
                List<String> tmp = scanPackageClasses(srcRoot, subSrcPackage, recursive);
                if(null!=tmp && tmp.size()>0){
                    lists.addAll(tmp);
                }
            }else{
                srcPackage = srcPackage.replaceAll("\\\\", "/").replaceAll("\\/", "\\.");
                lists.add(srcPackage + "." + file.getName());
            }
        }
        return lists;
    }


    /*public static void main(String [] args){
        //获取扫描到的实体类
        // 参数1表示应用上下文路径
        // 参数2表示包路径
        List<String> names = ScanUtils.scanPackageClasses("E:/YinYueTai/YYT-Platform/stage-provider/mobile-stage-domain/src/main/java/", "com/yinyuetai/mobile/stage/domain", true);
        System.out.println(names);

        List<String> name2 = ScanUtils.scanPackageClasses("E:/YinYueTai/YYT-Platform/stage-provider/mobile-stage-domain/src/main/java/", "com.yinyuetai.mobile.stage.domain", true);
        System.out.println(name2);

        // 针对当前工程进行扫描
        List<String> name3 = ScanUtils.scanPackageClasses("com.yyt.domain", true);

        System.out.println(name3);
    }*/
}
