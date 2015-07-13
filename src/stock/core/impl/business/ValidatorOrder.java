package stock.core.impl.business;

import java.math.BigDecimal;

import stock.core.application.Result;
import domain.DomainEntity;
import domain.Order;

public class ValidatorOrder extends AbstractStrategyValidate {

	@Override
	public Result validate(DomainEntity domainEntity) {
		
		Result result = new Result();
		Order order = (Order) domainEntity;
		
		result.setMessage(validateNullFields(order));
		String message = result.getMessage();
		
		if (message != null)
			return result;
		
		if (order.getCustomer().getId() == null) {
			
			result.setMessage("Cliente nao encontrado! Selecione um cliente para cadastrar uma venda!");
			return result;
		}
		
		BigDecimal qtdeStock = order.getProduct().getQuantity();
		BigDecimal qtdeVenda = new BigDecimal(order.getQuantity());
		
		if (qtdeStock.compareTo(qtdeVenda) < 0) {
			
			result.setMessage("O produto selecionado nao possui quantidade em estoque suficiente para este pedido."
					+ "<br>Estoque atual: " + order.getProduct().getQuantity());
			return result;
		}
		
		return result;

	}
}
