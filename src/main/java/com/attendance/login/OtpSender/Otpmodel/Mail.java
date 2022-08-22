package com.attendance.login.OtpSender.Otpmodel;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Mail 
{
	private String mailFrom;
    private String mailTo;
    private String mailSubject;
    private String mailContent;
    private String contentType = "text/plain";
    
    public Date getMailSendDate() {
        return new Date();
    }

    public void setMailContent(String message, String valueOf) {
    }
}
