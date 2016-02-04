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
package com.wuyu.plugin.gen.main;

import com.wuyu.plugin.gen.core.Generator;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class Main {


    public static void main(String[] args){
        System.out.println("=====================================================================================================================");
        System.out.println("\t\tAbsolute Path:");
        System.out.println("\t\t\tMain.gen(String classRoot,String classPackage,String sourceRoot,String genPackage,String encoding)");
        System.out.println("\t\t\tMain.UTF8(String classRoot,String classPackage,String sourceRoot,String genPackage,String encoding)");
        System.out.println("\t\tRelative Path:");
        System.out.println("\t\t\tMain.gen(String classPackage,String sourceRoot,String genPackage,String encoding)");
        System.out.println("\t\t\tMain.UTF8(String classPackage,String sourceRoot,String genPackage,String encoding)");
        System.out.println("\t\tDocument:");
        System.out.println("\t\t\t1.not support external key dependence");
        System.out.println("\t\t\t2.not support multi primary key dependence");
        System.out.println("\t\t\t3.must annotation primary key in domain");
        System.out.println("=====================================================================================================================");
    }

    public static void genUTF8(String classPackage,String sourceRoot,String genPackage){
        Generator.genSourceBindTemplateRelativeUTF8(classPackage, sourceRoot, genPackage);
    }

    public static void gen(String classPackage,String sourceRoot,String genPackage,String encoding){
        Generator.genSourceBindTemplateRelative(classPackage,sourceRoot,genPackage,encoding);
    }

    public static void genUTF8(String classRoot,String classPackage,String sourceRoot,String genPackage){
        Generator.genSourceBindTemplateAbsoluteUTF8(classRoot, classPackage, sourceRoot, genPackage);
    }

    public static void gen(String classRoot,String classPackage,String sourceRoot,String genPackage,String encoding){
        Generator.genSourceBindTemplateAbsolute(classRoot, classPackage, sourceRoot, genPackage, encoding);
    }
}
