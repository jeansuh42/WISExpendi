package com.wiselab.WISExpendi.Common;

import com.wiselab.WISExpendi.DTO.ExpenditureSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.mail.*;
import javax.mail.internet.MimeMessage;

@Component
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendMailAttachment(File file, ExpenditureSheet sheetData) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        LocalDate date = LocalDate.parse(sheetData.getInputDate());
        String lastDate = date.withDayOfMonth(date.lengthOfMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();

        String subject = lastDate + "_" + sheetData.getPaymentBasis().split("경비")[0] + "_경비명세표_" + sheetData.getCellList().get(0).getProjectNm() + "_" + sheetData.getName();
        // 20221131_개인_경비명세표_OK저축은행_서은빈

        helper.setTo(sheetData.getMailAdd());
        helper.setSubject(subject);
        helper.setText("요청받은 경비 명세표를 송신합니다.");

        FileSystemResource fileResource = new FileSystemResource(file);
        helper.addAttachment(subject + ".xlsx", fileResource);
        javaMailSender.send(message);

    }

}