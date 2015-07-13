package stock.core.impl.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import stock.core.application.Result;
import domain.DomainEntity;
import domain.User;

public class DAOUser extends AbstractJdbcDAO{

	public DAOUser() {
		super("user", "id");
	}
	
	@Override
	public Result save(DomainEntity domainEntity) {
		
		Result result = new Result();
		User user = (User) domainEntity;
		
		Timestamp time = new Timestamp(System.currentTimeMillis());
		
		String root = user.getRoot()?"1":"0";
		String ableEmail = user.getAbleEmail()?"1":"0";
		
		String query = "INSERT INTO user(`username`, `password`, `root`, `able_email`, `date_created`) "
				+ "values('"+user.getUsername()+"', '"+user.getHashCodePassword()+"', "+ root + ", " + ableEmail + ",'" +time+"')";
		
		executeQuery(query, "Usuário");
		
		return result;
		
	}

	@Override
	public Result update(DomainEntity domainEntity) {
		
		Result result = new Result();
		List<DomainEntity> entities = new ArrayList<DomainEntity>();
		User user = (User) domainEntity;
		
		
		String root = user.getRoot()?"1":"0";
		String ableEmail = user.getAbleEmail()?"1":"0";
		
		String query ="update user set `username`= '" +user.getUsername()+ "',"
		+ " `password`= '" +user.getHashCodePassword()+ "', "
		+ " `root`= " + root + ", "
		+ " `able_email` = " + ableEmail
		+ " where id = " + user.getId();		
		
		result.setMessage(executeQuery(query,"Usuário"));
		entities.add(user);
		result.setEntities(entities);
		
		return result;
	}

	@Override
	public Result consult(DomainEntity domainEntity) {

		User user = (User) domainEntity;
		Result result = new Result();
		String whereClause = "";
		
		if (user.getId() != null) {
			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "id = " + user.getId();
			
		} else if (user.getUsername() != null && !user.getUsername().equals("")) {
			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "username = '" + user.getUsername() + "'";
			
		} else if(user.getRoot()) {
			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "root = 1 ";
			
		}
		
		String query = "select * from `user` " + whereClause + " order by username asc;";
		
		result = executeSelect(query, User.class.getName());
		result.setEntityFiltered(user);
		
		return result;
	}

	@Override
	public Result consult(DomainEntity domainEntity, int offset, Integer recordsPerPage) {
		
		User user = (User) domainEntity;
		Result result = new Result();
		String whereClause = "";
		
		if (user.getId() != null) {
			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "id = " + user.getId();
			
		} else if (user.getUsername() != null && !user.getUsername().equals("")) {
			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "username like '%" + user.getUsername() + "%'";
		}
		
		if (user.getDtCreated() != null) {
			
			whereClause += whereClause.equals("")? "where ": " and ";
			whereClause += "date(date_created) >= '" + user.getDtCreated().get(Calendar.YEAR) + "-"
					+ user.getDtCreated().get(Calendar.MONTH) + "-" 
					+user.getDtCreated().get(Calendar.DAY_OF_MONTH) + "' ";
					
		}
		
		String query = "select * from `user` " + whereClause +" order by username asc" + " limit " + offset + "," + recordsPerPage;
		
		result = executeSelect(query, User.class.getName());
		
		result.setEntityFiltered(user);
		
		return result;
	}
	
	public User getUserByUsername(String username) {
		
		if (username != null && !username.equals("")) {
			
			User user = new User();
			user.setUsername(username);
			
			Result result =  consult(user);
			
			if (result == null)
				return null;
			
			if (result.getEntities().size() == 0) {
				return null;
			}
			
			return (User) result.getEntities().get(0);
						
		} else {
			
			return null;
			
		}
	}
	
	public User getUserRoot() {
		
		User user = new User();
		user.setRoot(true);
		
		Result result = consult(user);
		
		if (result == null)
			return null;
		
		if (result.getEntities().size() == 0)
			return null;			
		
		return (User) result.getEntities().get(0);
	}

}
