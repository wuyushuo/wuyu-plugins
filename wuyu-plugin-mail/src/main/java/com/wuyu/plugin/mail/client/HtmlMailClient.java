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
package com.wuyu.plugin.mail.client;

import com.wuyu.plugin.mail.bean.MailSmtpBean;
import com.wuyu.plugin.mail.except.MailException;
import com.wuyu.plugin.utils.CollectionUtil;
import com.wuyu.plugin.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.*;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class HtmlMailClient {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlMailClient.class);

    private String username = null;
    private String password = null;
    private boolean isDebug = false;
    private boolean isAuth = true;
    private List<MailSmtpBean> smtpBeans = null;

    public HtmlMailClient(String username, String password, List<MailSmtpBean> smtpBeans) {
        this(true, true, username, password, smtpBeans);
    }

    public HtmlMailClient(boolean isAuth, boolean isDebug,  String username, String password, List<MailSmtpBean> smtpBeans) {
        this.isDebug = isDebug;
        this.isAuth = isAuth;
        this.username = username;
        this.password = password;
        this.smtpBeans = smtpBeans;
    }

    public boolean sendHtmlMail(String title, String content, String ... receivers) {
        if(receivers.length == 0){
            throw new MailException(MailException.ErrorStatus.mailReceiverIsNull);
        }
        return sendHtmlMail(title, content, Arrays.asList(receivers));
    }

    public boolean sendHtmlMail(String title, String content, List<String> receivers) {
        if(CollectionUtil.isEmpty(receivers)){
            throw new MailException(MailException.ErrorStatus.mailReceiverIsNull);
        }
        MailSmtpBean smtpBean = this.getMailSmtpBean(this.username);
        if(null == smtpBean){
            throw new MailException(MailException.ErrorStatus.unknownSmtpServer);
        }
        return HtmlMailKernel.getInstance().sendHtmlMail(this.isAuth, this.isDebug,
                smtpBean.getSmtpHost(), smtpBean.getSmtpPort(),
                this.username, this.password,
                title,content, receivers);
    }

    public boolean sendHtmlMail(String title, String content, List<String> receivers, List<String> cces) {
        MailSmtpBean smtpBean = this.getMailSmtpBean(this.username);
        if(null == smtpBean){
            throw new MailException(MailException.ErrorStatus.unknownSmtpServer);
        }
        return HtmlMailKernel.getInstance().sendHtmlMail(this.isAuth, this.isDebug,
                smtpBean.getSmtpHost(), smtpBean.getSmtpPort(),
                this.username, this.password,
                title,content, receivers, cces);
    }

    public boolean sendHtmlMail(String title, String content, List<String> receivers, List<String> cces, List<File> attachFiles) {
        MailSmtpBean smtpBean = this.getMailSmtpBean(username);
        if(null == smtpBean){
            throw new MailException(MailException.ErrorStatus.unknownSmtpServer);
        }
        return HtmlMailKernel.getInstance().sendHtmlMail(this.isAuth, this.isDebug,
                smtpBean.getSmtpHost(), smtpBean.getSmtpPort(),
                this.username, this.password,
                title,content, receivers, cces, attachFiles);
    }

    /**
     * parser smtp bean from mail address
     * @param emailAddr
     * @return
     */
    public MailSmtpBean getMailSmtpBean(String emailAddr) {
        if(CollectionUtil.isEmpty(this.smtpBeans)){
            throw new MailException(MailException.ErrorStatus.noConfigSmtpServerAddr);
        }
        if(null == emailAddr){
            return null;
        }
        String mailDomain = this.getMailDomainName(emailAddr);
        LOG.debug("mail send domain:" + mailDomain);
        for (MailSmtpBean tmp : this.smtpBeans) {
            if (tmp.getName().equals(mailDomain)) {
                return tmp;
            }
        }
        return null;
    }

    /**
     * get mail domain from mail address
     * @param emailAddr mail address
     * @return domain for mail server
     */
    private String getMailDomainName(String emailAddr) {
        if (StringUtil.isBlank(emailAddr)) {
            return StringUtil.EMPTY;
        }
        int firstIndex = StringUtil.indexOf(emailAddr, "@");
        int secondIndex = StringUtil.lastIndexOf(emailAddr, ".");
        return StringUtil.substring(emailAddr, firstIndex + 1, secondIndex);
    }

}
