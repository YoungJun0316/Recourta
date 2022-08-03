package com.ssafy.recourta.domain.user.service;


import com.ssafy.recourta.domain.user.dto.request.UserRequest;
import com.ssafy.recourta.domain.user.dto.response.UserResponse;
import com.ssafy.recourta.domain.user.entity.User;
import com.ssafy.recourta.global.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@Service
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;



    private MimeMessage createMessage(String email) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("Recourta 인증번호입니다.");
        // 변경 예정
        String  code = UUID.randomUUID().toString().replaceAll("-","").substring(0, 9);

        String msg="";
        msg+= "<div style='margin:100px;'>";
        msg+= "<h1> 안녕하세요 Recourta입니다. </h1>";
        msg+= "<br>";
        msg+= "<p>아래 인증코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msg+= "<br>";
        msg+= "<p>감사합니다!<p>";
        msg+= "<br>";
        msg+= "<div align='center' style='border:1px solid black; font-family:verdana';><br>";
        msg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg+= "<div style='font-size:130%'>";
        msg+= "CODE : <strong>";
        msg+= code+"</strong><div><br/> ";
        msg+= "</div>";

        //message.setText("이메일 인증코드: "+code, "utf-8", "html");
        message.setText(msg, "utf-8", "html");

        message.setFrom(new InternetAddress("recourta@naver.com","리코타"));

        return  message;
    }

    public void sendMail(String email) throws Exception{
        try{
            MimeMessage mimeMessage = createMessage(email);


            javaMailSender.send(mimeMessage);
        }catch (MailException mailException){
            mailException.printStackTrace();
            throw   new IllegalAccessException();
       }
    }

    public UserResponse.isSuccess registMail(String email) throws  Exception{


//        if(passwordEncoder.matches(request.getNowPw(), user.getPassword())){
//            user.setPassword(passwordEncoder.encode(request.getNewPw()));
//            userRepository.save(user);
//            return UserResponse.isSuccess.build(false);
//        }  이메일이 이미 있으면(가입한 사람) fail 보내기/// null이면

        sendMail(email);
        return UserResponse.isSuccess.build(true);
    }
}