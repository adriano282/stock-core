package stock.core.impl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import domain.DomainEntity;
import stock.core.application.Result;

public class DAOReports extends AbstractJdbcDAO {
	
	public DAOReports() {
		super("report", "id");
	}

	/**
	 * Método que realiza a consulta no banco de dados para 
	 * levantar a quantidade de compras dos clientes.
	 * Está query é agrupada pelas quantidades de vendas 
	 * e sua respectiva quantidade de clientes que as realizaram.
	 * Ex.:		
	 * 		Qtde de Clientes Que Realizaram |  Qtde Compras
	 * 					Somente Uma			| 		10
	 * 					Mais de 1			|		6
	 * 					Mais de 5			|		4
	 * 
	 * Obs.: Os cliente que são contabilizados em quantidades 
	 * superiores não serão contabilizados em quantidades menores
	 * 
	 * 		Ex.: Clientes que compraram mais de 5 vezes não seram 
	 * 		contabilizados no Grupo Mais de Uma e Somente Uma 
	 * @return
	 */
	public DefaultCategoryDataset getQtdSaleSCustomer() {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset() {
			
			private static final long serialVersionUID = 1L;
			
			protected void finalize() throws Throwable {
				super.finalize();
			}
		};
		
		
		String query = 
					"select  'Somente 1' as 'Quantidades_de_Compra', count( if(T1.qtde_compras = 1 , 1, NULL)) as 'Qtde_clientes' "
				+ 	"	from (select  o.customer_id,count(*) as 'qtde_compras' from `order` o group by o.customer_id) as T1 "
				+ 	" union "
				+	"select  'Mais_de_1' as 'Quantidades_de_Compra', count( if(T1.qtde_compras > 1 && T1.qtde_compras < 5 , 1, NULL)) as 'Qtde_clientes' "
				+ 	"	from (select  o.customer_id,count(*) as 'qtde_compras' from `order` o group by o.customer_id) as T1 "
				+ 	" union "
				+ 	"select  'Mais_de_5' as 'Quantidades_de_Compra', count( if(T1.qtde_compras > 5 && T1.qtde_compras < 15, 1, NULL)) as 'Qtde_clientes' "
				+ 	"	from (select  o.customer_id,count(*) as 'qtde_compras' from `order` o group by o.customer_id) as T1 "
				+ 	" union "
				+ 	" select  'Mais_de_10' as 'Quantidades_de_Compra', count( if(T1.qtde_compras > 10 && T1.qtde_compras < 15 , 1,  NULL)) as 'Qtde_clientes' "
				+ 	"	from (select  o.customer_id,count(*) as 'qtde_compras' from `order` o group by o.customer_id) as T1 "
				+ 	" union "
				+ 	" select  'Mais_de_15' as 'Quantidades_de_Compra', count( if(T1.qtde_compras > 15 , 1,  NULL)) as 'Qtde_clientes' "
				+ 	"	from (select  o.customer_id,count(*) as 'qtde_compras' from `order` o group by o.customer_id) as T1;";
		
		
		openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.addValue(
						result.getInt("Qtde_clientes"),
						"Qtde_clientes",
						result.getString("Quantidades_de_Compra"));
			}
			
