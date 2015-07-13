package stock.core.service;

import org.apache.commons.mail.*;

import domain.EmailSubject;

public class EmailService implements Runnable {
	
	
	static final String FROM_ADDRESS = "systemstock2@gmail.com";
	static final String HOST_NAME = "smtp.googlemail.com";
	static final String PASSWORD = "carros123";
	
	static String emailUserLogged;
	static String addressUserRoot;
	
	private static EmailService instance;	
	private EmailSubject subject;
	private String body;
	private String workAddress;
	
	private EmailService() {}	 
	
	public void setAddressUserRoot(String addressRoot) {
		EmailService.addressUserRoot = addressRoot;
	}
	
	public String getAddressUserRoot() {
		return EmailService.addressUserRoot;
	}
	
	public String getEmailUserLogged() {
		return EmailService.emailUserLogged;
	}
	
	public void setEmailUserLogged(String emailUserLogged) {
			EmailService.emailUserLogged = emailUserLogged;
	}
	
	public EmailSubject getSubject() {
		return subject;
	}

	public void setSubject(EmailSubject subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getWorkAddress() {
		return workAddress;
	}

	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	
	public static EmailService getInstance() {
		if (instance == null) {
			instance = new EmailService();
		}
		return instance;
	}
	
	public boolean sendSimpleEmail() {
		
		if (this.subject == null || this.body == null 
				|| this.workAddress == null || this.body.equals("") || this.workAddress.equals(""))
			return false;
		
		Email email = new SimpleEmail();
		email.setHostName(HOST_NAME);
		email.setSmtpPort(587);
		email.setAuthenticator(new DefaultAuthenticator(FROM_ADDRESS, PASSWORD));
		
		System.out.println("Enviando email -->> Subject: Estoque Abaixo do Mínimo, email: " + this.workAddress);
		long timeStart = System.currentTimeMillis();
		
		try {
			
			email.addTo(this.workAddress);		
			email.setFrom(FROM_ADDRESS);
			email.setSubject(this.subject.getValue());
			email.setMsg(this.body);
			email.setSSL(true);
			email.send();
			
		} catch (EmailException e) {
			
			System.out.println("Email não enviado -->> Subject: Estoque Abaixo do Mínimo, email: " + this.workAddress);
			e.printStackTrace();
			long time = System.currentTimeMillis() - timeStart;
			System.out.println("Tempo de execução: " + time);
			
			return false;
			
		} 
		
		System.out.println("Email enviado com sucesso -->> Subject: Estoque Abaixo do Mínimo, email: " + this.workAddress);
		long time = System.currentTimeMillis() - timeStart;
		System.out.println("Tempo de execução: " + time);
		return true;
		
	}

	@Override
	public void run() {		
		sendSimpleEmail();		
	}
	
}
