package stock.core.impl.business;

import stock.core.application.Result;
import stock.core.impl.dao.DAOCustomer;
import domain.Customer;
import domain.DomainEntity;

public class ValidatorCustomer extends AbstractStrategyValidate{

	@Override
	public Result validate(DomainEntity domainEntity) {
		
		Result result = new Result();
		Customer customer = (Customer) domainEntity;
		result.setMessage(validateNullFields(customer));
		String message = result.getMessage();
		
		if (customer.getAddress() != null) {
			
			ValidatorAddress validatorAddress = new ValidatorAddress();
			
			if ( message == null) {
				result = validatorAddress.process(customer.getAddress());
				
			} else {
				message = message + validatorAddress.process(customer.getAddress()).getMessage();
			}				
		}
		
		if (message != null)
			return result;
		
		if (!validateEmail(customer.getEmail())) {
			result.setMessage("Formato de email invalido");
			return result;
		}
			
			
		
		if (customer.getPhone().length() > 20) {
			result.setMessage("O telefone e invalido! Ultrapassa o tamanho maximo de 20 caracteres.");
			return result;
		}
					
		if (customer.getName().length() > 45) {
			result.setMessage("O nome e invalido! Ultrapassa o tamanho maximo de 45 caracteres.");
			return result;
		}
				
		if (customer.getDocument().length() < 18 && customer.getType() == "PJ") {
			result.setMessage("O número de CNPJ informado é inválido.");
			return result;
		}
		
		if (customer.getDocument().length() < 14 && customer.getType() == "PF") {
			result.setMessage("O número de CPF informado é inválido.");
			return result;
		}
								
		DAOCustomer dao = new DAOCustomer();
		Customer c = dao.findByFieldAndValue("document", customer.getDocument());
		
		if (c != null && c.getId() != customer.getId()) {
			
			customer.setDocument("");
			result.setMessage("O documento informado ja se encontra cadastrado no sistema!");
			return result;
			
		}
		
		c = null;
		c = dao.findByFieldAndValue("email", customer.getEmail());
		
		if (c != null && c.getId() != customer.getId()) {
			
			result.setMessage("O email informado ja se encontra cadastrado no sistema!");
			return result;
			
		}
		
		return result;
		
	}

}
