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
package com.wuyu.plugin.qiniu.repository;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.wuyu.plugin.qiniu.auth.DefaultAuthKey;
import com.wuyu.plugin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class QiNiuFsRepository extends DefaultAuthKey {

    private static final Logger LOG = LoggerFactory.getLogger(QiNiuFsRepository.class);

    private String accessKey;

    private String secretKey;


    /**
     * 列出空间命名列表
     * @return
     */
    public List<String> listBuckets(){
        try {
            BucketManager bucketManager = getBucketManager();
            if(null != bucketManager){
                String[] buckets = bucketManager.buckets();
                if(null != buckets && buckets.length > 0){
                    return Arrays.asList(buckets);
                }
            }
        } catch (QiniuException e) {
            LOG.error(e.getLocalizedMessage(),e);
        }
        return null;
    }

    public List<FileInfo> listFileByPrefix(String prefix, int limit, String delimiter){
        return listFileByPrefix(DEFAULT_BUCKET_NAME, limit, delimiter);
    }

    /**
     *
     * @param bucketName 空间名
     * @param prefix 文件名前缀
     * @param limit 每次迭代的大小限制,最大1000,推荐100
     * @param delimiter 指定目录分隔符，列出所有公共前缀（模拟列出目录效果）。缺省值为空字符串
     * @return
     */
    public List<FileInfo> listFileByPrefix(String bucketName, String prefix, int limit, String delimiter){
        List<FileInfo> list = new ArrayList<FileInfo>();
        try {
            BucketManager bucketManager = getBucketManager();
            if(null != bucketManager){
                limit = limit< 0 ? 100 : (limit > 1000? 1000: limit);
                delimiter = StringUtil.isBlank(delimiter)? "" : delimiter;
                BucketManager.FileListIterator it = getBucketManager().createFileListIterator(bucketName, prefix, limit, delimiter);
                while (it.hasNext()) {
                    FileInfo[] items = it.next();
                    for(int i=0; i<items.length; i++){
                        list.add(items[i]);
                    }
                }
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return list;
    }

    public boolean upload(String fileKey, byte[] data){
        return upload(DEFAULT_BUCKET_NAME, fileKey, data);
    }

    /**
     * 上传文件
     * @param bucketName 文件命名空间
     * @param fileKey 文件名
     * @return
     */
    public boolean upload(String bucketName, String fileKey, byte[] data){
        try {
            if (StringUtil.isNotBlank(fileKey)) {
                String uploadToken = getAuth().uploadToken(bucketName);
                UploadManager uploadManager = new UploadManager();
                Response res = uploadManager.put(data, fileKey, uploadToken);
                if(res.isOK()){
                    return true;
                }
                LOG.error(res.error);
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }


    /**
     * 获取一个完全不同的做主键用的字符串，长度为8位
     * @return
     */
    public String getRandPrimaryKey() {
        long num = System.nanoTime() + getRandomNumber(10);
        StringBuffer sb = new StringBuffer();
        do {
            long y = num % 62;
            num = num / 62;
            if (y >= 0 && y <= 9) {
                sb.append(y);
            } else if (y >= 10 && y <= 35) {
                sb.append(((char) (y + 87)));
            }
            else {
                sb.append(((char) (y + 29)));
            }
        } while (num > 0);
        return sb.toString();
    }

    /**
     * getRandomNumber产生一个0-max之间的一个随机数
     */
    public static int getRandomNumber(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }

    public boolean upload(String fileKey, File file){
        return upload(DEFAULT_BUCKET_NAME, fileKey, file);
    }

    /**
     * 上传文件
     * @param bucketName 文件命名空间
     * @param fileKey
     * @return
     */
    public boolean upload(String bucketName, String fileKey, File file){
        try {
            if (StringUtil.isNotBlank(fileKey) && null != file) {
                String uploadToken = getAuth().uploadToken(bucketName);
                UploadManager uploadManager = new UploadManager();
                Response res = uploadManager.put(file, fileKey, uploadToken);
                if(res.isOK()){
                    return true;
                }
                LOG.error(res.error);
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    public boolean upload(String fileKey, String filePath){
        return upload(DEFAULT_BUCKET_NAME, fileKey, filePath);
    }

    /**
     * 上传文件
     * @param bucketName
     * @param fileKey
     * @param filePath
     * @return
     */
    public boolean upload(String bucketName, String fileKey, String filePath){
        try {
            if (StringUtil.isNotBlank(fileKey) && StringUtil.isNotBlank(filePath)) {
                String uploadToken = getAuth().uploadToken(bucketName);
                UploadManager uploadManager = new UploadManager();
                Response res = uploadManager.put(filePath, fileKey, uploadToken);
                if(res.isOK()){
                    return true;
                }
                LOG.error(res.error);
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    /**
     * 下载资源
     * @param sourceUrl
     * @return
     */
    public String download(String sourceUrl){
        try {
            Auth auth = getAuth();
            return auth.privateDownloadUrl(sourceUrl);
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public boolean isExist(String fileKey){
        return isExist(DEFAULT_BUCKET_NAME, fileKey);
    }

    /**
     * 查看文件是否存在
     * @param bucketName
     * @param fileKey
     * @return
     */
    public boolean isExist(String bucketName, String fileKey){
        try {
            BucketManager bucketManager = getBucketManager();
            if(null != bucketManager){
                FileInfo info = bucketManager.stat(bucketName, fileKey);
                return null != info;
            }
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }

    public boolean delete(String fileKey){
        return delete(DEFAULT_BUCKET_NAME, fileKey);
    }

    /**
     * 删除文件
     * @param bucketName 文件命名空间
     * @param fileKey 文件key
     * @return
     */
    public boolean delete(String bucketName, String fileKey) {
        try {
            BucketManager bucketManager = getBucketManager();
            if(null != bucketManager){
                bucketManager.delete(bucketName, fileKey);
            }
            return true;
        }catch (Exception e){
            LOG.error(e.getLocalizedMessage(), e);
        }
        return false;
    }


    private StringMap getParams(){
        return new StringMap().putNotEmpty("returnBody", "{\"key\": $(key), \"hash\": $(etag), \"width\": $(imageInfo.width), \"height\": $(imageInfo.height)}");
        //return new StringMap().put("x:foo", "foo");
    }

    public BucketManager getBucketManager(){
        BucketManager bucketManager = new BucketManager(getAuth());
        return bucketManager;
    }

    public Auth getAuth(){
        return Auth.create(this.accessKey, this.secretKey);
    }


    public void setAccessKey(String accessKey) {
        if(StringUtil.isBlank(accessKey)){
            accessKey  = ACCESS_KEY;
        }
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        if(StringUtil.isBlank(secretKey)){
            secretKey = SECRET_KEY;
        }
        this.secretKey = secretKey;
    }
}
