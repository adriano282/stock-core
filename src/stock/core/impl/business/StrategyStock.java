package stock.core.impl.business;

import domain.DomainEntity;
import domain.Product;
import stock.core.application.Result;
import stock.core.service.StockService;

public class StrategyStock extends AbstractStrategyProcess {

	@Override
	public Result execute(DomainEntity domainEntity) {
		
		if (domainEntity.getId() == null) {			
			return null;
		}
		
		Product p = (Product) domainEntity;
		StockService stockService = StockService.getInstance();

		Result result = new Result();
		
		result.setEntityFiltered(stockService.processStock(p));
		
		return result;
	}
	
	
}
