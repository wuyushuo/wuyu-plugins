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
package com.wuyu.plugin.mail.client;

import com.wuyu.plugin.mail.bean.PopupAuthenticator;
import com.wuyu.plugin.mail.valid.MailAddrValid;
import com.wuyu.plugin.utils.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class HtmlMailKernel {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlMailClient.class);

    private static HtmlMailKernel instance = new HtmlMailKernel();

    private HtmlMailKernel() {

    }

    public static HtmlMailKernel getInstance(){
        return HtmlMailKernel.instance;
    }


    public boolean sendHtmlMail(String smtpHost, int smtpPort, String username, String password, String title, String content, List<String> receivers) {
        return sendHtmlMail(true, true, smtpHost, smtpPort, username, password, title, content, receivers);
    }

    public boolean sendHtmlMail(boolean isAuth, boolean isDebug, String smtpHost, int smtpPort, String username, String password, String title, String content, List<String> receivers) {
        return sendHtmlMail(isAuth, isDebug, smtpHost, smtpPort, username, password, title, content, receivers, null);
    }

    public boolean sendHtmlMail(String smtpHost, int smtpPort, String username, String password, String title, String content, List<String> receivers, List<String> cces) {
        return sendHtmlMail(true, true,smtpHost, smtpPort, username, password, title, content, receivers, cces);
    }

    public boolean sendHtmlMail(boolean isAuth, boolean isDebug, String smtpHost, int smtpPort, String username, String password, String title, String content, List<String> receivers, List<String> cces) {
        Session session = HtmlMailKernel.getInstance().sessionAuth(username,password, smtpHost, smtpPort, isAuth, isDebug);
        Message message = null;
        if(CollectionUtil.isEmpty(cces)){
            message = makeNoAttachmentMail(session, title, content, username, receivers, cces);
        }else{
            message = makeNoAttachmentMail(session, title, content, username, receivers);
        }
        try {
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean sendHtmlMail(String smtpHost, int smtpPort, String username, String password, String title, String content, List<String> receivers, List<String> cces,List<File> attachFiles) {
        return sendHtmlMail(true, true, smtpHost, smtpPort, username, password, title, content, receivers, cces, attachFiles);
    }

    public boolean sendHtmlMail(boolean isAuth, boolean isDebug, String smtpHost, int smtpPort, String username, String password, String title, String content, List<String> receivers, List<String> cces,List<File> attachFiles) {
        Session session = sessionAuth(username, password, smtpHost, smtpPort, isAuth, isDebug);
        Message message = null;
        if(CollectionUtil.isEmpty(cces)){
            message = makeAttachmentMail(session, title, content, username, receivers, cces, attachFiles);
        }else{
            message = makeAttachmentMail(session, title, content, username, receivers, attachFiles);
        }
        try {
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }


    public Session sessionAuth(String username,String password,String smtpHost, int smtpPort, boolean isAuth, boolean isDebug){
        PopupAuthenticator auth = null;
        if (isAuth) {
            auth = new PopupAuthenticator(username, password);
        }
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", isAuth ? "true" : "false");
        props.setProperty("mail.transport.protocol", "auth");
        props.setProperty("mail.smtp.host", smtpHost);
        props.setProperty("mail.smtp.port", String.valueOf(smtpPort));
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(isDebug);
        return session;
    }

    /**
     * html mail with no attachment file
     * @param session mail config session
     * @param title title of mail
     * @param content content of mail
     * @param from mail sender
     * @param receivers mail receivers
     * @return Message
     */
    public Message makeNoAttachmentMail(Session session, String title, String content, String from, List<String> receivers) {
        return makeNoAttachmentMail(session, title, content, from, receivers, true);
    }


    /**
     * html mail with no attachment file
     * @param session mail config session
     * @param title title of mail
     * @param content content of mail
     * @param from mail sender
     * @param receivers mail receivers
     * @param isHtmlMail is html mail
     * @return Message
     */
    public Message makeNoAttachmentMail(Session session, String title, String content, String from, List<String> receivers, boolean isHtmlMail) {
        return makeNoAttachmentMail(session, title, content, from, receivers, null, isHtmlMail);
    }

    /**
     * html mail with no attachment file
     * @param session mail config session
     * @param title title of mail
     * @param content content of mail
     * @param from mail sender
     * @param receivers mail receivers
     * @param cces multi cc
     * @return Message
     */
    public Message makeNoAttachmentMail(Session session, String title, String content, String from, List<String> receivers,List<String> cces){
        return makeNoAttachmentMail(session,title,content,from,receivers,cces,true);
    }

    /**
     * html mail with no attachment file
     * @param session mail config session
     * @param title title of mail
     * @param content content of mail
     * @param from mail sender
     * @param receivers mail receivers
     * @param cces multi cc
     * @param isHtmlMail is html mail
     * @return Message
     */
    public Message makeNoAttachmentMail(Session session, String title, String content, String from, List<String> receivers, List<String> cces,
                                         boolean isHtmlMail) {
        Message message = new MimeMessage(session);
        try {
            LOG.debug("mail's title is {}", title);
            message.setSubject(title);

            LOG.debug("mail's content is {}", content);
            if (isHtmlMail) {
                message.setContent(content, "text/html;charset=utf-8");
            } else {
                //普通邮件
                message.setText(content);
            }

            LOG.debug("mail`s sender is {}.", from);
            message.setFrom(new InternetAddress(from));

            Address[] tos = validMailAddress(receivers);
            LOG.debug("mail`s receivers is {}.", receivers);
            message.setRecipients(Message.RecipientType.TO, tos);

            Address[] ccs = validMailAddress(cces);
            LOG.debug("mail`s cces is {}.", ccs.toString());

            message.setSentDate(new Date());

        } catch (MessagingException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }

        return message;
    }


    /**
     * html mail with attachment file
     * @param session mail config session
     * @param title title of mail
     * @param content content of mail
     * @param from mail sender
     * @param receivers mail receivers
     * @return Message
     */
    public Message makeAttachmentMail(Session session, String title, String content, String from, List<String> receivers,List<File> attachFiles) {
        return makeAttachmentMail(session, title, content, from, receivers, null, attachFiles);
    }


    /**
     * html mail with attachment file
     * @param session mail config session
     * @param title title of mail
     * @param content content of mail
     * @param from mail sender
     * @param receivers mail receivers
     * @param cces multi cc
     * @param attachFiles attachment files
     * @return Message
     */
    public Message makeAttachmentMail(Session session, String title, String content, String from, List<String> receivers,List<String> cces, List<File> attachFiles) {
        Message message = new MimeMessage(session);

        try {
            LOG.debug("mail's title is {}", title);
            message.setSubject(title);

            // add html content
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            mimeBodyPart.setContent(content, "text/html;charset=utf-8");
            multipart.addBodyPart(mimeBodyPart);
            if (attachFiles != null && !attachFiles.isEmpty()) {
                //存在附件
                for (Iterator<File> it = attachFiles.iterator(); it.hasNext(); ) {
                    mimeBodyPart = new MimeBodyPart();
                    File attachment = it.next();
                    //得到数据源
                    FileDataSource fds = new FileDataSource(attachment);
                    //得到附件本身并至入BodyPart
                    mimeBodyPart.setDataHandler(new DataHandler(fds));
                    //得到文件名同样至入BodyPart(附件名乱码解决方案MimeUtility.encodeText(name))
                    mimeBodyPart.setFileName(MimeUtility.encodeText(attachment.getName()));
                    multipart.addBodyPart(mimeBodyPart);
                }
            }
            LOG.debug("mail's content is {}", content);
            message.setContent(multipart);

            LOG.debug("mail`s sender is {}.", from);
            message.setFrom(new InternetAddress(from));

            Address[] tos = validMailAddress(receivers);
            LOG.debug("mail`s receivers is {}.", receivers);
            message.setRecipients(Message.RecipientType.TO, tos);

            Address[] ccs = validMailAddress(cces);
            LOG.debug("mail`s cces is {}.", ccs);

            if(null != ccs && ccs.length>0){
                message.setRecipients(Message.RecipientType.CC, ccs);
            }

            LOG.debug("mail`s end date {}.", receivers);
            message.setSentDate(new Date());

        } catch (MessagingException e) {
            LOG.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            LOG.error(e.getLocalizedMessage() ,e);
        }
        return message;
    }


    /**
     * valid mail address filtered error mail address
     * @param multiMailAddress
     * @return filtered mail address
     * @throws AddressException
     */
    public Address[] validMailAddress(List<String> multiMailAddress) throws AddressException {
        if(null != multiMailAddress && multiMailAddress.size() > 0){
            Address[] tos = new InternetAddress[multiMailAddress.size()];
            for (int i = 0; i < multiMailAddress.size(); i++) {
                String receiver = multiMailAddress.get(i);
                if (MailAddrValid.isEmail(receiver)) {
                    tos[i] = new InternetAddress(receiver);
                }
            }
            return tos;
        }
        return new Address[0];
    }

    public InternetAddress[] internetAddresses(List<String> addresses){
        if(CollectionUtil.isEmpty(addresses)){
            return null;
        }
        InternetAddress[] tmp = new InternetAddress[addresses.size()];
        for(int i=0; i<addresses.size(); i++){
            try {
                tmp[i] = new InternetAddress(addresses.get(i));
            } catch (AddressException e) {
                LOG.error(e.getLocalizedMessage(),e);
            }
        }
        return tmp;
    }

}
