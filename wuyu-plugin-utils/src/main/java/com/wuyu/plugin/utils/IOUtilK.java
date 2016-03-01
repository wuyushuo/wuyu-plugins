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

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class IOUtilK {

    /** 默认缓存大小 */
    public final static int DEFAULT_BUFFER_SIZE = 1024;

    public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
        IOUtils.write(data, output, encoding);
    }

    /**
     * 将Reader中的内容复制到Writer中
     * 使用默认缓存大小
     * @param reader Reader
     * @param writer Writer
     * @return 拷贝的字节数
     * @throws IOException
     */
    public static int copy(Reader reader, Writer writer) throws IOException {
        return copy(reader, writer, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 将Reader中的内容复制到Writer中
     * @param reader Reader
     * @param writer Writer
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IOException
     */
    public static int copy(Reader reader, Writer writer, int bufferSize) throws IOException {
        char[] buffer = new char[bufferSize];
        int count = 0;
        int readSize;
        while ((readSize = reader.read(buffer, 0, bufferSize)) >= 0) {
            writer.write(buffer, 0, readSize);
            count += readSize;
            writer.flush();
        }

        return count;
    }

    /**
     * 拷贝流，使用默认Buffer大小
     * @param in 输入流
     * @param out 输出流
     * @return 传输的byte数
     * @throws IOException
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 拷贝流
     * @param in 输入流
     * @param out 输出流
     * @param bufferSize 缓存大小
     * @return 传输的byte数
     * @throws IOException
     */
    public static int copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int count = 0;
        for (int n = -1; (n = in.read(buffer)) != -1;) {
            out.write(buffer, 0, n);
            count += n;
            out.flush();
        }

        return count;
    }

    /**
     * 拷贝文件流，使用NIO
     * @param in 输入
     * @param out 输出
     * @return 拷贝的字节数
     * @throws IOException
     */
    public static long copy(FileInputStream in, FileOutputStream out) throws IOException {
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        return inChannel.transferTo(0, inChannel.size(), outChannel);
    }
    //-------------------------------------------------------------------------------------- Copy end

    /**
     * 获得一个文件读取器
     * @param in 输入流
     * @param charsetName 字符集名称
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader getReader(InputStream in, String charsetName) throws IOException{
        return getReader(in, Charset.forName(charsetName));
    }

    /**
     * 获得一个文件读取器
     * @param in 输入流
     * @param charset 字符集
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader getReader(InputStream in, Charset charset) throws IOException{
        if(null == in){
            return null;
        }

        InputStreamReader reader = null;
        if(null == charset) {
            reader = new InputStreamReader(in);
        }else {
            reader = new InputStreamReader(in, charset);
        }

        return new BufferedReader(reader);
    }


    /**
     * 从Reader中读取String
     * @param reader Reader
     * @return String
     * @throws IOException
     */
    public static String read(Reader reader) throws IOException{
        final StringBuilder builder = new StringBuilder();
        final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
        while(-1 != reader.read(buffer)){
            builder.append(buffer.flip().toString());
        }
        return builder.toString();
    }

    /**
     * 从FileChannel中读取内容
     * @param fileChannel 文件管道
     * @param charset 字符集
     * @return 内容
     * @throws IOException
     */
    public static String read(FileChannel fileChannel, String charset) throws IOException {
        final MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size()).load();
        return StringUtil.str(buffer, charset);
    }

    /**
     * 从流中读取内容
     *
     * @param in 输入流
     * @param charset 字符集
     * @param collection 返回集合
     * @return 内容
     * @throws IOException
     */
    public static <T extends Collection<String>> T readLines(InputStream in, String charset, T collection) throws IOException {
        // 从返回的内容中读取所需内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        String line = null;
        while ((line = reader.readLine()) != null) {
            collection.add(line);
        }

        return collection;
    }

    /**
     * String 转为 流
     * @param content 内容
     * @param charset 编码
     * @return 字节流
     */
    public static ByteArrayInputStream toStream(String content, String charset) {
        if(content == null) {
            return null;
        }

        byte[] data = null;
        try {
            data = StringUtil.isBlank(charset) ? content.getBytes() : content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Invalid charset" + charset, e);
        }

        return new ByteArrayInputStream(data);
    }


    /**
     * 打印内容，调用系统的System.out.println方法
     * @param content 内容，会调用toString方法， 当内容中有 {} 表示变量占位符
     * @param param 参数
     */
    public static void echo(Object content, Object... param) {
        if(content == null) {
            System.out.println(content);
        }
        System.out.println(StringUtil.format(content.toString(), param));
    }

    /**
     * 关闭
     *
     * @param closeable 被关闭的对象
     */
    public static void close(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
        }
    }
}
