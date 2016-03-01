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
package com.wuyu.plugin.gen.template;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class Loader {

    private static final Logger LOG = LoggerFactory.getLogger(Loader.class);


    /**
     * 递归获取某个绝对路径下的所有模板内容
     * @param templateRoot 模板文件存放的绝对路径
     * @param resultFileNames 模板文件名称(含路径名)集合
     * @return
     */
    public static List<String> recursiveLoadAbsolute(String templateRoot,List<String> resultFileNames){
        if(StringUtils.isBlank(templateRoot)){
            return Collections.emptyList();
        }
        try{
            File file = new File(templateRoot);
            File[] files = file.listFiles();
            if(null != files ){
                for (File f : files) {
                    if(f.isDirectory()){        // 判断是否文件夹
                        //resultFileName.add(f.getPath());  //不增加目录文件
                        recursiveLoadAbsolute(f.getPath(),resultFileNames);// 调用自身,查找子目录
                    }else{
                        resultFileNames.add(f.getAbsolutePath());
                    }
                }
                return resultFileNames;
            }

        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return Collections.emptyList();
    }

    /**
     * 递归获取某个相对路径下的所有模板内容
     * @param templateRoot 相对路径下的资源路径
     * @param resultFileNames 模板文件名(含有路径)结果集
     * @return
     */
    public static List<String> recursiveLoadRelative(String templateRoot,List<String> resultFileNames){
        if(StringUtils.isBlank(templateRoot)){
            return Collections.emptyList();
        }
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(templateRoot.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath();
                        packagePath = URLDecoder.decode(packagePath, "UTF-8");
                        if(StringUtils.isNotBlank(packagePath)){
                            File file = new File(packagePath);
                            File[] files = file.listFiles();
                            if(null != files){
                                for (File f : files) {
                                    if(f.isDirectory()){        // 判断是否文件夹
                                        // 解析相对路径
                                        String tempAbsPath = f.getAbsolutePath().replaceAll("\\\\","/");
                                        int index = tempAbsPath.lastIndexOf(templateRoot);
                                        String relativePath = tempAbsPath.substring(index, tempAbsPath.length()).replaceAll("\\\\","/");
                                        recursiveLoadRelative(relativePath, resultFileNames);// 调用自身,查找子目录
                                    }else{
                                        resultFileNames.add(f.getPath());
                                    }
                                }
                                return resultFileNames;
                            }
                        }
                    }else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        JarFile jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> jarEntries = jarFile.entries();
                        while (jarEntries.hasMoreElements()) {
                            JarEntry jarEntry = jarEntries.nextElement();
                            String jarEntryName = jarEntry.getName();
                            if(StringUtils.isNotBlank(jarEntryName) && jarEntryName.startsWith(templateRoot) && !jarEntryName.endsWith("/")){
                                resultFileNames.add(jarEntryName);
                            }
                        }
                        return resultFileNames;
                    }
                }
            }

        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage(),e);
        }
        return Collections.emptyList();
    }

}