			CategoryAxis ca = new CategoryAxis();
			ca.addCategoryLabelToolTip("Quantidades_de_Compra", "Quantidades de Compra");
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;

		
	}
	
	
	/**
	 * Metódo para realizar consulta no banco e levantar a quantidade de vendas mensal
	 * @return
	 * 		DefaultCategoryDataset - Objeto da biblioteca CWolf preenchido com os valores do resultado da query
	 */
	public DefaultCategoryDataset getCountSalesByMonth() {
		
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset(){
			private static final long serialVersionUID = 1L;

			protected void finalize() throws Throwable {
				super.finalize();
			}
	    };
	    
	    String query = "select "
        		+ "concat(monthname(o.date_created), '/', year(o.date_created)) as 'periodo', "
        		+ "count(*) as 'quantidade_vendas', "
        		+ "round(sum(o.subTotal), 2) as 'valor_vendas' "
        		+ "from `order` o "
        		+ "group by concat(monthname(o.date_created), '/', year(o.date_created))"
        		+ "order by month(o.date_created) asc";
	    
	    openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.addValue(
						result.getInt("quantidade_vendas"),
						"quantidade_vendas",
						result.getString("periodo")
				);
			}
			
			CategoryAxis ca = new CategoryAxis();
			ca.addCategoryLabelToolTip("quantidade_vendas", "Quantidade de Vendas");
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;
	    
        
	}
	
	public DefaultCategoryDataset getCountSalesBestSellersProducts() {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset(){
			private static final long serialVersionUID = 1L;

			protected void finalize() throws Throwable {
				super.finalize();
			}
	    };
	    
	    String query = 
	    		  "select 	p.code 			as 'Codigo',"
	    		+ "			p.name 			as 'Nome',"
	    		+ "			count(o.id)		as 'Quantidade de Vendas',"
	    		+ "			sum(o.quantity) as 'Quantidade de Itens' "
	    		+ "from product p"
	    		+ "		inner join `order` o"
	    		+ "			on o.product_id = p.id "
	    		+ "group by p.id "
	    		+ "order by count(o.id) desc limit 10;";
	    
	    openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.addValue(
						result.getDouble("Quantidade de Vendas"),
						"Quantidade de Vendas",
						result.getString("Codigo")
				);
			}
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;

	}
	
	/**
	 * @getSumItensBestSellersProducts
	 * 
	 * 		Método que realiza consulta no banco para levantar a 
	 * 		soma da quantidade de itens vendidos por produto dos 
	 * 		10 produtos mais vendidos.
	 *  
	 * @return
	 * 
	 * 		DefaultCategoryDataset - Objeto da biblioteca CWolf 
	 * 		preenchido com os valores do resultado da query.
	 */
	public DefaultCategoryDataset getSumItensBestSellersProducts() {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset(){
			private static final long serialVersionUID = 1L;

			protected void finalize() throws Throwable {
				super.finalize();
			}
	    };
	    
	    String query = 
	    		  "select 	p.code 			as 'Codigo',"
	    		+ "			p.name 			as 'Nome',"
	    		+ "			count(o.id)		as 'Quantidade de Vendas',"
	    		+ "			sum(o.quantity) as 'Quantidade de Itens' "
	    		+ "from product p"
	    		+ "		inner join `order` o"
	    		+ "			on o.product_id = p.id "
	    		+ "group by p.id "
	    		+ "order by count(o.id) desc limit 10;";
	    
	    openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.addValue(
						result.getDouble("Quantidade de Itens"),
						"Quantidade de Itens",
						result.getString("Codigo")
				);
			}
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;

	}
	
	/**
	 * 
	 * @getCountProductByStatusStorage
	 * 		
	 * 		Método para realizar consulta no banco de dados
	 * 		para retornar a quantidade de produtos que estão abaixo 
	 * 		do estoque mínimo, estoque normal e estoque cheio
	 * 
	 * @return
	 * 
	 * 		DefaultCategoryDataset - objeto da biblioteca do 
	 * 		CeWolf preenchida com o resultado da consulta
	 */
	public DefaultPieDataset getCountProductByStatusStorage() {
		
		DefaultPieDataset dataset = new DefaultPieDataset() {
			
			private static final long serialVersionUID = 1L;
			protected void finalize() throws Throwable {
				super.finalize();
			}
		};
	    
	    String query 
	    		= "select	'Abaixo Estoque Mínimo' as 'Situação', "
	    		+ "	   		count(p.id)				as 'Quantidade de Produtos' "
	    		+ "from `product` p "
	    		+ "where p.quantity < p.quantityMinimum "
	    		+ "union "
	    		+ "select 	'Estoque Normal' 	as 'Situação', "
	    		+ "	   		count(p.id)			as 'Quantidade de Produtos' "
	    		+ "from `product` p "
	    		+ "where p.quantity > p.quantityMinimum and p.quantity < p.quantityMaximum "
	    		+ "union "
	    		+ "select 	'Estoque cheio' 	as 'Situação', "
	    		+ "	   		count(p.id)			as 'Quantidade de Produtos' "
	    		+ "from `product` p "
	    		+ "where p.quantity = p.quantityMaximum";
	    
	    openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.setValue(
						result.getString("Situação"), new Double(result.getDouble("Quantidade de Produtos"))
				);
			}
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;

	}
	
	/**
	 * 
	 * @getCountProductByStorageLevel
	 * 		
	 * 		Método para realizar consulta no banco de dados
	 * 		para retornar a quantidade de produtos que estão abaixo 
	 * 		do estoque mínimo, estoque normal e estoque cheio
	 * 
	 * @return
	 * 
	 * 		DefaultCategoryDataset - objeto da biblioteca do 
	 * 		CeWolf preenchida com o resultado da consulta
	 */
	public DefaultPieDataset getCountProductByStorageLevel() {
		
		DefaultPieDataset dataset = new DefaultPieDataset() {
			
			private static final long serialVersionUID = 1L;
			protected void finalize() throws Throwable {
				super.finalize();
			}
		};
	    
		String query 	= "select 	'Entre 0% e 25%'										as 'Capacidade de Armazenamento Atingida', "
	    				+ "			count(if(T.atual <= 25, T.id, null)) 					as 'Quantidade de Produtos' "
	    				+ "from "
	    				+ "(select 	p.id 											as id, "
	    				+ "	   		round((p.quantity*100)/p.quantityMaximum, 2) 	as 'atual' "
	    				+ "from `product` p) as T "
	    				+ "union all "
	    				+ "select 	'Entre 26% e 50%'										as 'Capacidade de Armazenamento Atingida', "
	    				+ "			count(if(T.atual >= 26 && T.atual <= 50, T.id, null)) 	as 'Quantidade de Produtos' "
	    				+ "from "
	    				+ "(select 	p.id 											as id, "
	    				+ "	   		round((p.quantity*100)/p.quantityMaximum, 2) 	as 'atual' "
	    				+ "from `product` p) as T "
	    				+ "union all "
	    				+ "select 	'Entre 51% e 75%'										as 'Capacidade de Armazenamento Atingida', "
	    				+ "			count(if(T.atual >= 50 && T.atual <= 75, T.id, null)) 	as 'Quantidade de Produtos' "
	    				+ "from "
	    				+ "(select 	p.id 											as id, "
	    				+ "	   		round((p.quantity*100)/p.quantityMaximum, 2) 	as 'atual' "
	    				+ "from `product` p) as T "
	    				+ "union all "
	    				+ "select 	'Entre 76% e 100%'										as 'Capacidade de Armazenamento Atingida', "
	    				+ "		count(if(T.atual >= 75 && T.atual <= 100, T.id, null)) 		as 'Quantidade de Produtos' "
	    				+ "from "
	    				+ "(select p.id 											as id, "
	    				+ "	   round((p.quantity*100)/p.quantityMaximum, 2) 		as 'atual' "
	    				+ "from `product` p) as T";
	    
	    openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.setValue(
						result.getString("Capacidade de Armazenamento Atingida"), new Double(result.getDouble("Quantidade de Produtos"))
				);
			}
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;

	}



	/**
	 * @getCountSaleByTypeCustomer
	 * 
	 * 		Método para realizar consulta no banco de dados
	 * 		para retornar a quantidade de vendas por tipo de
	 * 		cliente, ou seja pesso jurídica ou pessoa física.
	 * 
	 * @return
	 * 
	 * 		DefaultCategoryDataset - objeto da biblioteca do 
	 * 		CeWolf preenchida com o resultado da consulta
	 */
	public DefaultPieDataset getCountSaleByTypeCustomer() {
		
		DefaultPieDataset dataset = new DefaultPieDataset() {
			
			private static final long serialVersionUID = 1L;
			protected void finalize() throws Throwable {
				super.finalize();
			}
		};
	    
	    String query 
	    		= "select	c.type			as tipo_cliente, "
	    		+ "			round((count(o.id)* 100)/T2.total, 2) 	as qtde_vendas "
	    		+ "from `order` o "
	    		+ " 	join (select count(o.id) as total from `order` o) as T2"
	    		+ "		inner join customer c "
	    		+ "			on c.id = o.customer_id "
	    		+ "group by c.type;";
	    
	    openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.setValue(
						result.getString("tipo_cliente"), new Double(result.getDouble("qtde_vendas"))
				);
			}
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;

	}
	
	/**
	 * @getSumSalesByMonth
	 * 
	 * 		Método que realiza consulta no 
	 * 		banco para levantar a soma dos valores das vendas mensalmente
	 * 
	 * @return
	 * 
	 * 		DefaultCategoryDataset - Objeto da biblioteca CWolf 
	 * 		preenchido com os valores do resultado da query	 
	 * 
	 **/
	public DefaultCategoryDataset getSumSalesByMonth() {
		
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset(){
			private static final long serialVersionUID = 1L;

			protected void finalize() throws Throwable {
				super.finalize();
			}
	    };
	    
	    String query = "select "
        		+ "concat(monthname(o.date_created), '/', year(o.date_created)) as 'periodo', "
        		+ "count(*) as 'quantidade_vendas', "
        		+ "round(sum(o.subTotal), 2) as 'valor_vendas' "
        		+ "from `order` o "
        		+ "group by concat(monthname(o.date_created), '/', year(o.date_created))"
        		+ "order by month(o.date_created) asc";
	    
	    openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.addValue(
						result.getDouble("valor_vendas"),
						"Valor das Vendas",
						result.getString("periodo")
				);
			}
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;
	    
        
	}
	
	/**
	 * @getSalesByDaysOfWeek
	 * 		
	 * 		Método para realizar consulta no banco de dados 
	 * 		para levantar a quantidade de vendas em cada dia
	 * 		da semana.
	 * 
	 * @return
	 * 		
	 * 		DefaultCategoryDataset - Objeto da biblioteca CWolf
	 * 		preenchindo com o resultado da consulta
	 */
	public DefaultPieDataset getSalesByDaysOfWeek() {
		
		DefaultPieDataset dataset = new DefaultPieDataset() {
			
			private static final long serialVersionUID = 1L;
			protected void finalize() throws Throwable {
				super.finalize();
			}
		};
		
	    String query = 
	    		" select	case DATE_FORMAT(o.date_created,'%w')"
		    		+ "			when 0 then 'Domingo'"
		    		+ "            when 1 then 'Segunda'"
		    		+ "            when 2 then 'Terça'"
		    		+ "            when 3 then 'Quarta'"
		    		+ "            when 4 then 'Quinta'"
		    		+ "            when 5 then 'Sexta'"
		    		+ "			else"
		    		+ "				'Sábado'"
	    		+ "			end							as dia_semana,"
	    		+ "		round((count(o.id)*100) / T2.total,2)						as qtde_vendas "
	    		+ "from `order` o "
	    		+ "join (select count(o.id) as total from `order` o) as T2 "
	    		+ "group by dia_semana " 
	    		+ "order by date_format(o.date_created, '%w') asc ";
	    		
	    
	    openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.setValue(result.getString("dia_semana"),new Double(result.getDouble("qtde_vendas")));
			}
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;

	
		
	}
		
	/**
	 * 
	 * @getCountCustomerByMonth
	 * 
	 * 		Método que realiza consulta no banco para 
	 *		levantar a quantidade de cadastros de clientes mensalmente*
	 *
	 *
	 * @return
	 * 
	 * 		DefaultCategoryDataset - Objeto da biblioteca CWolf 
	 * 		preenchido com os valores do resultado da query
	 */
	public DefaultCategoryDataset getCountCustomersByMonth() {
	
		DefaultCategoryDataset dataset = new DefaultCategoryDataset(){
			private static final long serialVersionUID = 1L;

			protected void finalize() throws Throwable {
				super.finalize();
			}
	    };
	    
	    String query = "select "
	    		+ "concat(monthname(c.date_created), '/', year(c.date_created)) as 'periodo', "
	    		+ "count(*) as 'clientes'  "
	    		+ "from customer c "
	    		+ "group by concat(monthname(c.date_created), '/', year(c.date_created))"
	    		+ "order by month(c.date_created) asc";
	    
	    openConnection();
	    
	    try {
	    	
			Statement statement = connection.createStatement();			
			ResultSet result = statement.executeQuery(query);
			
			while(result.next()) {
				
				dataset.addValue(
						result.getDouble("clientes"),
						"Qtde Cadastro de Clientes",
						result.getString("periodo")
				);
			}
			
			return dataset;
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		} finally {
			
			closeConnection();
			
		}
		return dataset;
	    
	    
	}
    
	
	@Override
	public Result save(DomainEntity domainEntity) {
		return null;
		
	}

	@Override
	public Result update(DomainEntity domainEntity) {
		return null;
		
	}

	@Override
	public Result consult(DomainEntity domainEntity) {
		return null;
		
	}

	@Override
	public Result consult(DomainEntity entity, int offset, Integer recordsPerPage) {
		return null;
		
			
	}
	
}
