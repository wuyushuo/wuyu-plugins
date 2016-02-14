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
package com.wuyu.plugin.wfilter.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * class function depict
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/06 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class WFLoad {

    private Set<String> keywords = new CopyOnWriteArraySet<String>();

    private Properties props;

    private WFLoad(){
        loadInit();
        Set<String> main = loadMainDict();
        Set<String> exts = loadExtDict();
        System.out.println("default word size:" + main.size());
        keywords.addAll(main);
        System.out.println("extend word size:" + exts.size());
        keywords.addAll(exts);
    }

    static class WFLoadHold{
       public static WFLoad load = new WFLoad();
    }

    public static WFLoad getWFLoad(){
        return WFLoadHold.load;
    }

    private void loadInit(){
        props = new Properties();
        InputStream input = this.getClass().getClassLoader().getResourceAsStream(WConfs.CONF_FILE_NAME);
        if(input != null){
            try {
                props.loadFromXML(input);
            } catch (InvalidPropertiesFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载主词典及扩展词典
     */
    private Set<String> loadMainDict(){
        Set<String>  mainWords  = new CopyOnWriteArraySet<String>();
        //读取主词典文件
        InputStream is = WFLoad.class.getClassLoader().getResourceAsStream(WConfs.MAIN_ILLEGAL_DICT);
        if(is == null){
            throw new RuntimeException("main Dictionary not found!!!");
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
            String theWord = null;
            do {
                theWord = br.readLine();
                if (theWord != null && !"".equals(theWord.trim())) {
                    mainWords.add(theWord.trim().toLowerCase());
                }
            } while (theWord != null);
        } catch (IOException ioe) {
            System.err.println("Main Dictionary loading exception.");
            ioe.printStackTrace();
        }finally{
            try {
                if(is != null){
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mainWords;
    }



    /**
     * 加载用户配置的扩展词典到主词库表
     */
    private Set<String> loadExtDict(){
        Set<String> set = new CopyOnWriteArraySet<String>();
        List<String> extDictFiles  = paserConfDictPath();
        if(extDictFiles != null){
            InputStream is = null;
            for(String extDictName : extDictFiles){
                //读取扩展词典文件
                System.out.println("load copnfig dic path：" + extDictName);
                is = this.getClass().getClassLoader().getResourceAsStream(extDictName);
                //如果找不到扩展的字典，则忽略
                if(is == null){
                    continue;
                }
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
                    String theWord = null;
                    do {
                        theWord = br.readLine();
                        if (theWord != null && !"".equals(theWord.trim())) {
                            set.add(theWord);
                        }
                    } while (theWord != null);

                } catch (IOException ioe) {
                    System.err.println("Extension Dictionary loading exception.");
                    ioe.printStackTrace();

                }finally{
                    try {
                        if(is != null){
                            is.close();
                            is = null;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return set;
    }

    private List<String> paserConfDictPath(){
        List<String> extDictFiles = new ArrayList<String>(64);
        String extDictCfg = props.getProperty(WConfs.CONF_KEY_iLLEGAL_DICT);
        if(extDictCfg != null){
            // load multi dict config splited by
            String[] filePaths = extDictCfg.split(WConfs.CONF_KEY_ILLEGAL_DICT_SPLIT_VALUE);
            if(filePaths != null){
                for(String filePath : filePaths){
                    if(filePath != null && !"".equals(filePath.trim())){
                        extDictFiles.add(filePath.trim());
                    }
                }
            }
        }
        return extDictFiles;
    }

    public Set<String> getFilterWords(){
        return this.keywords;
    }

    public String getFilterReplaceTo(){
        String replaceTo = props.getProperty(WConfs.CONF_KEY_REPLACE_TO);
        if (null!=replaceTo && !"".equals(replaceTo.trim())) {
            return replaceTo;
        }
        return WConfs.CONF_KEY_REPLACE_TO_VALUE;
    }

}
