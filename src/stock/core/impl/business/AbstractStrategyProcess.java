package stock.core.impl.business;

import domain.DomainEntity;
import stock.core.IStrategy;
import stock.core.application.Result;

public abstract class AbstractStrategyProcess  implements IStrategy {

	
	public Result process(DomainEntity domain) {
		return execute(domain);
	}
	
	public abstract Result execute(DomainEntity domain);
	
	
	
}
