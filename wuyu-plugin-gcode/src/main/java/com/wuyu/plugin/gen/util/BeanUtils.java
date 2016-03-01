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
package com.wuyu.plugin.gen.util;

import com.wuyu.plugin.gen.annotation.AbolishedField;
import com.wuyu.plugin.gen.annotation.GeneratorField;
import com.wuyu.plugin.gen.annotation.GeneratorTable;
import com.wuyu.plugin.gen.bean.EntityTable;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public final class BeanUtils {

    private static final Logger LOG = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * 将字符串的首个字母小写
     * @param str
     * @return
     */
    public static String firstLower(String str) {
        if(str == null || str.length() == 0) {
            return null;
        }
        if(str.length() == 1) {
            return str.toLowerCase();
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 将字符串的首个字母大写
     * @param str
     * @return
     */
    public static String firstUpper(String str) {
        if(str == null || str.length() == 0) {
            return null;
        }
        if(str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 根据属性名构造 getfirstUpper(属性名)方法
     * @param name
     * @return
     */
    public static String getterName(String name) {
        if(name.length() == 1) {
            return "get" + name.toUpperCase();
        }
        return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * 根据属性名构造 setfirstUpper(属性名)方法
     * @param name
     * @return
     */
    public static String setterName(String name) {
        if(name.length() == 1) {
            return "set" + name.toUpperCase();
        }
        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * 从包信息中获取包名和类名
     * @param classPathWithPackage
     * @return
     */
    public static String getPackageWithClassName(String classPathWithPackage){
        int suffixSubLen = 0;
        if(StringUtils.isNotBlank(classPathWithPackage)){
            suffixSubLen = classPathWithPackage.lastIndexOf(".");
            return classPathWithPackage.substring(0, suffixSubLen);
        }
        return classPathWithPackage;
    }

    /**
     * 从包信息中获取类名
     * <pre>
     *     com.wuyu.domain.Student.java
     *     返回Student
     * </pre>
     * @param classPathWithPackage
     * @return
     */
    public static String getClassName(String classPathWithPackage){
        String packageWithClassName = getPackageWithClassName(classPathWithPackage);
        int suffixSubLen = 0;
        if(StringUtils.isNotBlank(packageWithClassName)){
            suffixSubLen = packageWithClassName.lastIndexOf(".");
            return packageWithClassName.substring(suffixSubLen + 1, packageWithClassName.length());
        }
        return classPathWithPackage;
    }

    /**
     * 从包信息中获取包名
     * <pre>
     *     com.wuyu.domain.Student.java
     *     返回com.wuyu.domain
     * </pre>
     * @param classPathWithPackage
     * @return
     */
    public static String getClassPackage(String classPathWithPackage){
        String packageWithClassName = getPackageWithClassName(classPathWithPackage);
        String className = getClassName(classPathWithPackage);
        return classPathWithPackage.substring(0,  packageWithClassName.length() - className.length() -1);
    }

    /**
     * 根据对象构造,表映射
     * @param packageClass 实体对象
     * @return 实体对象到字段表的映射,基于注解处理
     */
    @SuppressWarnings("unchecked")
	public static EntityTable constructEntityTableWithPath(String packageClass){
        Object cObj = null;
        try {
            Class c = Class.forName(packageClass);
            if(c.isEnum() || c.isInterface()){
                LOG.error("[parer error] " + packageClass + " is not class .");
                return null;
            }
            cObj = c.getConstructor().newInstance();        //调用无参的构造方法
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        if(null != cObj){
            return constructEntityTableWithObject(cObj);
        }
        return null;
    }

    /**
     * 根据对象构造,表映射
     * <pre>
     *     com.yyt.domain.Opus.java
     *     传递参数为com.yyt.domain.Opus
     * </pre>
     * @param object 用于扫描的带有生成器依赖注解的domain类,类必须注解指明主键,否则无法生成主键依赖接口
     * @return 实体对象到字段表的映射,基于注解处理,说明,多主键时只扫描第一个主键注解
     */
    public static EntityTable constructEntityTableWithObject(Object object){
        EntityTable entityTable = new EntityTable();
        //主键组
        ConcurrentMap<String,String> propertyToKey = new ConcurrentHashMap<String,String>();
        //字段组
        ConcurrentMap<String,String> propertyToColumn = new ConcurrentHashMap<String, String>();
        //主键到集合类型映射
        ConcurrentMap<String,Object> propertyPKeyType = new ConcurrentHashMap<String,Object>();
        // 表名到SQL的映射
        ConcurrentMap<String,String> dbNameToSQL = new ConcurrentHashMap<String,String>();
        StringBuffer sql = new StringBuffer("DROP TABLE IF EXISTS ");
        try{
            if(null == object){return null;}

            ConcurrentMap<String,String> entityToTable = new ConcurrentHashMap<String, String>();
            boolean isGeneratorTable = object.getClass().isAnnotationPresent(GeneratorTable.class);
            String tbName = null;
            if(isGeneratorTable){
                GeneratorTable generatorTable = object.getClass().getAnnotation(GeneratorTable.class);
                if(StringUtils.isBlank(generatorTable.name())){
                    tbName = firstLower(object.getClass().getSimpleName());
                    entityToTable.put(object.getClass().getSimpleName(),tbName);
                }else{
                    tbName = generatorTable.name();
                    entityToTable.put(object.getClass().getSimpleName(),tbName);
                }

            }else{
                tbName = firstLower(object.getClass().getSimpleName());
                entityToTable.put(object.getClass().getSimpleName(),tbName);
            }
            sql.append(tbName + ";\r\n" + "CREATE TABLE " + tbName + "(\r\n");

            Class<?> clazz = object.getClass() ;

            for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
                Field[] fields = clazz.getDeclaredFields();
                if(null != fields){

                    for (int i = 0; i < fields.length; i++) {
                        //判断是否剔除
                        boolean isAbolishedField = fields[i].isAnnotationPresent(AbolishedField.class);
                        if(isAbolishedField){
                            //跳过该字段的提取
                            continue;
                        }
                        //判断是否是主键
                        boolean isGeneratorField = fields[i].isAnnotationPresent(GeneratorField.class);
                        if(isGeneratorField){
                            // 取注解中的文字说明
                            GeneratorField generatorField =  fields[i].getAnnotation(GeneratorField.class);
                            boolean primaryKey = generatorField.primaryKey();
                            if(primaryKey){
                                //主键只取第一个有效的,其它多个视为无效主键注解
                                if(propertyToKey.size()<=0){
                                    //添加到主键集合
                                    String pkName = fields[i].getName();
                                    if(StringUtils.isNotBlank(generatorField.name())){
                                        pkName = generatorField.name();
                                    }
                                    propertyPKeyType.put(pkName,fields[i].getType().getName());
                                    propertyToKey.put(pkName,fields[i].getName());
                                    sql.append(pkName + " " + generatorField.type().toLowerCase() + " primary key not null auto_increment ");
                                    sql.append(StringUtils.isBlank(generatorField.comment())? ",": "comment '" + generatorField.comment() + "' ,\r\n");
                                }
                            }else{
                                if(StringUtils.isBlank(generatorField.name())){
                                    propertyToColumn.put(fields[i].getName(),fields[i].getName());
                                    if(StringUtils.isNotBlank(generatorField.type())){
                                        sql.append(fields[i].getName() + " " + generatorField.type().toLowerCase() + " ");
                                        sql.append(StringUtils.isBlank(generatorField.comment())? ",": "comment '" + generatorField.comment() + "' ,\r\n");
                                    }

                                }else{
                                    propertyToColumn.put(fields[i].getName(),generatorField.name());
                                    if(StringUtils.isNotBlank(generatorField.type())){
                                        sql.append(generatorField.name() + " " + generatorField.type().toLowerCase() + " ");
                                        boolean isNull = generatorField.isNull();
                                        if(!isNull){
                                            sql.append("not null default '" + generatorField.defaultValue() + "' ");
                                        }
                                        sql.append(StringUtils.isBlank(generatorField.comment())? ",": "comment '" + generatorField.comment() + "' ,\r\n");
                                    }
                                }
                            }
                            continue;
                        }
                        propertyToColumn.put(fields[i].getName(), fields[i].getName());
                    }
                    entityTable.setPropertyPKeyType(propertyPKeyType);
                    entityTable.setPropertyToPKey(propertyToKey);
                    entityTable.setPropertyToColumn(propertyToColumn);
                }
            }

            entityTable.setEntityToTable(entityToTable);
            String tmp = sql.toString().trim();
            if(tmp.endsWith(",")){
                tmp = tmp.substring(0,tmp.length()-1);
            }
            sql = new StringBuffer(tmp);
            sql.append("\r\n) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;\r\n");
            dbNameToSQL.put(tbName, sql.toString());
            System.out.println("\r\n==================================================");
            System.out.println(sql.toString());
            System.out.println("==================================================\r\n");
            entityTable.setDbNameToSQL(dbNameToSQL);
        }catch (Exception e){
            e.printStackTrace();
        }
        return entityTable;
    }

    /*public static void main(String[] args)throws Exception{
        String packageName = "com.java.test.MemeberInfo.java";

        String className = BeanUtils.getClassName(packageName);
        System.out.println("类名 : " + className);
        System.out.println("首字母小写 : " + BeanUtils.firstLower(className));
        System.out.println("首字母大写 : " + BeanUtils.firstUpper(className));
        String innerVar = "password";
        System.out.println("setXX : " + BeanUtils.setterName(innerVar));
        System.out.println("getXX : " + BeanUtils.getterName(innerVar));

        EntityTable entityTable = BeanUtils.constructEntityTableWithPath("com.yyt.domain.Opus");
        System.out.println("主键映射" + entityTable.getPropertyToPKey());
        System.out.println("字段映射" + entityTable.getPropertyToColumn());
        System.out.println("主键集合" + entityTable.getPropertyPKeyType());
        System.out.println("表名映射" + entityTable.getEntityToTable());

    }*/
}

