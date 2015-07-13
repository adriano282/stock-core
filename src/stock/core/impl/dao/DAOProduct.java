package stock.core.impl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import stock.core.application.Result;
import domain.DomainEntity;
import domain.Make;
import domain.Product;
import domain.TimeCover;

public class DAOProduct extends AbstractJdbcDAO{

	
	public DAOProduct() {
		super("product", "id");
	}
		
	@Override
	public Result save(DomainEntity domainEntity) {
		
		Result result = new Result();
		List<DomainEntity> entities = new ArrayList<DomainEntity>();
		
		Product product = (Product) domainEntity;
		Make make = (Make) product.getMake();
		TimeCover timeCover = product.getTimeCover();
		
		DAOMake daoMake = new DAOMake();
		
		if (daoMake.findMakeByName(make.getName()) == null) {
			
			daoMake.save(make);			
			product.setMake(daoMake.findMakeByName(make.getName()));
			
		} else {
			
			product.setMake(daoMake.findMakeByName(make.getName()));
			
		}
		
		DAOTimeCover daoTimeCover = new DAOTimeCover();
		daoTimeCover.save(timeCover);
		
		String query;		
		query = "INSERT INTO product(`name`, `code`, `barcode`, `unitMeasure`, `make_id`, `time_cover_id`,`date_created`, `quantity`, `quantityMinimum`, `quantityMaximum`, `quantityMinimumSale`, `price`) values( '" + product.getName() 
				+ "','" + product.getCode() + "','" + product.getBarcode() + "','" + product.getUnitMeasure() + "'," + daoMake.findMakeByName(make.getName()).getId()+ ", " + daoTimeCover.getLastTimeCover().getId() +", now(), "
				+ product.getQuantity() +", " + product.getQuantityMinimum() + ", " + product.getMaxQuantity() +", " + product.getQuantityMinimumSale() + ", " + product.getPrice() + ")";
		
		
		result.setMessage(executeQuery(query, "Produto"));
		
		product = (Product) this.getLastProduct();
		entities.add(product);
		result.setEntities(entities);
		
		return result;
	}

	
	@Override
	public Result update(DomainEntity domainEntity) {
		Result result = new Result();
		List<DomainEntity> entities = new ArrayList<DomainEntity>();
		
		
		Product product = (Product) domainEntity;
		Make make = (Make) product.getMake();
		TimeCover timeCover = product.getTimeCover();
			
				
		DAOMake daoMake = new DAOMake();
		if (daoMake.findMakeByName(make.getName()) == null) {
			
			daoMake.save(make);			
			product.setMake(daoMake.findMakeByName(make.getName()));
			
		} else {
			product.setMake(daoMake.findMakeByName(make.getName()));
		}			
		DAOTimeCover daoTimeCover = new DAOTimeCover();
		daoTimeCover.update(timeCover);
		
		
		String query;
		query = "UPDATE product set name = '" +product.getName() + "', code = '" + product.getCode() + "', barcode = '"
					+ product.getBarcode() + "', unitMeasure = '" + product.getUnitMeasure() + "', make_id = " + product.getMake().getId() 
					+ ", time_cover_id = " + product.getTimeCover().getId() + ", quantity = " + product.getQuantity() + ", quantityMinimum = " + product.getQuantityMinimum() + ",  quantityMaximum = " +product.getMaxQuantity() +", price = " 
					+ product.getPrice() +", quantityMinimumSale = " +product.getQuantityMinimumSale() + " where id = " + product.getId() + ";";
		
		result.setMessage(executeQuery(query, "Produto"));
		result.setEntities(entities);
		return result;
			
	}
	
