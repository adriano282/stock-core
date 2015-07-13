package stock.core.impl.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import stock.core.application.Result;
import domain.Address;
import domain.Customer;
import domain.DomainEntity;

public class DAOAddress extends AbstractJdbcDAO{
	
	public DAOAddress() {
		super("address", "id");
	}
	
	public DAOAddress(Connection c) {
		super(c, "address","id");
	}

	@Override
	public Result save(DomainEntity domainEntity) {
		
		Result result = new Result();
		Address address = (Address) domainEntity;
		
		String query = "INSERT INTO address(`street`, `zipcode`, `number`, "
					+ "`quarter`, `city`, `state`, `date_created`)"
					+ "values('"+address.getStreet()+"', '"+address.getZipCode()
					+ "', '"+address.getNumber()+"', '"+address.getQuarter()+"', '"
					+ ""+address.getCity()+"', '"+address.getState()+"', now())";
		
		result.setMessage(executeQuery(query, "Endereço"));
		
		return result;
	}

	@Override
	public Result update(DomainEntity domainEntity) {
		
		Result result = new Result();
		Address address = (Address) domainEntity;
		
		String query = "update address set `street`='"+address.getStreet()+"', `zipcode`= '"+address.getZipCode()+"', `number` = '"+address.getNumber()+"', "
				+ " `quarter`='"+address.getQuarter()+"', `city`= '"+address.getCity()+"', `state`='"+address.getState()+"' where id = " + address.getId();
		
		result.setMessage(executeQuery(query, "Endereço"));
		
		return result;
	}

	@Override
	public Result consult(DomainEntity domainEntity) {
		return null;
	}
	
	
	public Address findByCustomer(Customer c) {
				
		Address ad = new Address();
		
		String query = "select * from address where id  = (select address_id from customer where id = " + c.getId() + ")";
		
		try {
			
			openConnection();
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			ResultSet result = statement.executeQuery(query);
			connection.commit();
			
			if (result != null) {
				while(result.next()) {
					ad.setCity(result.getString("city"));
					ad.setId(Integer.parseInt(result.getString("id")));
					ad.setNumber(result.getString("number"));
					ad.setQuarter(result.getString("quarter"));
					ad.setState(result.getString("state"));
					ad.setStreet(result.getString("street"));
					ad.setZipCode(result.getString("zipcode"));
				}
			}
					
		} catch (SQLException e) {
			
			rollBackTransaction();			
			e.printStackTrace();
			
		} finally{
			
			closeConnection();
			
		}
		
		return ad;
	}
		
	public Address findById(int id) {
				
		Address ad = new Address();
		String query = "select * from address where id = " + id;
		openConnection();
		
		try {
			
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			ResultSet result = statement.executeQuery(query);
			connection.commit();	
			
			if (result != null) {
				while(result.next()) {
					ad.setCity(result.getString("city"));
					ad.setId(Integer.parseInt(result.getString("id")));
					ad.setNumber(result.getString("number"));
					ad.setQuarter(result.getString("quarter"));
					ad.setState(result.getString("state"));
					ad.setStreet(result.getString("street"));
					ad.setZipCode(result.getString("zipcode"));					
				}
			}
					
		} catch (SQLException e) {
			
			e.printStackTrace();
			rollBackTransaction();			
			
		} finally {
			
			closeConnection();
		}
		
		return ad;
	}
		
	public int getLastAddressId() throws SQLException {
		
		Integer id = null;
		String query = "select max(id) as 'id' from address";
		openConnection();
		
		try {
			
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			ResultSet result = statement.executeQuery(query);
						
			if (result != null && result.next()) {
				id =  result.getInt("id");
			}
					
		} catch (SQLException e) {			
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		
		return id;
	}

	@Override
	public Result consult(DomainEntity entity, int i, Integer recordsPerPage) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
