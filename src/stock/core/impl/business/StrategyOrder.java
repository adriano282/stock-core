package stock.core.impl.business;

import java.io.UnsupportedEncodingException;

import domain.DomainEntity;
import domain.EmailSubject;
import domain.Order;
import domain.Product;
import stock.core.application.Result;
import stock.core.service.EmailService;

public class StrategyOrder extends AbstractStrategyProcess {

	private EmailService emailService = EmailService.getInstance();
	
	@Override
	public Result execute(DomainEntity domainEntity)  {
		
		Result result = new Result();
		Order order = (Order) domainEntity;
		Product product = order.getProduct();
		
		if (product.getQuantity().compareTo(product.getQuantityMinimum()) < 0) {
						
			String data = (new java.text.SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date(System.currentTimeMillis()))); 
			
			String body = "O produto abaixo esta abaixo do estoque minimo.\n\n\n"
					+ "Nome do Produto: " + product.getName() + "\n"
					+ "Código: " + product.getCode() + "\n"
					+ "Quantidade Atual: " + product.getQuantity() +"\n"
					+ "Estoque Minimo: " + product.getQuantityMinimum() + "\n"					
					+ "Data da Ultima Compra: "	+ data + "\n"
					+ "Quantidade da ultima compra: "
					+ order.getQuantity();
			
			try {
				body = new String(body.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
			
			if (emailService.getEmailUserLogged() != null && !emailService.getEmailUserLogged().equals("")) 
				SendEmail(body, emailService.getEmailUserLogged());
			
			
			if (emailService.getAddressUserRoot() != null && !emailService.getAddressUserRoot().equals(emailService.getEmailUserLogged()) 
					&& !emailService.getAddressUserRoot().equals(""))
				SendEmail(body, emailService.getAddressUserRoot());				
			
		}
		
		result.setEntityFiltered(order);
		return result;

	}
	
	private void SendEmail(String body, String to) {
		
		emailService.setSubject(EmailSubject.STOCK_LOWER);
		emailService.setBody(body);
		emailService.setWorkAddress(to);
		
		Thread t1 = new Thread(emailService);
		t1.start();
		
	}

}
