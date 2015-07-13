package stock.core;



import stock.core.application.Result;
import domain.DomainEntity;

public interface IDAO {
	
	public Result save(DomainEntity domainEntity);
	public Result delete(DomainEntity domainEntity) ;
	public Result update(DomainEntity doaminEntity) ;
	public Result consult(DomainEntity entity, int offset, Integer recordsPerPage);
	public Result consult(DomainEntity domainEntity);
		
}
