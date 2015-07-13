package stock.core.impl.reports.dynamicreports;

import java.util.HashMap;
import java.util.Map;

import domain.Customer;
import domain.DomainEntity;
import domain.Order;
import domain.Product;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;

public class ColumnsReportDomain {
	
	private Map<String, TextColumnBuilder<?>> listColumns;
		
	public Map<String, TextColumnBuilder<?>> getColumnsByDomain(DomainEntity domain) {
		
		if (domain instanceof Customer) {
			
			listColumns = new HashMap<String, TextColumnBuilder<?>>();
			listColumns.put("id", Columns.column("ID", "id", DynamicReports.type.integerType()));
			listColumns.put("name", Columns.column("Nome Cliente", "name", DynamicReports.type.stringType()));
			listColumns.put("email",Columns.column("Email Cliente", "email", DynamicReports.type.stringType()));
			listColumns.put("document",Columns.column("N. Documento", "document", DynamicReports.type.stringType()));
			listColumns.put("phone", Columns.column("Telefone Cliente", "phone", DynamicReports.type.stringType()));
			listColumns.put("type", Columns.column("Tipo", "type", DynamicReports.type.stringType()));
			listColumns.put("dtCreated", Columns.column("Data de Cadastro", "stringDate", DynamicReports.type.stringType()));
			
			return listColumns;								
			
		} else if (domain instanceof Order) {
			
			listColumns = new HashMap<String, TextColumnBuilder<?>>();
			
			listColumns.put("id", Columns.column("Número do Pedido", "id", DynamicReports.type.integerType()));			
			listColumns.put("customer.name",Columns.column("Nome do Cliente", "customer.name", DynamicReports.type.stringType()));
			listColumns.put("customer.document",Columns.column("No. Documento do Cliente", "customer.document", DynamicReports.type.stringType()));
			listColumns.put("customer.email",Columns.column("Email do Cliente", "customer.email", DynamicReports.type.stringType()));
			listColumns.put("product.name",Columns.column("Nome do Produto", "product.name", DynamicReports.type.stringType()));
			listColumns.put("product.code",Columns.column("Código do Produto", "product.code", DynamicReports.type.stringType()));
			listColumns.put("product.make.name",Columns.column("Marca do Produto", "product.make.name", DynamicReports.type.stringType()));
			
			listColumns.put("quantity",Columns.column("Quantidade", "quantity", DynamicReports.type.doubleType()));
			listColumns.put("subTotal",Columns.column("Valor Total", "subTotal",  new PriceType()));
			listColumns.put("dtCreated",Columns.column("Data Venda", "month_name", DynamicReports.type.stringType()));
			
			return listColumns;					
			
		} else if (domain instanceof Product) {
			
			listColumns = new HashMap<String, TextColumnBuilder<?>>();
			
			listColumns.put("id", Columns.column("Id", "id", DynamicReports.type.integerType()));
			listColumns.put("name", Columns.column("Nome", "name", DynamicReports.type.stringType()));
			listColumns.put("code", Columns.column("Código", "code", DynamicReports.type.stringType()));
			listColumns.put("barcode", Columns.column("Código de Barras", "barcode", DynamicReports.type.stringType()));
			listColumns.put("unitMeasure", Columns.column("Unidade Medida", "unitMeasure", DynamicReports.type.stringType()));
			listColumns.put("make.name", Columns.column("Marca", "make.name", DynamicReports.type.stringType()));
			listColumns.put("price", Columns.column("Preço", "price", DynamicReports.type.bigDecimalType()));
			listColumns.put("quantity", Columns.column("Estoque", "quantity", DynamicReports.type.bigDecimalType()));
			listColumns.put("quantityMinimum", Columns.column("Estoque Mínimo", "quantityMinimum", DynamicReports.type.bigDecimalType()));
			listColumns.put("quantityMinimumSale", Columns.column("Quantidade Mínima Venda", "quantityMinimumSale", DynamicReports.type.bigDecimalType()));
			listColumns.put("totalDays", Columns.column("Tempo de Cobertura", "timeCover.totalDays", DynamicReports.type.integerType()));
			listColumns.put("dtCreated",Columns.column("Data Cadastro", "stringDate", DynamicReports.type.stringType()));
			
			return listColumns;						
		}
		
		return null;
		
	}
	
		
	

}
