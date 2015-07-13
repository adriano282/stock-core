package stock.core;

import stock.core.application.Result;
import domain.DomainEntity;

public interface IFacade {

	public Result save(DomainEntity entity);
	public Result update(DomainEntity entity);
	public Result delete(DomainEntity entity);
	public Result consult(DomainEntity entity, Integer page, Integer recordsPerPage);
	public Result process(DomainEntity entity);

}
