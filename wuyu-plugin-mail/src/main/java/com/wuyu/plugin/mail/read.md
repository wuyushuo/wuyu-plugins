<dependency>
    <groupId>com.wuyushuo</groupId>
    <artifactId>wuyu-plugin-mail</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
===

> 对发送邮件进行封装，依赖smtp和mail api

## 使用
1. Java代码
```java

    public static void main(String [] args){
        List<MailSmtpBean> lists = new ArrayList<MailSmtpBean>();
        MailSmtpBean bean1 = new MailSmtpBean();
        bean1.setName("qq");
        bean1.setSmtpHost("smtp.qq.com");
        bean1.setSmtpPort(25);

        MailSmtpBean bean2 = new MailSmtpBean();
        bean2.setName("sina");
        bean2.setSmtpHost("smtp.sina.com");
        bean2.setSmtpPort(25);

        lists.add(bean1);
        lists.add(bean2);

        HtmlMailClient htmlMailTool = new HtmlMailClient("dennisit@sina.com","xxx@",lists);

        List<String> to = Arrays.asList("1325103287@qq.com", "799089378@qq.com");
        List<String> cc = Arrays.asList("1325103287@qq.com");

        List<File> attachfiles = new ArrayList<File>();
        attachfiles.add(new File("D:/report_data2.txt"));
        attachfiles.add(new File("D:/Documents/read.txt"));

        boolean flag = htmlMailTool.sendHtmlMail("测试", "测试内容<font color='red'>测试数据</red>,baidu <a href='www.baidu.com'>百度</a>",to,cc);
        System.out.println(flag);

        //InternetAddress[] addr = htmlMailTool.internetAddresses(to);
        //System.out.println(addr);
    }
...
```
2. 默认支持以下邮箱发送
    - 163
    - Sina（.com和.cn的配置一样）
    - Sohu
    - Gmail
    - QQ
3. 如果需要扩展新的邮件发送服务器，如下配置，spring会按照类型扫描：
    ```xml
    <bean id="mail_hotmail" class="com.wuyu.plugin.mail.bean.MailSmtpBean">
        <property name="name">
            <value>hotmail</value>
        </property>
        <property name="smtp">
            <value>smtp.live.com</value>
        </property>
        <property name="port">
            <value>25</value>
        </property>
        <property name="description">
            <value>hotmail的mail配置</value>
        </property>
    </bean>
    ```
