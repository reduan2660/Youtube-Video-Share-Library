package com.ytvideoshare.backend.Email;

import com.ytvideoshare.backend.domain.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j @Service
public class EmailService {

    @Autowired private JavaMailSender javaMailSender;

    /**
     * Sends verification url to the specified email
     * @param user AppUser object
     * @param siteurl String object
     */
    @Async
    public void sendVerificationCode(AppUser user, String siteurl)  {

        String toAddress = user.getEmail();
        String fromAddress = "noreply@alvereduan.me";
        String senderName = "YT-Video-Share-Library";
        String subject = "Verification Code";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "YT-Video-Share-Library";

        content = content.replace("[[name]]", user.getName());
        String verifyURL = "" + siteurl + "/verify/email/[[EMAIL]]/code/" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        content = content.replace("[[EMAIL]]", user.getEmail());

        try{
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);

            helper.setTo(toAddress);
            helper.setFrom(fromAddress);
            helper.setSubject(subject);
            helper.setText(content, true);

            javaMailSender.send(msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
