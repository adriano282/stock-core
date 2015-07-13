package stock.core.service;

import java.math.BigDecimal;

import stock.core.impl.dao.DAOProcessStock;
import domain.DomainEntity;
import domain.Product;

public class StockService {
	
	private static StockService  instance = new StockService();
	
	private StockService() {
		
	}
	
	public static StockService getInstance() {
		return instance;
	}
	
	public Product processStock(DomainEntity entity) {
		
		Product p = (Product) entity;
		Double stockMinimum = getStockMinimun(p);
		
		if (stockMinimum == null) {
			return null;
		}
		
		p.setQuantityMinimum( new BigDecimal(stockMinimum).setScale(2, BigDecimal.ROUND_HALF_UP));
		
		return p;
	}
	
	private Double getStockMinimun(Product p) {
		
		DAOProcessStock dao = new DAOProcessStock();
		
		Double factor = Double.parseDouble(p.getTimeCover().getTotalDays().toString())/30;
		String unidade = p.getUnitMeasure();
		
		Double average = dao.avgQuatityByMonth(p.getId());
		
		if (average == null)
			return null;
		
		Double resultado =  factor * average;
		
		if ( unidade != Product.KILOGRAMA && unidade != Product.LITRO && unidade != Product.METRO) {
			return (double) Math.round(resultado);
		}
		
		return resultado;					
		
	}

}
