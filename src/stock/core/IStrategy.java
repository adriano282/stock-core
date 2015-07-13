package stock.core;

import domain.DomainEntity;
import stock.core.application.Result;

public interface IStrategy {
	
	public Result process(DomainEntity domain);
	
	

}
