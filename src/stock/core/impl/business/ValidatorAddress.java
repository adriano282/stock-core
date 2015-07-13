package stock.core.impl.business;

import stock.core.application.Result;
import domain.Address;
import domain.DomainEntity;

public class ValidatorAddress extends AbstractStrategyValidate {

	@Override
	public Result validate(DomainEntity domainEntity) {
		
		Result result = new Result();
		Address address = (Address) domainEntity;
		result.setMessage(validateNullFields(address));
		
		return result;
	}

}
