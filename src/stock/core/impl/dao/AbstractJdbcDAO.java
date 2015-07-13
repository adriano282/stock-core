package stock.core.impl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import domain.DomainEntity;
import stock.core.IDAO;
import stock.core.application.Result;
import stock.core.useful.ConnectionDataBase;
import stock.core.useful.DataBind;

public abstract class AbstractJdbcDAO implements IDAO{
	
	protected Connection connection;
	protected String table;
	protected String idTable;
	protected boolean ctrlTransaction=true;
	protected DataBind dataBind = DataBind.getInstance();
	
	public AbstractJdbcDAO( Connection connection, String table, String idTable) {
		
		this.table = table;
		this.idTable = idTable;
		this.connection = connection;
		
	}
	
	protected AbstractJdbcDAO(String table, String idTable){
		
		this.table = table;
		this.idTable = idTable;
		
	}
	
	@Override
	public Result delete(DomainEntity entityDomain) {
		
		Result result = new Result();
		openConnection();
		PreparedStatement pst=null;		
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(table);
		sb.append(" WHERE ");
		sb.append(idTable);
		sb.append("=");
		sb.append("?");
		
		try {
			
			connection.setAutoCommit(false);
			pst = connection.prepareStatement(sb.toString());
			pst.setInt(1, entityDomain.getId());
			pst.executeUpdate();
			connection.commit();
			
		} catch (SQLException e) {
			
			rollBackTransaction();
			
			result.setMessage("Nao é possivel excluir o objeto " + entityDomain.getClass().getName() + " pois é usado internamente pelo sistema!");
			e.printStackTrace();
			
		} finally {
			
			try {
				
				pst.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				
			}
			
			closeConnection();
		}
		
		return result;
	}		
	
	protected void openConnection(){
		
		try {
			
			if(connection == null || connection.isClosed())
				connection = ConnectionDataBase.getConnection();
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
	}
	
	protected boolean rollBackTransaction() {
		
		try {
			
			connection.rollback();
			
		} catch (SQLException e1) {
			
			e1.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	
	protected boolean closeConnection() {
		
		try {
		
			connection.close();
			
		} catch (SQLException e) {

			e.printStackTrace();
			return false;
			
		}
		
		return true;
	}

	public Integer executeCount(String className, String whereClause) {		
		return count(className, whereClause);
	}
	
	public Integer executeCount(String className) {		
		return count(className, null);
	}
	
	protected Result executeSelect(String query, String className, String whereClause) {
		
		return select(query, className, whereClause);
	}
		
	protected Result executeSelect(String query, String className) {
		
		return select(query, className, null);
	}
	
	protected String executeQuery(String query, String entityName){
		return saveOrUpdate(query, entityName);
	}

	protected String executeQuery(String query) throws SQLException {
		return saveOrUpdate(query, null);
	}
	
	private String saveOrUpdate(String query, String entityName) {
		
		String message = null;
		
		try {		
			
			openConnection();
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			statement.executeUpdate(query);
			connection.commit();
			
		} catch (SQLException e) {
			
			rollBackTransaction();
			e.printStackTrace();
			message = "Ocorreu um erro ao tentar salvar a entidade" + entityName == null || entityName.equals("")? ".":entityName;
			
		} finally {
			
			closeConnection();
			
		}
		
		return message;
		
	}
	
	private Result select(String query, String className, String whereClause) {
		
		Result result = new Result();
		List<DomainEntity> entities = new ArrayList<DomainEntity>();
		openConnection();
		
		try{
			
			Statement statement = connection.createStatement();
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
						
			if (result != null) {
				
				while(resultSet.next()) {
					
					entities.add(dataBind.bind(className, resultSet));
					
				}
			}
						
		} catch (SQLException e) {
			
			e.printStackTrace();
			result.setMessage("Ocorreu um erro ao tentar realizar a consulta");
			
		} finally {
			
			closeConnection();
			
			if (whereClause != null) {
				
				result.setCountEntities(executeCount(className, whereClause));
				
			} else {
				
				result.setCountEntities(executeCount(className));
			}
			
			result.setEntities(entities);
			
		}
		return result;
	}
	
	private Integer count(String className, String whereClause) {
		
		openConnection();
		Statement statement;
		
		try {
			statement = connection.createStatement();
					
			String queryCount = "select count(*) as 'Total' from ";
			
			switch (className) {
			
				case "domain.Customer":
					queryCount += "customer";
					break;
					
				case "domain.User":
					queryCount += "user";
					break;
					
				case "domain.Product":
					queryCount += "product p "
							+ "inner join make m on m.id = p.make_id ";
					break;
					
				case "domain.Order":
					queryCount += "`order` o "
							+ "inner join customer c on c.id = o.customer_id "
							+ "inner join product p on p.id = o.product_id ";
					break;
					
				case "domain.TimeCover":
					queryCount += "time_cover";
					break;
					
			}
			
			if (whereClause != null) 
				queryCount += " " + whereClause;
			
			ResultSet resultSet = statement.executeQuery(queryCount);
			while(resultSet.next()) {
				
				return(resultSet.getInt("Total"));
			}
		
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
		}
		return null;
	}
	
}
