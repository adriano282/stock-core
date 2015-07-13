package stock.core.impl.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import stock.core.application.Result;
import domain.Address;
import domain.Customer;
import domain.DomainEntity;

public class DAOCustomer extends AbstractJdbcDAO{
	
	public DAOCustomer() {
		super("customer", "id");
	}
	
	public DAOCustomer(Connection c) {
		super(c, "customer", "id");
	}

	@Override
	public Result save(DomainEntity domainEntity)  {
		
		Result result = new Result();
		Customer customer = (Customer) domainEntity;
		Address ad = customer.getAddress();
		DAOAddress daoAddress = new DAOAddress();
		
		try {
			
			result = daoAddress.save(ad);
			
		} catch (Exception e1) {
			
			e1.printStackTrace();
			result.setMessage("Ocorreu um erro ao tentar salvar o cliente");
			return result;
			
		}
		
		try {
			
			customer.setAddress(ad);
			String query = "INSERT INTO customer(`name`, `document`, `phone`, `email`,`type`, `address_id`, `date_created`) "
					+ "values('"+customer.getName()+"', '"+customer.getDocument()+"', '"+customer.getPhone()+"', '"+customer.getEmail()+"','" 
					+ customer.getType()+"', "+daoAddress.getLastAddressId()+", now())";
		
			executeQuery(query);
			
		} catch (Exception e) {
			
			result.setMessage("Ocorreu um erro ao tentar salvar o cliente");
			
		}
		
		return result;
		
	}

	@Override
	public Result update(DomainEntity domainEntity)  {
		
		Result result = new Result();
		List<DomainEntity> entities = new ArrayList<DomainEntity>();
		Customer customer = (Customer) domainEntity;
		Address ad = customer.getAddress();
		DAOAddress daoAddress = new DAOAddress();
		
		try {
			
			ad.setId(daoAddress.findByCustomer(customer).getId());
			result = daoAddress.update(ad);
			
		} catch (Exception e1) {
			
			e1.printStackTrace();
			result.setMessage("Ocorreu um erro ao tentar salvar o cliente");
			return result;
		}
		
		try {
			
			String query ="update customer set `name`= '" + customer.getName() + "',"
			+ " `document`= '" + customer.getDocument() + "', "
			+ " `phone`= '" + customer.getPhone() + "', "
			+ " `email`= '" + customer.getEmail() + "', "
			+ "`type`= '" + customer.getType() + "' "
			+ "where id = " + customer.getId();
			executeQuery(query);
			
		} catch (Exception e) {
			
			result.setMessage("Ocorreu um erro ao tentar salvar o cliente");
			return result;
			
		} finally {
			
			closeConnection();
			
		}
		
		entities.add(customer);
		result.setEntities(entities);
		return result;
	}

	@SuppressWarnings("finally")
	public List<Customer> findAllByFieldAndValue(String nameField, String value) {
		
		
		Field[] fields = Customer.class.getDeclaredFields();
		List<Customer> customers = new ArrayList<Customer>();	
		for(Field f : fields ) {
			if (f.getName().equalsIgnoreCase(nameField)){
				try {
					openConnection();
					Statement statement = connection.createStatement();
					connection.setAutoCommit(false);
					String query;
					if (f.getName().equals("id")) {
						query = "select * from customer where " + f.getName() + "=" + value;
					} else {
						query = "select * from customer where " + f.getName() + "='" + value+"'";
					}
					ResultSet result;
					result = statement.executeQuery(query);
					connection.commit();			
					if (result != null) {
						while(result.next()) {
							customers.add(dataBind.bindCustomer(result));
						}//While
					}//If result
				} catch (SQLException e1) {
					
					e1.printStackTrace();
					
				}finally{

					closeConnection();
					return customers;
				}
			}// if field
		}
		return null;					
	}
	
	
	@SuppressWarnings("finally")
	public List<Customer> findAllByFieldAndLikeValue(String nameField, String value) {
			
			Customer c = new Customer();
			Field[] fields = c.getClass().getDeclaredFields();
			List<Customer> customers = new ArrayList<Customer>();
			for(Field f : fields ) {
				if (f.getName().equalsIgnoreCase(nameField)){
					try {
						openConnection();
						Statement statement = connection.createStatement();
						connection.setAutoCommit(false);
						String query = "select * from customer where " + f.getName() + " like '%" + value + "%'";
						ResultSet result;
						result = statement.executeQuery(query);
						connection.commit();			
						if (result != null) {
							while(result.next()) {
								customers.add(dataBind.bindCustomer(result));
							}//While
						}//If result
					}catch (SQLException e1) {
						
						e1.printStackTrace();
						
					}finally{

						closeConnection();
						return customers;
					}
				}// if field
			}
			return null;					
		}

