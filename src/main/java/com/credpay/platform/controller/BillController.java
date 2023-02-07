package com.credpay.platform.controller;

import com.credpay.platform.PdfGenerator;
import com.credpay.platform.dto.BillDto;
import com.credpay.platform.dto.PaymentDto;
import com.credpay.platform.model.Bill;
import com.credpay.platform.model.CreditCard;
import com.credpay.platform.model.Payment;
import com.credpay.platform.model.User;
import com.credpay.platform.model.payload.BillDetailsRequestModel;
import com.credpay.platform.repository.CreditCardRepository;
import com.credpay.platform.repository.UserRepository;
import com.credpay.platform.service.BillService;
import com.credpay.platform.service.EmailSenderService;
import com.credpay.platform.service.PDFGeneratorService;
import com.credpay.platform.service.PaymentService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CreditCardRepository creditCardRepository;
    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    PDFGeneratorService pdfGeneratorService;


    @GetMapping
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> bills = billService.getAllBills();
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{userId}/all")
    public ResponseEntity<List<Bill>> getBillsByUserId(@PathVariable String userId) {
        List<Bill> bills = billService.getAllBillsById(userId);
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/generate/{creditCardId}")
    public ResponseEntity<BillDto> generateBill(@PathVariable Long creditCardId,
                                                @RequestBody BillDetailsRequestModel billDetails) {
        BillDto billDto = new BillDto();
        BeanUtils.copyProperties(billDetails, billDto);

        BillDto createdBill = billService.createBill(creditCardId, billDto);
        BillDto returnValue = new BillDto();

        CreditCard creditCard = creditCardRepository.findByUserIdAndId(createdBill.getUserId(),creditCardId);

        User user = userRepository.findByUserId(createdBill.getUserId());
        emailSenderService.sendSimpleEmail(user.getEmail()," CreditCard Bill",
                "Hi " + user.getFirstName() + " The CreditCard Bill of your CredirCard " + creditCard.getCardNumber()+
                        " Is Rs "+createdBill.getBillAmount()+
                        " Please close the due before  "+ createdBill.getDueDate()+
                        " Regards CredPay Team!" );
        BeanUtils.copyProperties(createdBill, returnValue);
        return ResponseEntity.ok(returnValue);
    }

    @PostMapping("/send")
    public ResponseEntity<BillDto> generateBillForAll(@PathVariable Long creditCardId,
                                                @RequestBody BillDetailsRequestModel billDetails) {
        BillDto billDto = new BillDto();
        BeanUtils.copyProperties(billDetails, billDto);

        BillDto createdBill = billService.createBill(creditCardId, billDto);
        BillDto returnValue = new BillDto();

        CreditCard creditCard = creditCardRepository.findByUserIdAndId(createdBill.getUserId(),creditCardId);

        User user = userRepository.findByUserId(createdBill.getUserId());
        emailSenderService.sendSimpleEmail(user.getEmail()," CreditCard Bill",
                "Hi " + user.getFirstName() + " The CreditCard Bill of your CredirCard " + creditCard.getCardNumber()+
                        " Is Rs "+createdBill.getBillAmount()+
                        " Please close the due before  "+ createdBill.getDueDate()+
                        " Regards CredPay Team!" );
        BeanUtils.copyProperties(createdBill, returnValue);
        return ResponseEntity.ok(returnValue);
    }



    @PreAuthorize("#userId == principal.userId")
    @PostMapping("/pay/{id}/{userId}")
    public ResponseEntity<PaymentDto> payBill(@PathVariable Long id, @PathVariable String userId,
                                              @RequestBody PaymentDto paymentDto) {
        PaymentDto processedPayment = paymentService.processPayment(id, userId,paymentDto);
        return new ResponseEntity<>(processedPayment, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/pdf/generate")
    public void generatePDF(@RequestParam String userId, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename= "+ userId + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        List < Bill > listofStudents = billService.getAllBillsById(userId);
        PdfGenerator generator = new PdfGenerator();
        generator.generate(listofStudents, response);
    }

   /* @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/pdf/generate/all")
    public void generateBillForAll( HttpServletResponse response) throws IOException, MessagingException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        List<User>users = userRepository.findAll();
        for (User user : users) {
            String headervalue = "attachment; filename= " + user.getFirstName() + currentDateTime + ".pdf";
            response.setHeader(headerkey, headervalue);
            List<Bill> listofBills = billService.getAllBillsById(user.getUserId());
            PdfGenerator generator = new PdfGenerator();
            generator.generate(listofBills, response);
            emailSenderService.generateAndSendPDF(user.getEmail(), "Bill", "Bill statement");

        }
    }*/

}