	/**
	 * @author adriano
	 * 		email: adriano.jesus2@fatec.sp.gov.br
	 * 
	 * @param
	 * 		DomainEntity domainEntity: 
	 * 			Objeto preenchido com as informações a consultar
	 * 
	 * 		int offset: 
	 * 			Variável para receber a posição de ínicio de retorno do resultado da busca
	 * 
	 * 		Integer redordsPerPage:
	 * 			objeto para receber a quantidade de produtos a retornar    					
	 * 
	 * @return
	 * 		Result 			-
	 */
	@Override
	public Result consult(DomainEntity domainEntity, int offset, Integer recordsPerPage) {
		
		Product p = (Product) domainEntity;
		Result result = new Result();
		
		String whereClause = "";
		
		if (p.getId() != null) {
			
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "p.id = " + p.getId();
			
		} else {
			
			if (p.getDtCreated() != null) {
				
				whereClause += whereClause.equals("")? "where ": " and ";
				whereClause += "date(p.date_created) >= '" + p.getDtCreated().get(Calendar.YEAR) + "-"
						+ p.getDtCreated().get(Calendar.MONTH) + "-" 
						+p.getDtCreated().get(Calendar.DAY_OF_MONTH) + "' ";
						
			}
			
			if (p.getName() != null && !p.getName().equals("")) {
				
				whereClause += whereClause.equals("")? "where ": " and ";				
				whereClause += "p.name like '%" + p.getName()+ "%'";
			}
			
			if (p.getCode() != null && !p.getCode().equals("")) {
				
				whereClause += whereClause.equals("")? "where ": " and ";			
				whereClause += "p.code like '%" + p.getCode() + "%'";
			}
			 
			if (p.getBarcode() != null && !p.getBarcode().equals("")) {
				
				whereClause += whereClause.equals("")? "where ": " and ";
				whereClause += "p.barcode = " + p.getBarcode();
			}
			
			if (p.getMake() != null) {
				
				if (p.getMake().getId() != null) {
					
					whereClause += whereClause.equals("")? "where ": " and ";
					whereClause += "p.make_id = " + p.getMake().getId();
					
				} else if( p.getMake().getName() != null && !p.getMake().getName().equals("")){
					
					whereClause += whereClause.equals("")? "where ": " and ";			
					whereClause += "m.name like '%" + p.getMake().getName() + "%'";
				}
				
			}
			
			if (p.getUnitMeasure() != null && !p.getUnitMeasure().equals("")) {
				
				whereClause += whereClause.equals("")? "where ": " and ";			
				whereClause += "p.unitMeasure = '" + p.getUnitMeasure() +"'";
			}
			
		}
		
		
		String query = "select p.id as 'id', p.name as 'name', p.code as 'code', p.barcode as 'barcode',"
				+ "p.unitMeasure as 'unitMeasure', p.quantity as 'quantity', p.quantityMinimum as 'quantityMinimum',"
				+ "p.quantityMaximum as 'quantityMaximum' , p.price as 'price', p.quantityMinimumSale as 'quantityMinimumSale', "
				+ "p.date_created as 'date_created', m.name as makeName, m.id as 'make_id', p.time_cover_id as 'time_cover_id' from product p " +
				" inner join make m"
				+ "	on m.id = make_id " + whereClause;
		
		if (recordsPerPage == null) {
			
			query = query + ";";
			
		} else {
			
			query = query + " limit " + offset +","+recordsPerPage+";";
		}
		
		result = executeSelect(query, Product.class.getName(), whereClause );
		result.setEntityFiltered(p);
		
		return result;
		
	}
	
	public Product findByBarcode(String barcode) {
		
		Product p = new Product();
		openConnection();
		try {
			
			String query = "select * from product where barcode = '" + barcode + "'";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			if (result.next()) {
				return dataBind.bindProduct(result);
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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
		return p;
	}
	
	public Product findByCode(String code) {
		Product p = new Product();
		openConnection();
		try {
			
			String query = "select * from product where code = '" + code + "'";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			if (result.next()) {
				return dataBind.bindProduct(result);
			}else {
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
		return p;
	}
	public Product findById(int id) {
		
		Product p = new Product();
		openConnection();
		try {
			
			String query = "select * from product where id = " + id;
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			if (result.next()) {
				return dataBind.bindProduct(result);
			}
			return p;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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
		return p;
	}
	
	public DomainEntity getLastProduct() {
		Product p = new Product();
		openConnection();
		try {			
			String query = "select * from product where id = (select max(id) from product)";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			if (result != null) {
				while (result.next()) {
					return dataBind.bindProduct(result);
				}
			}
			return p;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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
	public List<DomainEntity> findAllByName(String name) {
		
		List<DomainEntity> products = new ArrayList<DomainEntity>();
		openConnection();
		try {			
			String query = "select * from product where name like '%" + name + "%'";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			if (result != null) {
				while (result.next()){
					products.add(dataBind.bindProduct(result));
				}
			}
			return products;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public List<DomainEntity> listAll() {
		
		List<DomainEntity> products = new ArrayList<DomainEntity>();
		openConnection();
		try {
			
			String query = "select * from product";
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			if (result != null) {
				while (result.next()) {
					products.add(dataBind.bindProduct(result));
				}
			}
			return products;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
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

	@Override
	public Result consult(DomainEntity domainEntity) {
		Product p = (Product) domainEntity;
		Result result = new Result();
		
		String whereClause = "";
		if (p.getId() != null) {
			whereClause += whereClause.equals("")? "where ": " and "; 			
			whereClause += "id = " + p.getId();
		} else {
			if (p.getName() != null && !p.getName().equals("")) {
				whereClause += whereClause.equals("")? "where ": " and ";				
				whereClause += "name like '%" + p.getName()+ "%'";
			}
			
			if (p.getCode() != null && !p.getCode().equals("")) {
				whereClause += whereClause.equals("")? "where ": " and ";			
				whereClause += "code like '%" + p.getCode() + "%'";
			}
			 
			if (p.getBarcode() != null && !p.getBarcode().equals("")) {
				whereClause += whereClause.equals("")? "where ": " and ";
				whereClause += "barcode = " + p.getBarcode();
			}
			
			if (p.getMake() != null && p.getMake().getId() != null) {
				whereClause += whereClause.equals("")? "where ": " and ";			
				whereClause += "make_id = " + p.getMake().getId();
			}
			
			if (p.getUnitMeasure() != null && !p.getUnitMeasure().equals("")) {
				whereClause += whereClause.equals("")? "where ": " and ";			
				whereClause += "unitMeasure = '" + p.getUnitMeasure() +"'";
			}			
		}
		String query = "select * from product;";
		result = executeSelect(query, Product.class.getName());
		result.setEntityFiltered(p);
		return result;
	}
	
	
}
