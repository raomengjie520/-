package com.zuoye.newspaper;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmailTask implements Runnable {
    private final String recipientEmail;
    private final String subject;
    private final String messageBody;

    // 构造函数，初始化收件人邮箱、主题和内容
    public SendEmailTask(String recipientEmail, String subject, String messageBody) {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.messageBody = messageBody;
    }

    @Override
    public void run() {
        try {
            // 发件人邮箱和授权码
            final String senderEmail = "1587128824@qq.com";
            final String senderAuthCode = "bzspjbrmqwwggbjg"; // 替换为从QQ邮箱获取的授权码

            // 配置SMTP服务器
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.qq.com"); // QQ邮箱SMTP服务器地址
            props.put("mail.smtp.port", "465"); // QQ邮箱SSL端口
            props.put("mail.smtp.auth", "true"); // 需要身份验证
            props.put("mail.smtp.ssl.enable", "true"); // 启用SSL

            // 创建会话
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderAuthCode);
                }
            });

            // 创建邮件消息
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail)); // 发件人邮箱
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // 收件人邮箱
            message.setSubject(subject); // 邮件主题
            message.setText(messageBody); // 邮件内容

            // 发送邮件
            Transport.send(message);
            System.out.println("邮件发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("邮件发送失败：" + e.getMessage());
        }
    }
}
