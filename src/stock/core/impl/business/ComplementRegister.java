package stock.core.impl.business;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import domain.DomainEntity;
import stock.core.application.Result;

public class ComplementRegister extends AbstractStrategyValidate{

	
	public Result process(DomainEntity domainEntity) {
		
		return validate(domainEntity);
			
	}

	@Override
	public Result validate(DomainEntity domainEntity) {
		Result result = new Result();
		
		if (domainEntity == null) {
			return null;
		}
		
		Date date = new Date();		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		domainEntity.setDtCreated(cal);
		
		result.setEntityFiltered(domainEntity);
		return result;
	}
	
}
