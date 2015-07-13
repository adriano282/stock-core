package stock.core.impl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import domain.DomainEntity;
import domain.TimeCover;
import stock.core.application.Result;

public class DAOTimeCover extends AbstractJdbcDAO{
	

	public DAOTimeCover() {
		super("time_cover", "id");
	}

	@Override
	public Result save(DomainEntity domainEntity) {
		
		Result result = new Result();
		TimeCover tc = (TimeCover) domainEntity;
		
		String query = "insert into time_cover(`days_to_order`, `days_to_delivery`, `days_to_conference`, `days_margin_security`, `total_days`)"
				+ "values( " + tc.getDaysToOrder()+", " +tc.getDaysToDelivery()+", "+tc.getDaysToConference()+", " + tc.getDaysMarginSecurity()+ ", " +tc.getTotalDays()+ ")";
			
		result.setMessage(executeQuery(query, "Tempo de Cobertura"));
		
		return result;
	}

	@Override
	public Result update(DomainEntity domainEntity) {
		
		Result result = new Result();
		TimeCover tc = (TimeCover) domainEntity;
		
		String query = "update time_cover set `days_to_order` = " + tc.getDaysToOrder()
				+ ",`days_to_delivery` = " +tc.getDaysToDelivery()
				+ ", `days_to_conference` = " +tc.getDaysToConference() 
				+ ", `days_margin_security` = " + tc.getDaysMarginSecurity()
				+ ", `total_days` = "+tc.getTotalDays()
				+ " where id = " + tc.getId();
		
		result.setMessage(executeQuery(query, "Tempo de Cobertura"));
		
		return result;
	}

	@Override
	public Result consult(DomainEntity domainEntity) {
		
		TimeCover tc = (TimeCover) domainEntity;
		Result result = new Result();
		
		String whereClause = "";
		
		if (tc.getId() != null) {			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "id = " + tc.getId();
		}	
		
		String query = "select * from time_cover " + whereClause +";";
		
		result = executeSelect(query, TimeCover.class.getName());
		result.setEntityFiltered(tc);
		
		return result;
	
	}
	
	
	
	public TimeCover getLastTimeCover() {
		openConnection();
		try {			
			String query = "select * from time_cover where id = (select max(id) from time_cover)";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			if (result != null) {
				while (result.next()) {
					return dataBind.bindTimeCover(result);
				}
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return null;

	}
	public TimeCover getTimeCoverById(Integer id) {
		
		TimeCover tc = new TimeCover();
		Result result = new Result();		
		String whereClause = "";
		if (id != null) {
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "id = " + id;
		} 				
		String query = "select * from time_cover " + whereClause +";";
			
		try {
			openConnection();
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next()) {
				tc = dataBind.bindTimeCover(resultSet);
			}
			
		} catch (SQLException e) {
			result.setMessage("Ocorreu um erro durante a consulta de produtos");
			e.printStackTrace();
		}
		return tc;
	
	}

	@Override
	public Result consult(DomainEntity entity, int i, Integer recordsPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

}
