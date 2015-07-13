package stock.core.impl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import stock.core.application.Result;

import com.mysql.jdbc.Connection;

import domain.DomainEntity;
import domain.Make;

public class DAOMake extends AbstractJdbcDAO {

	public DAOMake() {
		super("make","id");
	}
	public DAOMake(Connection c) {
		super(c, "make", "id");
	}
	
	public DAOMake(String table, String idTable) {
		super( table, idTable );
	}
	@Override
	public Result save(DomainEntity domainEntity) {
		
		Result result = new Result();
		Make make = (Make) domainEntity;
		openConnection();
		
		String query = "INSERT INTO make(`name`, `date_created`) values( '" + make.getName() + "', now())";  
		result.setMessage(executeQuery(query, "Marca"));
		
		return result;
	}

	@Override
	public Result update(DomainEntity doaminEntity)  {
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public Result consult(DomainEntity domainEntity)  {
		return null;
		
	}
	
	public Make findMakeByName(String name) {
		
		openConnection();
		
		try {
			
			Statement statement = connection.createStatement();		
			Make make = new Make();
			String query = "select * from make where name = '" + name + "'";
			
			
			ResultSet resultSet = statement.executeQuery(query);
			
			if(resultSet != null && resultSet.next()){
				
				make.setId(Integer.parseInt(resultSet.getString("id")));
				make.setName(resultSet.getString("name"));				
				make.setDtCreated(dataBind.formatDate(resultSet.getString("date_created")));
				return make;
				
            } else {
            	
            	return null;
            	
            }
								
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				
				connection.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}			
		}
		return null;
	}

public Make getMakeById(int id)  {
		
		openConnection();
		try {
			Statement statement = connection.createStatement();
		
			Make make = new Make();
			String query = "select * from make where id = " + id;  
			ResultSet resultSet = statement.executeQuery(query);
			
			if(resultSet != null && resultSet.next()){  
				make.setId(Integer.parseInt(resultSet.getString("id")));
				make.setName(resultSet.getString("name"));				
				make.setDtCreated(dataBind.formatDate(resultSet.getString("date_created")));
				return make;
            } else {
            	return null;
            }
			
								
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			closeConnection();			
		}
		
		return null;
	}
@Override
public Result consult(DomainEntity entity, int i, Integer recordsPerPage)
		 {
	// TODO Auto-generated method stub
	return null;
}

}
