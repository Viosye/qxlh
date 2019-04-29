package com.qxlh.core.utils;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

public class EmailSendUtil {

    private EmailSendUtil() {

    }

    private static boolean sendMail(HttpServletRequest request, String email, String content, String title) {
        final String account = ConfigUtil.SITE_SEND_EMAIL_ACCOUNT;
        final String passWord = ConfigUtil.SITE_SEND_EMAIL_PASSWORD;
        final String smtp = ConfigUtil.SITE_SEND_EMAIL_SMTP.toUpperCase();
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", smtp);// 用户主机
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account, passWord);// 获取用户名和密码
            }
        });

        Message msg = new MimeMessage(session);
        session.setDebug(false);// 查看调试信息:true,不查看：false;
        try {
            msg.setFrom(new InternetAddress(account));
            msg.setSubject(title);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));// 多个收件人
            msg.setContent(content, "text/html;charset=utf-8");// 文本/指定文本类型，字符集
            Transport.send(msg);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            //发送失败
            return false;
        }// 发送端
        return true;
    }


    public static boolean forgetpwd(HttpServletRequest request, String email, String randomCode) {
        String siteName = "qlh";

        String title = siteName + "找回密码";
        String content = "<h4>您好，" + email + "：</h4><p>请点击下面的链接来重置您的密码<br  />" +
                "<a href='" +  "localhost:8080/user/resetpwd?email=" + email + "&token=" + randomCode + "' target='_blank'>" +
                 "user/resetpwd?email=" + email + "&token=" + randomCode + "</a><br  />" +
                "本链接30分钟内有效。<br />" +
                "(如果点击链接无反应，请复制链接到浏览器里直接打开)<p>";
        return sendMail(request, email, content, title);
    }
}