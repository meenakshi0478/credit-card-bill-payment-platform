package com.credpay.platform.service;


import com.credpay.platform.dto.BillDto;
import com.credpay.platform.model.Bill;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.model.Payment;
import com.credpay.platform.model.payload.Request.MailRequestModel;
import com.credpay.platform.model.payload.Response.MailResponseModel;
import com.itextpdf.text.DocumentException;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;


    @Autowired
    private Configuration config;


    @Autowired
    private PDFGeneratorService pdfService;

    public void sendPdfTableViaEmail(CreditCard creditCard, BillDto createdBill) throws MessagingException, IOException, DocumentException {
        // Generate PDF
        byte[] pdfBytes = pdfService.generatePdf(creditCard, createdBill);

        // Create message
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("meenakshimanirudhan@gmail.com");
        helper.setText("Hi " + creditCard.getCardholderName()+ ", <br><br>"+ " The CreditCard Bill of your Card :  <strong>" + creditCard.getCardNumber()+"</strong>"+
                " Is <strong> Rs "+createdBill.getBillAmount()+"</strong><br>"+
                "  Please close the due before  "+ createdBill.getDueDate()+"<br>"+
                " Regards CredPay Team!" , true);
        helper.setSubject("Credit Card Statement");

        // Add attachment
        helper.addAttachment("statement.pdf", new ByteArrayResource(pdfBytes));

        // Send message
        mailSender.send(message);
    }

    public void sendSimpleEmail(String toEmail,
                                String subject,
                                String body
    ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fromemail@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Send...");

    }

    public void sendPaymentStatus(CreditCard creditCard, Bill bill, Payment payment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("meenakshimanirudhan@gmail.com");
        helper.setText("Hi " + creditCard.getCardholderName()+ ", <br><br>"+ " You have successfully closed your due amount Rs:  <strong>" + bill.getBillAmount()+"</strong>"+
                " Of your credit card <strong> Rs "+creditCard.getCardNumber()+"</strong><br>"+
                "  on  "+ payment.getPaymentDate() +"<br>"+
                " Regards CredPay Team!" , true);
        helper.setSubject("Credit Card Bill Payment Status");


        // Send message
        mailSender.send(message);
        System.out.println("Mail Send...");

    }



    public MailResponseModel sendEmail(MailRequestModel request, Map<String, Object> model) {
        MailResponseModel response = new MailResponseModel();
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template t = config.getTemplate("email-template.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t,model);
            helper.setTo(request.getTo());
            helper.setText(html, true);
            helper.setSubject(request.getSubject());
            helper.setFrom("meenakshimanirudhan@gmail.com");
            mailSender.send(message);
            response.setMessage("mail send to : " + request.getTo());
            response.setStatus(Boolean.TRUE);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending failure : "+e.getMessage());
            response.setStatus(Boolean.FALSE);
        }
        return response;
    }

}


