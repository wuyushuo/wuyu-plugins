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
package com.wuyu.plugin.mail.except;

/**
 * organization <a href="http://www.wuyushuo.com">www.wuyushuo.com</a>
 * created on 2016/02/04 by <strong>elon su</strong>
 * email addr (<a href='mailto:git_wuyu@163.com'></>git_wuyu@163.com</a>)
 * @version 1.0.0
 */
public class MailException extends RuntimeException{

    private int errorCode;

    private Throwable exception;

    private String message;

    public MailException(int errorCode) {
        this.errorCode = errorCode;
    }

    public MailException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public MailException(int errorCode,String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public MailException(Throwable cause, int errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public MailException(ErrorStatus errorStatus) {
        this(errorStatus.message, errorStatus.getErrorCode());
    }

    public enum ErrorStatus{

        illegalAuthenticator(500010,"illegal authenticator"),
        unknownSmtpServer(500020,"unknown SmtpServer"),
        mailReceiverIsNull(500030,"mail receiver is null"),
        noConfigSmtpServerAddr(500030, "no config smtp server address");

        private int errorCode;
        private String message;

        ErrorStatus(int errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
        }

        public int getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(int errorCode) {
            this.errorCode = errorCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
