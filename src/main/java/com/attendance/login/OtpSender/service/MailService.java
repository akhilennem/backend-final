package com.attendance.login.OtpSender.service;


import com.attendance.login.OtpSender.Otpmodel.Mail;

public interface MailService 
{
	public void sendEmail(Mail mail);
}