	public Customer findById(int id) {
		try {
			openConnection();
			Statement statement = connection.createStatement();				
			connection.setAutoCommit(false);
			String query;
			query = "select * from customer where id = " + id;
			ResultSet result;
			result = statement.executeQuery(query);
			connection.commit();			
			if (result != null && result.next()) {
				return dataBind.bindCustomer(result);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally{
			closeConnection();
		}
		return null;
	}
	
	@SuppressWarnings("finally")
	public Customer findByFieldAndValue(String nameField, String value) {
		Customer c = null;
		Field[] fields = Customer.class.getDeclaredFields();
		for(Field f : fields ) {
			if (f.getName().equalsIgnoreCase(nameField)){
				try {
					openConnection();
					Statement statement = connection.createStatement();				
					connection.setAutoCommit(false);
					String query;
					if (f.getName().equals("id")) {
						query = "select * from customer where " + f.getName() + "=" + value;
					} else {
						query = "select * from customer where " + f.getName() + "='" + value+"'";
					}
					ResultSet result;
					result = statement.executeQuery(query);
					connection.commit();			
					if (result != null && result.next()) {
						c = dataBind.bindCustomer(result);
					}//If result
				} catch (SQLException e1) {
					e1.printStackTrace();
				}finally{
					closeConnection();
					return c;
				}	
			}// if field
		}
		return null;					
		}
	
	@Override
	public Result consult(DomainEntity entity, int offset, Integer recordsPerPage) {
		
		Customer c = (Customer) entity;
		Result result = new Result();
		String whereClause = "";
		
		if (c.getId() != null) {
			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "id = " + c.getId();
			
		} else if (c.getDocument() != null && !c.getDocument().equals("")) {
			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "document = '" + c.getDocument() + "' ";
			
		} else {
			
			if (c.getName() != null && !c.getName().equals("")) {
				
				whereClause += whereClause.equals("")? "where ": " and ";
				whereClause += "name like '%" + c.getName()+ "%'";
			}
			
			if (c.getPhone() != null && !c.getPhone().equals("")) {
				
				whereClause += whereClause.equals("")? "where ": " and "; 			
				whereClause += "phone = " + c.getPhone();
			}
			
			if (c.getEmail() != null && !c.getEmail().equals("")) {
				
				whereClause += whereClause.equals("")? "where ": " and ";
				whereClause += "email like '%" + c.getEmail()+ "%'";
			}
			
			if (c.getType() != null && !c.getType().equals("")) {
				
				whereClause += whereClause.equals("")? "where ": " and "; 			
				whereClause += "type = " + c.getType();
			}
			
			if (c.getDtCreated() != null && !c.getDtCreated().equals("")) {
				
				whereClause += whereClause.equals("")? "where ": " and "; 			
				whereClause += "date_created = " + c.getDtCreated();
			}
		}
		
		String query = "";
		
		if (recordsPerPage == null) {
			
			query = "select * from customer " + whereClause + " order by name asc;";
			
		} else {
			
			query = "select * from customer " + whereClause + " order by name asc limit " + offset + "," + recordsPerPage +";";
		}
		
				
		result = (executeSelect(query, Customer.class.getName(), whereClause));
		result.setEntityFiltered(c);
		return result;
	}

	@Override
	public Result consult(DomainEntity domainEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
