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

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;


/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class AssetsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AssetsUtil.class);

    private static final String JS_EXT = ".js";
    private static final String JS_MIN_EXT = ".min.js";
    private static final String CSS_EXT = ".css";
    private static final String CSS_MIN_EXT = ".min.css";
    private static final String CHARSET = "UTF-8";


    public static void removeMinFiles(ArrayList<File> files) {
        if (!(files instanceof ArrayList)) {
            return;
        }
        String fileString = null;
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            fileString = file.toString().toLowerCase();
            if (fileString.endsWith(JS_MIN_EXT) || fileString.endsWith(CSS_MIN_EXT)) {
                System.out.println("delete file:" + fileString);
                file.delete();
            }
        }
    }

    public static void buildMinFiles(ArrayList<File> files) {
        if (!(files instanceof ArrayList)) {
            return;
        }
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            if (isJsFile(file.toString())) {
                String inFilePath = file.toString();
                String outFilePath = inFilePath.substring(0, inFilePath.length() - JS_EXT.length()).concat(JS_MIN_EXT);
                LOG.debug("js:{}", outFilePath);
                compressJs(file, outFilePath, CHARSET);
            }
            if (isCssFile(file.toString())) {
                String inFilePath = file.toString();
                String outFilePath = inFilePath.substring(0, inFilePath.length() - CSS_EXT.length()).concat(CSS_MIN_EXT);
                compressCss(file, outFilePath, CHARSET);
            }
        }
    }

    /**
     * is js file
     * @param filename
     * @return
     */
    public static boolean isJsFile(String filename){
        if(StringUtil.isBlank(filename)){
            return false;
        }
        filename = filename.toLowerCase();
        return filename.endsWith(JS_EXT) && !filename.endsWith(JS_MIN_EXT);
    }

    /**
     * is css file
     * @param filename
     * @return
     */
    public static boolean isCssFile(String filename){
        if(StringUtil.isBlank(filename)){
            return false;
        }
        filename = filename.toLowerCase();
        return filename.endsWith(CSS_EXT) && !filename.endsWith(CSS_MIN_EXT);
    }


    /**
     *
     * @param inFilePath
     * @param outFilePath
     * @param charset
     */
    public static void compressCss(File inFilePath, String outFilePath, String charset){
        LOG.debug("css:{}", outFilePath);
        Reader file_in = null;
        Writer file_out = null;
        try {
            file_in = new InputStreamReader(new FileInputStream(inFilePath), charset);
            CssCompressor compressor = new CssCompressor(file_in);
            file_in.close();
            file_out = new OutputStreamWriter(new FileOutputStream(outFilePath), charset);
            compressor.compress(file_out, -1);
            file_out.close();
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        } finally {
            IOUtils.closeQuietly(file_in);
            IOUtils.closeQuietly(file_out);
        }
    }


    /**
     * compress js
     * @param inFilePath
     * @param outFilePath
     * @param charset
     */
    public static void compressJs(File inFilePath, String outFilePath, String charset){
        Reader file_in = null;
        Writer file_out = null;
        try {
            file_in = new InputStreamReader(new FileInputStream(inFilePath), charset);
            JavaScriptCompressor compressor = new JavaScriptCompressor(file_in, new ErrorReporter() {
                public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
                    if (line < 0) {
                        LOG.error("\n[WARNING] " + message);
                    } else {
                        LOG.error("\n[WARNING] " + line + ':' + lineOffset + ':' + message);
                    }
                }
                public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
                    if (line < 0) {
                        LOG.error("\n[ERROR] " + message);
                    } else {
                        LOG.error("\n[ERROR] " + line + ':' + lineOffset + ':' + message);
                    }
                }
                public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
                    error(message, sourceName, line, lineSource, lineOffset);
                    return new EvaluatorException(message);
                }
            });
            file_out = new OutputStreamWriter(new FileOutputStream(outFilePath), charset);
            compressor.compress(file_out, -1, true, false, false, false);
            file_in.close();
            file_out.flush();
            file_out.close();
        }  catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        } finally {
            IOUtils.closeQuietly(file_in);
            IOUtils.closeQuietly(file_out);
        }

    }


    public static ArrayList<File> getListFiles(Object obj) {
        File directory = null;
        if (obj instanceof File) {
            directory = (File) obj;
        } else {
            directory = new File(obj.toString());
        }
        ArrayList<File> files = new ArrayList<File>();
        if (directory.isFile()) {
            files.add(directory);
            return files;
        } else if (directory.isDirectory()) {
            File[] fileArr = directory.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                File fileOne = fileArr[i];
                files.addAll(getListFiles(fileOne));
            }
        }
        return files;
    }

}
