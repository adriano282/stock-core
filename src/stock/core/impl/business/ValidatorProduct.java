package stock.core.impl.business;

import java.math.BigDecimal;

import stock.core.application.Result;
import stock.core.impl.dao.DAOProduct;
import domain.DomainEntity;
import domain.Product;
import domain.TimeCover;

public class ValidatorProduct extends AbstractStrategyValidate{

	@Override
	public Result validate(DomainEntity domainEntity) {
		
		Result result = new Result();
		Product p = (Product) domainEntity;
		String message =  validateNullFields(p);
				
		if (message != null) {
			result.setMessage(message);
			return result;
		}
		
		message = validateNullFields(p.getTimeCover());
		if (message != null) {
			result.setMessage(message);
			return result;
		}
		
		message = validateNullFields(p.getMake());
		if (message != null) {
			result.setMessage(message);
			return result;
		}
		
		DAOProduct dao = new DAOProduct();
		
		if (!validateUnitMeasure(p.getUnitMeasure())) {
			result.setMessage("Unidade de medida inválida!");
			return result;
		}
		
		if (!validateBarcode(p.getBarcode())) {
			
			result.setMessage("Formato inválido de código de barras. Deve possuir 13 dígitos numéricos de 0 à 9");
			return result;
		}
		
		Product pr = dao.findByCode(p.getCode());
		
		if ( pr != null && pr.getId() != p.getId()) {
			
			result.setMessage("O codigo " + p.getCode() + " ja está cadastrado!");
			return result;				
		} 
		
		pr = null;
		pr = dao.findByBarcode(p.getBarcode());
		
		if ( pr != null && pr.getId() != p.getId()) {
			result.setMessage("O codigo de barras deve ser unico. Este codigo ja se encontra cadastrado.");
			return result;
		}
		
		if (p.getQuantity().compareTo(p.getMaxQuantity()) > 0) {
			result.setMessage("A quantidade digitada é maior que o estoque máximo permitido.");
			return result;
		}
		
		if ( (p.getPrice() != null && p.getPrice().compareTo(new BigDecimal(0.00)) < 0)) {
			result.setMessage("Formato de preço inválido! O preço nao pode ter um valor negativo");
			return result;
		}
		
		if ( (p.getQuantity() != null && p.getQuantity().compareTo(new BigDecimal(0.00)) < 0)) {
			result.setMessage("Formato da quantidade inválida! A quantidade nao pode ser um valor negativo");
			return result;
		}
		
		if ( (p.getQuantityMinimum() != null && p.getQuantityMinimum().compareTo(new BigDecimal(0.00)) < 0)) {
			result.setMessage("Formato da quantidade minima inválida! A quantidade minima nao pode ser negativa");
			return result;
		}
		
		if ( (p.getMaxQuantity() != null && p.getMaxQuantity().compareTo(new BigDecimal(0.00)) < 0)) {
			result.setMessage("Formato da quantidade minima inválida! A quantidade máxima nao pode ser negativa");
			return result;
		}
		
		TimeCover time = p.getTimeCover();
		
		if (time.getDaysToOrder() < 0 || time.getDaysToDelivery() < 0 || time.getDaysToConference() < 0  || time.getDaysMarginSecurity() < 0 || time.getTotalDays() < 0) {
			
			result.setMessage("Formato inválido de nũmero. Somente números positivos!");
			return result; 
		}
		return result;
	}
	
	private boolean validateUnitMeasure(String unitM) {
		
		if (!unitM.equalsIgnoreCase(Product.CAIXA) && !unitM.equalsIgnoreCase(Product.KILOGRAMA) && !unitM.equalsIgnoreCase(Product.LITRO) && !unitM.equalsIgnoreCase(Product.METRO) && !unitM.equalsIgnoreCase(Product.SACO) && !unitM.equalsIgnoreCase(Product.UNIDADE)) {
			return false;
		}
		return true;
	}

	
	private boolean validateBarcode(String barcode) {
		
		try {
			@SuppressWarnings("unused")
			Long lBarcode = Long.parseLong(barcode);
		}catch (Exception e) {
			return false;
		}
		
		if (barcode.length() != 13) {
			return false;
			
		}
		return true;		
	}

}
