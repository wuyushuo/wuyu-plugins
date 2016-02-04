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
package com.wuyu.plugin.gen.core;


import com.wuyu.plugin.gen.bean.EntityTable;
import com.wuyu.plugin.gen.bean.VersionInfo;
import com.wuyu.plugin.gen.parser.TemplateParser;
import com.wuyu.plugin.gen.template.TemplateLoader;
import com.wuyu.plugin.gen.util.BeanUtils;
import com.wuyu.plugin.gen.util.ScanUtils;
import com.wuyu.plugin.utils.CollectionUtil;
import com.wuyu.plugin.utils.FileUtil;
import com.wuyu.plugin.utils.IOUtilK;
import com.wuyu.plugin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class Generator {

    public static final String CHARSET = "UTF-8";

    public static final String TEMPLAT = "template";

    private static final Logger LOG = LoggerFactory.getLogger(Generator.class);


    /**
     * 扫描指定包的类,映射生成代码和代码存放生成模板
     * @param classPackage 扫描的实体类所在的包
     * @param genPackage 代码生成的基类包名
     * @param sourceRoot 代码生成目录
     * @return
     */
    public static void genSourceBindTemplateRelativeUTF8(String classPackage,String sourceRoot,String genPackage){
        genSourceBindTemplateRelative(classPackage, sourceRoot, genPackage, Generator.CHARSET);
    }

    /**
     * 扫描指定包的类,映射生成代码和代码存放生成模板
     * @param classPackage 扫描的实体类所在的包
     * @param genPackage 代码生成的基类包名
     * @param sourceRoot 代码生成目录
     * @param encoding 生成的代码使用的编码
     * @return
     */
    public static void genSourceBindTemplateRelative(String classPackage,String templateFile,String sourceRoot,String genPackage,String encoding){
        List<String> packagedomainNames = getEntityNames(classPackage);
        if(CollectionUtil.isEmpty(packagedomainNames)){
            LOG.error("[load domains] found no none domain");
        }
        for(String packagedomainName : packagedomainNames){
            if(StringUtil.isNotBlank(packagedomainName)){
                String baseEntityPackageName = BeanUtils.getClassPackage(packagedomainName);
                String baseEntityName = BeanUtils.getClassName(packagedomainName);
                String basePackageEntity = BeanUtils.getPackageWithClassName(packagedomainName);
                if(StringUtil.isNotBlank(baseEntityPackageName)
                        && StringUtil.isNotBlank(baseEntityName)
                        && StringUtil.isNotBlank(basePackageEntity)){
                    mergeTemplateWithSource(genPackage, templateFile, sourceRoot, encoding, baseEntityPackageName, baseEntityName, basePackageEntity);
                }
            }
        }
    }

    /**
     * 扫描指定包的类,映射生成代码和代码存放生成模板
     * @param classPackage 扫描的实体类所在的包
     * @param genPackage 代码生成的基类包名
     * @param sourceRoot 代码生成目录
     * @param encoding 生成的代码使用的编码
     * @return
     */
    public static void genSourceBindTemplateRelative(String classPackage,String sourceRoot,String genPackage,String encoding){
        List<String> packagedomainNames = getEntityNames(classPackage);
        if(CollectionUtil.isEmpty(packagedomainNames)){
            LOG.error("[load domains] found no none domain");
        }
        for(String packagedomainName : packagedomainNames){
            if(StringUtil.isNotBlank(packagedomainName)){
                String baseEntityPackageName = BeanUtils.getClassPackage(packagedomainName);
                String baseEntityName = BeanUtils.getClassName(packagedomainName);
                String basePackageEntity = BeanUtils.getPackageWithClassName(packagedomainName);
                if(StringUtil.isNotBlank(baseEntityPackageName)
                        && StringUtil.isNotBlank(baseEntityName)
                        && StringUtil.isNotBlank(basePackageEntity)){
                     mergeTemplateWithSource(genPackage, Generator.TEMPLAT, sourceRoot, encoding, baseEntityPackageName, baseEntityName, basePackageEntity);
                }
            }
        }
    }

    /**
     * 扫描指定包的类,映射生成代码和代码存放生成模板
     * @param classRoot 扫描的实体类所在的绝对路径
     * @param classPackage 扫描的实体类所在的包
     * @param genPackage 代码生成的基类包名
     * @param sourceRoot 代码生成目录
     * @return
     */
    public static void genSourceBindTemplateAbsoluteUTF8(String classRoot,String classPackage,String sourceRoot,String genPackage){
        genSourceBindTemplateAbsolute(classRoot,classPackage,sourceRoot,genPackage, Generator.CHARSET);
    }


    /**
     * 扫描指定包的类,映射生成代码和代码存放生成模板
     * @param classRoot 扫描的实体类所在的绝对路径
     * @param classPackage 扫描的实体类所在的包
     * @param genPackage 代码生成的基类包名
     * @param sourceRoot 代码生成目录
     * @return
     */
    public static void genSourceBindTemplateAbsolute(String classRoot,String classPackage,String sourceRoot,String genPackage,String encoding){
        List<String> packagedomainNames = genEntityNames(classRoot,classPackage);
        if(CollectionUtil.isEmpty(packagedomainNames)){
            LOG.error("[load domains] found no none domain");
        }
        for(String packagedomainName : packagedomainNames){
            if(StringUtil.isNotBlank(packagedomainName)){
                String baseEntityPackageName = BeanUtils.getClassPackage(packagedomainName);
                String baseEntityName = BeanUtils.getClassName(packagedomainName);
                String basePackageEntity = BeanUtils.getPackageWithClassName(packagedomainName);
                if(StringUtil.isNotBlank(baseEntityPackageName)
                        && StringUtil.isNotBlank(baseEntityName)
                        && StringUtil.isNotBlank(basePackageEntity)){
                    mergeTemplateWithSource(genPackage, Generator.TEMPLAT, sourceRoot, encoding, baseEntityPackageName, baseEntityName, basePackageEntity);
                }
            }
        }
    }

    /**
     * 扫描指定包的类,映射生成代码和代码存放生成模板
     * @param classRoot 扫描的实体类所在的绝对路径
     * @param classPackage 扫描的实体类所在的包
     * @param genPackage 代码生成的基类包名
     * @param sourceRoot 代码生成目录
     * @return
     */
    public static void genSourceBindTemplateAbsolute(String classRoot,String classPackage,String templateFile,String sourceRoot,String genPackage,String encoding){
        List<String> packagedomainNames = genEntityNames(classRoot,classPackage);
        if(CollectionUtil.isEmpty(packagedomainNames)){
            LOG.error("[load domains] found no none domain");
        }
        for(String packagedomainName : packagedomainNames){
            if(StringUtil.isNotBlank(packagedomainName)){
                String baseEntityPackageName = BeanUtils.getClassPackage(packagedomainName);
                String baseEntityName = BeanUtils.getClassName(packagedomainName);
                String basePackageEntity = BeanUtils.getPackageWithClassName(packagedomainName);
                if(StringUtil.isNotBlank(baseEntityPackageName)
                        && StringUtil.isNotBlank(baseEntityName)
                        && StringUtil.isNotBlank(basePackageEntity)){
                    mergeTemplateWithSource(genPackage, templateFile, sourceRoot, encoding, baseEntityPackageName, baseEntityName, basePackageEntity);
                }
            }
        }
    }


    /**
     * 将代码和扫描到的实体合并
     * @param genPackage
     * @param templateRoot
     * @param sourceRoot
     * @param encoding
     * @param baseEntityPackageName
     * @param baseEntityName
     * @param basePackageEntity
     * @return
     */
    private static void mergeTemplateWithSource(String genPackage, String templateRoot, String sourceRoot, String encoding, String baseEntityPackageName, String baseEntityName, String basePackageEntity) {
        EntityTable entityTable = BeanUtils.constructEntityTableWithPath(basePackageEntity);
        Map<String,String> sourceBindTemplate = genSourceBindTemplate(baseEntityName,genPackage,templateRoot,sourceRoot);
        Map context = new HashMap();
        context.put("basePackageName",genPackage);
        context.put("baseEntityName", baseEntityName);
        context.put("baseEntityPackageName", baseEntityPackageName);
        context.put("genPackage",genPackage);
        context.put("baseEntityTable", entityTable);
        if(null != sourceBindTemplate && sourceBindTemplate.size() >0){
            Set<String> sourcePath = sourceBindTemplate.keySet();
            if(CollectionUtil.isNotEmpty(sourcePath)){
                for(String source : sourcePath){
                    try {
                        File tmpFile = new File(source);
                        if(!tmpFile.getParentFile().exists()) {
                            FileUtil.forceMkdir(tmpFile.getParentFile());
                        }
                        Map<String,String> dbNameToSQL = entityTable.getDbNameToSQL();
                        //代码生成路径
                        FileOutputStream sourceFile = new FileOutputStream(new File(source));
                        //生成的代码内容
                        String templateFile = sourceBindTemplate.get(source);

                        templateRoot = templateRoot.replaceAll("\\\\","/");
                        templateFile = templateFile.replaceAll("\\\\","/");

                        String templateLoaderPath = templateFile.substring(templateFile.lastIndexOf(templateRoot),templateFile.length());

                        String code = TemplateParser.parse(templateLoaderPath, context, encoding, new VersionInfo());
                        System.out.println("[success]:"+ source);
                        //将代码按照指定编码写入文件
                        IOUtilK.write(code, sourceFile, encoding);

                        //将SQL按照指定编码写入文件
                        if(dbNameToSQL.size()>0){
                            Map<String,String> entityToTable =  entityTable.getEntityToTable();
                            String sqlPath = sourceRoot + File.separator + dbNameToSQL.keySet().iterator().next() + ".sql";
                            System.out.println("[success]:" + sqlPath);
                            IOUtilK.write(dbNameToSQL.get(entityToTable.get(baseEntityName)), new FileOutputStream(new File(sqlPath)), encoding);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 加载当前工程中的包下的实体类
     * @param classPackage
     * @return
     */
    private static List<String> getEntityNames(String classPackage){
        return ScanUtils.scanPackageClasses(classPackage);
    }

    /**
     * 加载指定绝对路径下的指定包下的实体类
     * 如果指定的路径没有找到,然后以指定的包名在当前工程中找,如果存在,则映射当前工程中的指定包下的实体
     * @param classRoot 绝对路径地址
     * @param classPackage 扫描的报名
     * @return
     */
    private static List<String> genEntityNames(String classRoot, String classPackage){
        List<String>  domainNames = ScanUtils.scanPackageClasses(classRoot,classPackage);
        if(CollectionUtil.isEmpty(domainNames)){
            //domainNames = new ArrayList<String>();
            domainNames = getEntityNames(classPackage);
        }
        return domainNames;
    }

    /**
     * 根据模板的绝对路径加载模板,映射要生成的代码文件路径与模板路径
     * @param baseEntityName 表映射的实体类名
     * @param genPackage 代码生成的基础包
     * @param templateRoot 模板所在的路径
     * @param sourceRoot 代码生成的记录
     * @return
     */
    private static Map<String,String> genSourceBindTemplate(String baseEntityName,String genPackage,String templateRoot,String sourceRoot){
        Map<String,String> sourceBindTemplate = new HashMap<String,String>();
        if(StringUtil.isBlank(baseEntityName)){
            LOG.error("[load domain] null domain class found !");
        }
        List<String> templateFullNames = TemplateLoader.loadTemplateFiles(templateRoot);
        //System.out.println(templateFullNames);
        if(CollectionUtil.isEmpty(templateFullNames)){
            LOG.error("[load templates] null template files found !");
        }
        if(StringUtil.isNotBlank(genPackage)){
            genPackage = genPackage.replaceAll("\\.", "/");
            if(genPackage.endsWith("/")){
                genPackage = genPackage.substring(0,genPackage.length()-1);
            }
        }
        if(StringUtil.isNotBlank(sourceRoot)){
            sourceRoot = sourceRoot.replaceAll("\\.", "/");
            if (sourceRoot.endsWith("/")){
                sourceRoot = sourceRoot.substring(0,sourceRoot.length()-1);
            }
        }
        for(String templatefullName: templateFullNames){

            // 代码依赖的模板路径
            String templatePath = templatefullName;
            LOG.info("[template file]:" + templatePath);
            templateRoot = templateRoot.replaceAll("\\\\", "/");
            int tempLen = templatePath.lastIndexOf(templateRoot) + 1;
            templatefullName = templatefullName.replaceAll("\\\\", "/");
            templatefullName = templatefullName.substring(tempLen  + templateRoot.length(), templatefullName.length());
            templatefullName = templatefullName.replaceAll("\\$\\{baseEntityPackageName\\}",genPackage).replaceAll("\\$\\{baseEntityName\\}",baseEntityName);
            templatefullName = templatefullName.replaceAll("\\$\\{bean.firstLower\\(\\$baseEntityName\\)\\}", BeanUtils.firstLower(baseEntityName));
            System.out.println("[template path]" + templatefullName);
            if(!templatefullName.startsWith("/")){
                templatefullName = "/" + templatefullName;
            }

            // 生成的代码文件路径
            String codeFile = sourceRoot + templatefullName;

            // 代码文件依赖的模板路径
            LOG.info("[source file]:" + codeFile);

            sourceBindTemplate.put(codeFile, templatePath);
        }
        return sourceBindTemplate;
    }
}
