package stock.core.impl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import domain.DomainEntity;
import stock.core.application.Result;


public class DAOProcessStock extends AbstractJdbcDAO {

	public DAOProcessStock() {
		super("product", "id");
	}

	@Override
	public Result save(DomainEntity domainEntity)  {
		return null;
	}
		
	public Double avgQuatityByMonth(Integer id) {
			
		Map<Integer, Double> resultCount = countSaleQuantityByMonth(id);
		
		if (resultCount != null) {
			
			Iterator<Entry<Integer, Double>> iterator = resultCount.entrySet().iterator();
			Double somaQuantity  = 0.00;
			int i = 0;
			
			while (iterator.hasNext()) {
				
				Entry<Integer, Double> pair = (Map.Entry<Integer, Double>) iterator.next();
				somaQuantity += pair.getValue();
				iterator.remove();
				i++;
			}		
			
			return (somaQuantity /  i);
			
		} else {
			
			return null;
		}
		
	}
	
	public Map<Integer, Double> countSaleQuantityByMonth(Integer id) {
		
		Map<Integer, Double> countQuantity = new HashMap<Integer, Double>();
		
		String query = 	  " SELECT month(o.date_created) as 'month', year(o.date_created) AS 'year', SUM(o.quantity) as 'quantity'"
						+ " FROM `order` o"
						+ " inner join product p "
						+ " on p.id = o.product_id "
						+ " where p.id = " + id
						+ " and year(o.date_created) = year(current_date())"
						+ " and month(o.date_created) < month(current_date())"
						+ " group by concat(month(o.date_created), ' - ', year(o.date_created)) "
						+ " order by concat(month(o.date_created), ' - ', year(o.date_created)) asc;";
		
		openConnection();
		
		try {
			
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			while (result.next()) {
				
				countQuantity.put(result.getInt("month"),result.getDouble("quantity"));
				
			}
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
		
		return countQuantity;
		
	}

	@Override
	public Result update(DomainEntity doaminEntity)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result consult(DomainEntity domainnEntity)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Result consult(DomainEntity entity, int i, Integer recordsPerPage)
			 {
		// TODO Auto-generated method stub
		return null;
	}

}
