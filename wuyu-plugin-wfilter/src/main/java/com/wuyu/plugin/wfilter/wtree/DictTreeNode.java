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
package com.wuyu.plugin.wfilter.wtree;

import com.wuyu.plugin.wfilter.conf.WConfs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: https://github.com/hymer/sensitivewords.git
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class DictTreeNode {

    private static final Logger LOG = LoggerFactory.getLogger(DictTreeNode.class);

    // 敏感词
    private static Set<String> WORDS = new HashSet<String>();

    // 由敏感词生成的字树
    private static Map<String, Map> TREE = new ConcurrentHashMap<String, Map>();

    private static Properties props;

    static {
        loadInit();
        loadMainDict();
        loadExtDict();
        LOG.debug("dict word total:{}", WORDS.size());
    }

    public static Map<String, Map> getTree(){
        return TREE;
    }

    public static char getReplaceTo(){
        try {
            String obj = props.getProperty(WConfs.ELM_REPLACE_TO);
            if(null != obj && obj.trim().length()>0){
                return obj.charAt(0);
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return WConfs.DEFAULT_REPLACE_TO;
    }

    public static boolean isEachReplace(){
        try {
            String obj = props.getProperty(WConfs.ELM_ISEACH_REPLACE);
            if(null != obj && obj.trim().length()>0){
                return Boolean.parseBoolean(obj);
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return WConfs.DEFAULT_ISEACH_REPLACE_TO;
    }


    /**
     * 添加敏感词树
     * @param words
     */
    public static void addDictWord(Collection<String> words){
        buildTreeWithWords(words);
    }

    /**
     * 添加敏感词树
     * @param words
     */
    public static void addDictWord(String ... words){
        buildTreeWithWords(words);
    }

    /**
     * 加载词库
     */
    private static void loadInit(){
        props = new Properties();
        InputStream input = DictTreeNode.class.getClassLoader().getResourceAsStream(WConfs.ELE_CONF_FILE_NAME);
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
    private static void loadMainDict(){
        long start = System.currentTimeMillis();
        //读取主词典文件
        InputStream is = DictTreeNode.class.getClassLoader().getResourceAsStream(WConfs.MAIN_DICT_PATH);
        if(is == null){
            throw new RuntimeException("main Dictionary not found!!!");
        }
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is , WConfs.DEFAULT_CHARSET), 512);
            String theWord = null;
            do {
                theWord = br.readLine();
                if (theWord != null && !"".equals(theWord.trim())) {
                    buildTreeWithWords(theWord);
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
        long end = System.currentTimeMillis();
        System.out.println("加载主词库使用时长:" + (end - start) + "ms");
    }


    /**
     * 加载用户配置的扩展词典到主词库表
     */
    private static void loadExtDict(){
        long start = System.currentTimeMillis();
        List<String> extDictFiles  = paserConfDictPath();
        if(extDictFiles != null){
            InputStream is = null;
            for(String extDictName : extDictFiles){
                //读取扩展词典文件
                System.out.println("load copnfig dic path：" + extDictName);
                is = DictTreeNode.class.getClassLoader().getResourceAsStream(extDictName);
                //如果找不到扩展的字典，则忽略
                if(is == null){
                    continue;
                }
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is , WConfs.DEFAULT_CHARSET), 512);
                    String theWord = null;
                    do {
                        theWord = br.readLine();
                        if (theWord != null && !"".equals(theWord.trim())) {
                            buildTreeWithWords(theWord);
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
        long end = System.currentTimeMillis();
        System.out.println("加载扩展词库使用时长:" + (end - start) + "ms");
    }

    private static List<String> paserConfDictPath(){
        List<String> extDictFiles = new ArrayList<String>(64);
        String extDictCfg = props.getProperty(WConfs.ELE_WORD_DICT);
        if(extDictCfg != null){
            // load multi dict config splited by
            String[] filePaths = extDictCfg.split(";");
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


    /**
     * 格式掉用来标记treeNode的非敏感标识符
     * @param word
     * @return
     */
    private static String formartDictWord(String word){
        if(null == word || word.trim().equals("")){
            return null;
        }
        word = word.trim().toLowerCase();
        if(word.contains(WConfs.TREE_END_KEY)){
            return word.replaceAll(WConfs.TREE_END_KEY, "");
        }
        return word;
    }

    /**
     * 用敏感词构造词汇树,敏感词构造钱先进行格式化
     * @param sensitiveWords
     */
    private static void buildTreeWithWords(Collection<String> sensitiveWords) {
        if(null != sensitiveWords && !sensitiveWords.isEmpty()){
            for(String word: sensitiveWords){
                buildTreeWithWords(word);
            }
        }
    }

    /**
     * 用敏感词构造词汇树,敏感词构造钱先进行格式化
     * @param sensitiveWords
     */
    private static void buildTreeWithWords(String... sensitiveWords) {
        for (String word : sensitiveWords) {
            word = formartDictWord(word);
            if (null != word) {
                int len = word.length();
                if (len > 1024) {
                    LOG.debug("sensitive word too long , skip it .");
                    continue;
                }
                // 添加该词，如果未重复，则添加到tree
                if (WORDS.add(word)) {
                    DictTreeBulkit.buildDictTree(TREE, word);
                }
            }
        }
        LOG.debug("current sensitive word total:{}", WORDS.size());
    }

    /**
     * 移除敏感词,重建敏感树
     * @param sensitiveWords
     */
    private static void buildTreeRemoveWords(String... sensitiveWords) {
        for (String word : sensitiveWords) {
            word = formartDictWord(word);
            if (null != word) {
                WORDS.remove(word);
            }
        }
        TREE.clear();
        buildTreeWithWords(WORDS.toArray(new String[WORDS.size()]));
    }

    /**
     * 清空构建的敏感词树
     */
    public static void clearTree() {
        WORDS.clear();
        TREE.clear();
    }


    /**
     * 打印构建的敏感词数[此方法用户测试]
     */
    public static void printTree(){
        printTree(TREE, 0);
    }

    /**
     * 打印构建的敏感词树[此方法用户测试]
      * @param wordTree
     * @param level
     */
    private static void printTree(Map<String, Map> wordTree, int level) {
        if (null == wordTree || wordTree.isEmpty()) {
            return;
        }
        Iterator<String> it = wordTree.keySet().iterator();
        while (it.hasNext()) {
            String next = it.next();
            for (int i = 0; i < level; i++) {
                System.out.print("-");
            }
            System.out.print(next + "\n");
            Object tmp = wordTree.get(next);
            if (tmp instanceof Map) {
                printTree((Map) tmp, level + 1);
            }
        }
    }


}
