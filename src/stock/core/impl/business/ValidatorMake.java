package stock.core.impl.business;

import stock.core.application.Result;
import domain.DomainEntity;
import domain.Make;

public class ValidatorMake extends AbstractStrategyValidate {

	@Override
	public Result validate(DomainEntity domainEntity) {
		
		Result result = new Result();
		Make make = (Make) domainEntity;
		
		String name = make.getName();
		if (name == null || name == "") {
			
			result.setMessage("Marca do produto não preenchida.");
			return result;
		}
		
		return result;
	}

}
