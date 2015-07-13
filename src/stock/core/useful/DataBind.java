package stock.core.useful;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import stock.core.impl.dao.DAOAddress;
import stock.core.impl.dao.DAOCustomer;
import stock.core.impl.dao.DAOMake;
import stock.core.impl.dao.DAOProduct;
import stock.core.impl.dao.DAOTimeCover;
import domain.Customer;
import domain.DomainEntity;
import domain.Order;
import domain.Product;
import domain.TimeCover;
import domain.User;

public class DataBind {
	
	private static DataBind instance = new DataBind();
	
	public static DataBind getInstance() {
		return instance;
	}
	
	private DataBind() {
	}
	
	
	public DomainEntity bind(String className, ResultSet resultSet) {
		
		try {
			
			switch (className) {
			
				case "domain.Customer":
					return bindCustomer(resultSet);
					
				case "domain.User":
					return bindUser(resultSet);
									
				case "domain.Product":
					return bindProduct(resultSet);
								
				case "domain.Order":
					return bindOrder(resultSet);
								
				case "domain.TimeCover":
					return bindTimeCover(resultSet);
												
			}
			
			
		} catch (SQLException e) {

			e.printStackTrace();
			
		} catch (ParseException e) {

			e.printStackTrace();
		}
		
		return null;
	
	}
	
	public User bindUser(ResultSet result) throws SQLException {
		
		User user = new User();
		user.setId(result.getInt("id"));
		user.setUsername(result.getString("username"));
		user.setHashCodePassword(result.getInt("password"));
		user.setRoot(result.getBoolean("root"));
		user.setAbleEmail(result.getBoolean("able_email"));
		user.setDtCreated(formatDate(result.getString("date_created")));
		
		return user;
	}
	
	public Customer bindCustomer(ResultSet result) throws SQLException, ParseException {
		
		DAOAddress daoAddress = new DAOAddress();
		Customer c = new Customer();
		
		c.setType(result.getString("type"));
		c.setPhone(result.getString("phone"));
		c.setName(result.getString("name"));
		c.setId(result.getInt("id"));
		c.setEmail(result.getString("email"));
		c.setDocument(result.getString("document"));
		c.setAddress(daoAddress.findById(result.getInt("address_id")));
		c.setDtCreated(formatDate(result.getDate("date_created")));
		
		return c;
	}
	
	public Product bindProduct(ResultSet result) throws SQLException, ParseException {
		
		Product p = new Product();
		p.setId(result.getInt("id"));
		p.setName(result.getString("name") != null? result.getString("name"): "");
		p.setCode(result.getString("code") != null? result.getString("code"): "");
		p.setBarcode(result.getString("barcode") != null? result.getString("barcode"):"");
		p.setUnitMeasure(result.getString("unitMeasure") != null? result.getString("unitMeasure"):"");
		p.setQuantity(result.getBigDecimal("quantity"));
		p.setQuantityMinimum(result.getBigDecimal("quantityMinimum"));
		p.setMaxQuantity(result.getBigDecimal("quantityMaximum"));
		p.setQuantityMinimumSale(result.getBigDecimal("quantityMinimumSale"));
		p.setPrice(result.getBigDecimal("price"));
		p.setDtCreated(formatDate(result.getString("date_created")));
		
		DAOMake daoMake = new DAOMake();
		p.setMake(daoMake.getMakeById(result.getInt("make_id")));
		if (p.getMake().getName() == null) {
			p.getMake().setName(result.getString("makeName"));
		}
		
		DAOTimeCover daoTime = new DAOTimeCover();
		p.setTimeCover(daoTime.getTimeCoverById(result.getInt("time_cover_id")));
		
		
		return p;		
	}
	
	public Order bindOrder(ResultSet result) throws SQLException, ParseException {
		
		DAOCustomer daoCustomer = new DAOCustomer();
		DAOProduct daoProduct = new DAOProduct();
		
		Order o = new Order();
		
		o.setId(result.getInt("id"));
		o.setCustomer(daoCustomer.findById(result.getInt("customer_id")));
		o.setProduct(daoProduct.findById(result.getInt("product_id")));
		o.setQuantity(Double.parseDouble(result.getString("quantity")));
		o.setSubTotal(Double.parseDouble(result.getString("subtotal")));
		o.setDtCreated(formatDate(result.getString("date_created")));
		
		return o;
	}
	
	public TimeCover bindTimeCover(ResultSet resultSet) throws SQLException {
		
		TimeCover tc = new TimeCover();
		
		tc.setDaysToOrder(resultSet.getInt("days_to_order"));
		tc.setDaysToDelivery(resultSet.getInt("days_to_delivery"));
		tc.setDaysToConference(resultSet.getInt("days_to_conference"));
		tc.setDaysMarginSecurity(resultSet.getInt("days_margin_security"));
		tc.setTotalDays(resultSet.getInt("total_days"));
		tc.setId(resultSet.getInt("id"));
		
		return tc;
		
	}
	
	/**
	 * 
	 * @param date 
	 * 		 yyyy/mm/dd: String
	 * @return
	 * 		Calendar 
	 */
	public Calendar formatDate(String date) {
		
		date = date.replaceAll("-", "/");
		
		Calendar cal = new GregorianCalendar();		
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(5,7)));
		cal.set(Calendar.YEAR,Integer.parseInt(date.substring(0,4)));
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(8,10)));
		
		return cal;
	}	
	
	public Calendar formatDate(Date date) {
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return cal;
	}	

}
