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

import java.io.File;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/05 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class QiNiuFsImageRepository extends QiNiuFsRepository{

   /* public String imageCUT(String fileKey, int width, int height){
        http://developer.qiniu.com/resource/gogopher.jpg?imageView2/1/w/200/h/200
    }
*/
    public static void main(String[] args){
        QiNiuFsImageRepository qiNiuFsRepository = new QiNiuFsImageRepository();
        System.out.println(qiNiuFsRepository.listBuckets());

        File file = new File("D:/2.jpg");
        //String fileName = /*Uni*/
        //System.out.println(qiNiuFsRepository.upload("test", "/avatar/2015/12/22/test2.jpg", file));

        //System.out.println(qiNiuFsRepository.upload("test", "/avatar/2015/12/22/test2.jpg", "D:/xxxxx.jpg"));

       /* List<FileInfo> lists = qiNiuFsRepository.listFileByPrefix("test", "/avatar", 10, null);
        if(null != lists && !lists.isEmpty()){
            for(FileInfo fileInfo: lists){
                System.out.println(JsonUtil.toJson(fileInfo));
            }
        }*/

        //System.out.println(qiNiuFsRepository.delete("test", "favicon.ico"));

        // System.out.println(qiNiuFsRepository.isExist("test", "robots.txt"));
        //System.out.println(qiNiuFsRepository.isExist("test", "favicon.ico"));

        System.out.println(qiNiuFsRepository.getRandPrimaryKey());

        System.out.println(qiNiuFsRepository.download("/avatar/2015/12/22/test2.jpg"));
    }
}
