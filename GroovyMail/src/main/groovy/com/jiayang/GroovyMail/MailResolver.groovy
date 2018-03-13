package com.jiayang.GroovyMail

import com.sun.mail.util.MailSSLSocketFactory

import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.internet.MimeMessage.RecipientType;
class MailResolver {
	def data
	def Properties mailProps=new Properties()
	def String mailServer="smtp.qq.com"
	def String username,password
	def messages=[] as LinkedList<MimeBodyPart>
	def Session session
	def MimeMessage mm
	def Date sendDate
	def Multipart mp=new MimeMultipart();
	def propertyMissing(name)
	{
		return data[name]
	}
	def MailResolver() 
	{
		mailProps.setProperty("mail.debug", "true")
		mailProps.setProperty("mail.smtp.auth", "true")
		mailProps.setProperty("mail.transport.protocol", "smtp")
		def MailSSLSocketFactory sf = new MailSSLSocketFactory()
		sf.setTrustAllHosts(true)
	    mailProps.setProperty("mail.smtp.ssl.enable", "true")
	    mailProps.put("mail.smtp.ssl.socketFactory", sf)
		mailProps.setProperty("mail.smtp.host", this.mailServer)
		this.session=Session.getDefaultInstance(this.mailProps)
		this.mm=new MimeMessage(session)
		}
	 def mailServer(String mailServerName) {
		 mailProps.setProperty("mail.smtp.host", mailServerName)
		 this.session=Session.getDefaultInstance(this.mailProps)
		 this.mm=new MimeMessage(session)
	 }
	 def from(String address)
	 {
		 this.mm.setFrom(new InternetAddress(address))
	 }
	 def to(String toAddress)
	{
		this.mm.addRecipient(RecipientType.TO, new InternetAddress(toAddress));
	}
	def cc(String toAddress)
	{
		this.mm.addRecipient(RecipientType.CC, new InternetAddress(toAddress));
	}
	def bcc(String type,String toAddress)
	{
		this.mm.addRecipient(RecipientType.BCC, new InternetAddress(toAddress));
	}
	def recipient(String type,String toAddress)
	{
		type=type.toUpperCase()
		this.mm.addRecipient(RecipientType[type], new InternetAddress(toAddress));
	}
	def setRecipient(String type,String toAddress)
	{
		type=type.toUpperCase()
		this.mm.setRecipient(RecipientType.(type), new InternetAddress(toAddress));
	}
	def subject(String subject)
	{
		this.mm.setSubject(subject)
	}
	def message(mes)
	{
		if(mes as String)
		{
			if(mes.trim().startsWith("!html!"))
			{
				MimeBodyPart mbp=new MimeBodyPart()
				mbp.setContent(mes.substring(6),"text/html;charset=utf-8")
			    messages.add(mbp)
			}
			else
			{
				MimeBodyPart mbp=new MimeBodyPart()
				mbp.setText(mes)
				messages.add(mbp)
			}
				
		}
		else if (mes as File) {
			MimeBodyPart mbp=new MimeBodyPart()
			FileDataSource fds=new FileDataSource(mes)
			mbp.setDataHandler(new DataHandler(fds))
			String filename=mes.getName()
			filename=filename.substring(0,filename.lastIndexOf("."))
			+UUID.randomUUID().toString()+filename.substring(filename.lastIndexOf("."))
			mbp.setFileName(filename)
			messages.add(mbp)
		}
		}
		def username(String username)
		{
			this.username=username
		}
		def password(String password)
		{
			this.password=password
		}
		def sendDate(date)
		{
			this.sendDate=date
		}
		def send(data)
		{
			if(data.username)
			   this.username=data.username
			if(data.password)
			   this.password=data.password
			if(!(this.username&&this.password))
			   throw new Error("用户名密码为空")
			   def mp=new MimeMultipart()
			   messages.each {
				mp.addBodyPart(it)
			   }
			   messages.clear()
			   mm.setSentDate(this.sendDate?this.sendDate:new Date());
			   mm.setContent(mp)  
			Transport ts=session.getTransport();
			ts.connect(this.username,this.password);
			ts.sendMessage(mm, mm.getAllRecipients());
			ts.close();
		}
	}

