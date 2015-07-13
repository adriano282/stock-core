package stock.core.impl.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import domain.DomainEntity;
import domain.Order;
import domain.Product;
import stock.core.application.Result;

public class DAOOrder extends AbstractJdbcDAO{
	
	public DAOOrder() {
		super("`order`", "id");
	}
	public DAOOrder(Connection c) {
		super(c, "`order`", "id");
	}
		
	@Override
	public Result save(DomainEntity domainEntity)  {

		Result result = new Result();
		Order o = (Order) domainEntity;
		DAOProduct daoProduct = new DAOProduct();
		Product p = new Product();		
		p = o.getProduct();
		
		BigDecimal dbQuantidade = new BigDecimal(o.getQuantity());
		BigDecimal quantity = p.getQuantity().subtract(dbQuantidade);
		
		p.setQuantity(quantity);
		daoProduct.update(p);		
		
		String query = "insert into `order`(`quantity`, `subtotal`, `product_id`, `customer_id`, `date_created`) "
				+ "values( "+ o.getQuantity()+", " + o.getSubTotal()+", " +o.getProduct().getId()+", "+o.getCustomer().getId()+", now())";
		
		result.setMessage(executeQuery(query, "Venda"));			
		return result;
	}
	
	@Override
	public Result update(DomainEntity doaminEntity)  {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Result consult(DomainEntity domainEntity, int offset, Integer recordsPerPage) {
		
		Order o = (Order) domainEntity;
		String whereClause = "";
		
		if (o.getId() != null) {
			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "o.id = " + o.getId();
			
		} else {			
			
			if (o.getDtCreated() != null) {
				
				whereClause += whereClause.equals("")? "where ": " and ";
				whereClause += "date(c.date_created) >= '" + o.getDtCreated().get(Calendar.YEAR) + "-"
						+ o.getDtCreated().get(Calendar.MONTH) + "-" 
						+ o.getDtCreated().get(Calendar.DAY_OF_MONTH) + "' ";
						
			}
			
			if (o.getCustomer() != null) {	
				
				if (o.getCustomer().getDocument() != null && !o.getCustomer().getDocument().equals("")) {
					
					whereClause += whereClause.equals("")? "where ": " and ";
					whereClause += "c.document = '" + o.getCustomer().getDocument() + "' "; 
					
				} else {
					
					if (o.getCustomer().getName() != null && !o.getCustomer().getName().equals("")) {
						
						whereClause += whereClause.equals("")? "where ": " and ";
						whereClause += "c.name like '%" + o.getCustomer().getName() + "%'";
					}
				}			
			}
			
			if (o.getProduct() != null) {
				
				if (o.getProduct().getId() != null) {
					
					whereClause += whereClause.equals("")? "where ": " and ";
					whereClause += "o.product_id = " + o.getProduct().getId();
					
				} else {
					
					if (o.getProduct().getCode() != null && !o.getProduct().getCode().equals("")) {
						
						whereClause += whereClause.equals("")? "where ": " and ";
						whereClause += "p.code like '%" + o.getProduct().getCode() + "%'";
						
					}
					
					if (o.getProduct().getName() != null && !o.getProduct().getName().equals("")) {
						
						whereClause += whereClause.equals("")? "where ": " and ";
						whereClause += "p.name like '%" + o.getProduct().getName() + "%'";
					}
				}
			}
		}	
		
		String query = "select o.id as id, o.quantity as quantity, o.subtotal as subtotal, "
				+ "o.customer_id as customer_id, o.product_id as product_id" 
				+ ", o.date_created as date_created from `order` o "
				+ "inner join customer c on c.id = o.customer_id " 
				+ "inner join product p on p.id = o.product_id " + whereClause + " order by o.id desc "
				+ "limit " + offset + "," + recordsPerPage+";";
		
		Result result = executeSelect(query, Order.class.getName(), whereClause);
		result.setEntityFiltered(o);
		return result;
	}
	
	@Override
	public Result consult(DomainEntity domainEntity) {
		
		Order o = (Order) domainEntity;
		Result result = new Result();
		List<DomainEntity> orders = new ArrayList<DomainEntity>();
		String whereClause = "";
		
		if (o.getId() != null) {
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "o.id = " + o.getId();
		} else {			
			
			if (o.getCustomer() != null) {				
				if (o.getCustomer().getDocument() != null && !o.getCustomer().getDocument().equals("")) {
					whereClause += whereClause.equals("")? "where ": " and ";
					whereClause += "c.document = " + o.getCustomer().getDocument();
				} else {
					if (o.getCustomer().getName() != null && !o.getCustomer().getName().equals("")) {
						whereClause += whereClause.equals("")? "where ": " and ";
						whereClause += "c.name like '%" + o.getCustomer().getName() + "%'";
					}
				}			
			}
			
			if (o.getProduct() != null) {
				if (o.getProduct().getId() != null) {
					whereClause += whereClause.equals("")? "where ": " and ";
					whereClause += "o.product_id = " + o.getProduct().getId();
				} else {
					if (o.getProduct().getCode() != null && !o.getProduct().getCode().equals("")) {
						whereClause += whereClause.equals("")? "where ": " and ";
						whereClause += "p.code like '%" + o.getProduct().getCode() + "%'";
					}
					if (o.getProduct().getName() != null && !o.getProduct().getName().equals("")) {
						whereClause += whereClause.equals("")? "where ": " and ";
						whereClause += "p.name like '%" + o.getProduct().getName() + "%'";
					}
				}
			}
		}	
		
		String query = "select o.id as id, o.quantity as quantity, o.subtotal as subtotal, "
				+ "o.customer_id as customer_id, o.product_id as product_id" 
				+ ", o.date_created as date_created from `order` o "
				+ "inner join customer c on c.id = o.customer_id " 
				+ "inner join product p on p.id = o.product_id " + whereClause + ";";
		
		openConnection();
		
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
		
			while (resultSet.next()) {
		
				orders.add(dataBind.bindOrder(resultSet));
			}
		} catch (ParseException p)  {
			p.printStackTrace();
			result.setMessage("Ocorreu um erro na consulta de pedidos");
			return result;						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		result.setEntities(orders);
		result.setEntityFiltered(o);
		return result;
	}
	
	public int countSalesByProduct(Product product) {
		if (product.getId() != null) {
			String query = "select p.id as 'product_id', "
					+ " p.name as 'Nome Produto' ,"
					+ " count(o.id) as 'Total Vendas',"
					+ " max(o.date_created) as 'Ultima Compra'"
					+ "  from `order` o "
					+ " inner join product p "
					+ " on p.id = o.product_id "
					+ " where (day(current_date()) - day(o.date_created) ) < 31 and p.id = " + product.getId();
			
			openConnection();
			try {
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
				
				if (resultSet.next()) {
					return resultSet.getInt("Total Vendas");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;

	}
	public int countSoldItensByProduct(Product product) {
		
		if (product.getId() != null) {
			String query = "select p.id as 'product_id', "
					+ " p.name as 'Nome Produto' ,"
					+ " sum(o.quantity) as 'Total Itens',"
					+ " max(o.date_created) as 'Ultima Compra'"
					+ "  from `order` o "
					+ " inner join product p "
					+ " on p.id = o.product_id "
					+ " where (day(current_date()) - day(o.date_created) ) < 31 and p.id = " + product.getId() 
					+ " group by product_id;";
			
			openConnection();
			try {
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
				
				if (resultSet.next()) {
					return resultSet.getInt("Total Itens");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

}
