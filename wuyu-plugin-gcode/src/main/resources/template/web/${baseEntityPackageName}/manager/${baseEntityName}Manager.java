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
package ${genPackage}.manager;

import ${baseEntityPackageName}.${baseEntityName};
import com.wuyu.plugin.pager.Page;
import java.util.List;
import java.util.Set;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public interface ${baseEntityName}Manager {

    /**
     * insert and return primary key
     * @param entity new object
     * @return insert and return primary key
     */
    public abstract ${baseEntityName} insert(${baseEntityName} entity);

    /**
     * update object by object hold primary key
     * @param entity entity dependence original primary key fulled with new data
     * @return effect row numbers
     */
    public abstract int update(${baseEntityName} entity);

    /**
     * delete object by primary key
     * @param primaryKey primary key of object
     * @return effect row numbers
     */
    public abstract int delete(#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end primaryKey);

    /**
     * delete object by collection of primary key
     * @param primaryKeys primary key collection of multi object
     * @return effect row numbers
     */
    public abstract int deleteMulti(Set<#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end> primaryKeys);

    /**
     * delete object by condition
     * @param entity condition of object
     * @return effect row numbers
     */
    public abstract int deleteObjects(${baseEntityName} entity);

    /**
     * select object by primaryKey
     * @param primaryKey primary key of object
     * @return result object match query condition
     */
    public abstract ${baseEntityName} select(#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end primaryKey);

    /**
     * select object by collection of primaryKey
     * @param primaryKeys primary key collection of multi object
     * @return object collection result
     */
    public abstract List<${baseEntityName}> selectMulti(Set<#foreach($propertyToPKey in $baseEntityTable.getPropertyPKeyType().entrySet())$propertyToPKey.value#end> primaryKeys);

    /**
     * select object by condition
     * @param entity condition of object
     * @return object collection result
     */
    public abstract List<${baseEntityName}> selectObjects(${baseEntityName} entity);

    /**
     * query page object collection by condition
     * @param entity condition of object
     * @param offset page.offset
     * @param size page.size
     * @return page object collection result
     */
    public abstract Page<${baseEntityName}> selectPage(${baseEntityName} entity, long offset, int size);

    /**
    * query total number by condition
    * @param entity  condition of object
    * @return total number
    */
    public abstract long selectTotal(${baseEntityName} entity);
}
