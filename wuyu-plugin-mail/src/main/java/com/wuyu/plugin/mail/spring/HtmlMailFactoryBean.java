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
package com.wuyu.plugin.mail.spring;

import com.wuyu.plugin.mail.bean.MailSmtpBean;
import com.wuyu.plugin.mail.client.HtmlMailClient;
import com.wuyu.plugin.mail.except.MailException;
import com.wuyu.plugin.utils.CollectionUtil;
import com.wuyu.plugin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import java.util.List;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class HtmlMailFactoryBean implements FactoryBean {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlMailFactoryBean.class);

    private String username = null;
    private String password = null;
    private boolean isDebug = false;
    private boolean isAuth = true;
    private List<MailSmtpBean> smtpBeans = null;


    @Override
    public Object getObject() throws Exception {
        if(CollectionUtil.isEmpty(smtpBeans)){
            throw new MailException(MailException.ErrorStatus.noConfigSmtpServerAddr);
        }
        if(StringUtil.isBlank(username)){
            throw new MailException(MailException.ErrorStatus.illegalAuthenticator);
        }
        return new HtmlMailClient(isAuth, isDebug,username,password,smtpBeans);
    }

    @Override
    public Class<?> getObjectType() {
        return HtmlMailClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    public void setSmtpBeans(List<MailSmtpBean> smtpBeans) {
        this.smtpBeans = smtpBeans;
    }
}
