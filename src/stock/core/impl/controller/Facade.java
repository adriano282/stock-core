package stock.core.impl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.Address;
import domain.Customer;
import domain.DomainEntity;
import domain.Make;
import domain.Order;
import domain.Product;
import domain.User;
import stock.core.IDAO;
import stock.core.IFacade;
import stock.core.IStrategy;
import stock.core.application.Result;
import stock.core.impl.business.AbstractStrategyProcess;
import stock.core.impl.business.AbstractStrategyValidate;
import stock.core.impl.business.ComplementRegister;
import stock.core.impl.business.StrategyOrder;
import stock.core.impl.business.StrategyStock;
import stock.core.impl.business.ValidatorAddress;
import stock.core.impl.business.ValidatorCustomer;
import stock.core.impl.business.ValidatorMake;
import stock.core.impl.business.ValidatorOrder;
import stock.core.impl.business.ValidatorProduct;
import stock.core.impl.business.ValidatorUser;
import stock.core.impl.dao.DAOAddress;
import stock.core.impl.dao.DAOCustomer;
import stock.core.impl.dao.DAOMake;
import stock.core.impl.dao.DAOOrder;
import stock.core.impl.dao.DAOProduct;
import stock.core.impl.dao.DAOUser;

public class Facade implements IFacade{

	private Map<String, IDAO> daos;
	private Map<String, List<IStrategy>> rules;
	
	
	public Facade(){
		
		daos = new HashMap<String, IDAO>();
		rules = new HashMap<String, List<IStrategy>>();		
		
		//rules of the all domain's entities		
		List<IStrategy> listMake = new ArrayList<IStrategy>();
		listMake.add(new ComplementRegister());
		listMake.add(new ValidatorMake());
		rules.put(Make.class.getName(), listMake);
		
		List<IStrategy> listProduct = new ArrayList<IStrategy>();
		listProduct.add(new ComplementRegister());
		listProduct.add(new ValidatorProduct());
		rules.put(Product.class.getName(), listProduct );
		
		List<IStrategy> listCustomer = new ArrayList<IStrategy>();
		listCustomer.add(new ComplementRegister());
		listCustomer.add(new ValidatorCustomer());
		rules.put(Customer.class.getName(), listCustomer );
		
		List<IStrategy> listAddress = new ArrayList<IStrategy>();
		listAddress.add(new ComplementRegister());
		listAddress.add(new ValidatorAddress());
		rules.put(Address.class.getName(), listAddress );
		
		
		List<IStrategy> listOrder = new ArrayList<IStrategy>();
		listOrder.add(new ComplementRegister());
		listOrder.add(new ValidatorOrder());
		listOrder.add(new StrategyOrder());
		rules.put(Order.class.getName(), listOrder );
		
		List<IStrategy> listUser = new ArrayList<IStrategy>();
		listUser.add(new ComplementRegister());
		listUser.add(new ValidatorUser());
		rules.put(User.class.getName(), listUser );
		
		// DAOS of the all domain's entities
		daos.put(Product.class.getName(), new DAOProduct());
		daos.put(User.class.getName(), new DAOUser());
		daos.put(Make.class.getName(), new DAOMake());
		daos.put(Customer.class.getName(), new DAOCustomer());
		daos.put(Address.class.getName(), new DAOAddress());
		daos.put(Order.class.getName(), new DAOOrder());
					
	}
	
	private String execute(DomainEntity entity) {
		
		Result result = new Result();
		result.setEntityFiltered(entity);
		
		String nmClasse = entity.getClass().getName();		
		StringBuilder message = new StringBuilder();		

		List<IStrategy> strategies = rules.get(nmClasse);
		
		for ( IStrategy s : strategies) {
			
			if (s instanceof AbstractStrategyProcess) {				
				message.append(((AbstractStrategyProcess) s).execute(entity).getMessage());
			}
		}
		
		if (message.toString().equals("null")) {
			return null;
		}
		
		return message.toString();
	}

	private String validate(DomainEntity entity) {
		
		Result result = new Result();
		result.setEntityFiltered(entity);
		
		String nmClasse = entity.getClass().getName();		
		StringBuilder message = new StringBuilder();		

		List<IStrategy> strategies = rules.get(nmClasse);
		
		for ( IStrategy s : strategies) {
			
			String messageResult = null;
			if (s instanceof AbstractStrategyValidate) {
				
				messageResult = ((AbstractStrategyValidate) s).validate(entity).getMessage();
				
				if (messageResult != null)
					message.append(messageResult);
			}
		}
		return message.toString();
	}
	
	public Result save(DomainEntity entity) {
		
		Result result = new Result();
		String nmClasse = entity.getClass().getName();
		
		String msg = validate(entity);
						
		if (msg == null || msg.equals("")) {
			
			IDAO dao = daos.get(nmClasse);
			result = dao.save(entity);
			
			
			result.setMessage(execute(entity));
			
		} else {
			
			List<DomainEntity> entities = new ArrayList<DomainEntity>();			
			if ( entity instanceof Order == false) {
				
				entities.add(entity);
				result.setEntities(entities);
				
			}			
			result.setTypeMessage(Result.DANGER);
			result.setMessage(msg);
		}
		
		if (result.getMessage() != null && result.getMessage().equals("")) {
			result.setMessage(null);
		}
		return result;
	}
	
	@Override
	public Result update(DomainEntity entity) {
			
		Result result = new Result();
		String nmClasse = entity.getClass().getName();
		
		String msg = validate(entity);
						
		if (msg == null || msg.equals("")) {
			
			IDAO dao = daos.get(nmClasse);
			result = dao.update(entity);
			
			if (result.getMessage() != null && !result.getMessage().equals("")) {
				return result;
			}
			
			result.setMessage(execute(entity));
			
		} else {
			
			List<DomainEntity> entities = new ArrayList<DomainEntity>();
			entities.add(entity);
			result = new Result();
			result.setEntities(entities);
			result.setMessage(msg);
		}
		
		if (result.getMessage() != null && result.getMessage().equals("")) {
			result.setMessage(null);
		}
		return result;
		
	}

	@Override
	public Result delete(DomainEntity entity) {
		
		String nameClass = entity.getClass().getName();
		IDAO dao = daos.get(nameClass);
		Result result =  new Result();
	
		result = dao.delete(entity);
		
		if (result == null) {
			
			result = new Result();
			List<DomainEntity> entities = new ArrayList<DomainEntity>();
			entities.add(entity);
			result.setEntities(entities);
		}
		
		return result;
	}

	public Result consult(DomainEntity entity, Integer page, Integer recordsPerPage) {
		
		String nameClass = entity.getClass().getName();
		
		IDAO dao = daos.get(nameClass);
		Result result =  new Result();
		
		result = dao.consult(entity, recordsPerPage == null? 0:(page -1)* recordsPerPage, recordsPerPage);

		
		return result;
	}
	

	@Override
	public Result process(DomainEntity entity) {
		
		StrategyStock strategyStock = new StrategyStock();
		Result result =  new Result();
		
		if (entity instanceof Product) {
			strategyStock.process(entity);
			
			DAOProduct dao = new DAOProduct();
			dao.update(entity);

		}
		
		result = new Result();
		return result;
	}
}
