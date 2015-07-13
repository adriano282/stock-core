package stock.core.impl.reports.dynamicreports;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.mysql.fabric.xmlrpc.base.Data;

import stock.core.impl.dao.DAOCustomer;
import stock.core.impl.dao.DAOOrder;
import stock.core.impl.dao.DAOProduct;
import stock.core.impl.reports.condicional.LowerStockConditionExpression;
import domain.Customer;
import domain.DomainEntity;
import domain.Order;
import domain.Product;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.constant.ImageType;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.FieldBuilder;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.chart.Bar3DChartBuilder;
import net.sf.dynamicreports.report.builder.chart.BarChartBuilder;
import net.sf.dynamicreports.report.builder.chart.CategoryChartSerieBuilder;
import net.sf.dynamicreports.report.builder.chart.LineChartBuilder;
import net.sf.dynamicreports.report.builder.column.Columns;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.ConditionalStyleBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.AxisPosition;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.Evaluation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.exception.DRException;

public class DynamicReportGettingStarted {
	
	private void configReport(JasperReportBuilder report, DomainEntity domain, String nameAttributeGroup, boolean grafico, boolean listagem) {
		
		
		ColumnsReportDomain buidColumns = new ColumnsReportDomain();
		Map<String,TextColumnBuilder<?>> listColumns = buidColumns.getColumnsByDomain(domain);
		
		  StyleBuilder subtotalStyle = stl.style()
    		  .bold()
    		  .setTopBorder(stl.pen1Point())
    		  .setHorizontalAlignment(HorizontalAlignment.CENTER);
	      
		if (domain instanceof Customer) {
			
			TextColumnBuilder<String> columnDate = Columns.column("Data de Cadastro", "stringDate", DynamicReports.type.stringType());
			
			if (listagem) {
				
				if (nameAttributeGroup != null && !nameAttributeGroup.equals("")) {
					report.groupBy(listColumns.get(nameAttributeGroup));
					report.subtotalsAtFirstGroupFooter(DynamicReports.sbt.count(columnDate), DynamicReports.sbt.text("Total Clientes", listColumns.get("name")));
					
				}
				
			} else if (grafico) {
						
				TextColumnBuilder<String> column = Columns.column("Data Cadastro", "stringDate", DynamicReports.type.stringType());
				TextColumnBuilder<Integer> columnId	= Columns.column("ID", "id", DynamicReports.type.integerType());
				FieldBuilder<String> dateField = field("stringDate", type.stringType());				
				
				VariableBuilder<Integer> itemVariable = variable(columnId, Calculation.COUNT);
	      	      itemVariable.setResetType(Evaluation.FIRST_GROUP);
	    
	      	    Bar3DChartBuilder chart1 = cht.bar3DChart()
					.setCategory(dateField)
					.series(cht.serie(itemVariable).setLabel("Quantidade de Clientes Cadastrados"))
					.setValueAxisFormat(cht.axisFormat().setLabel("Quantidade de Clientes Cadastrados"));
	      	    
	      	  report
				.summary(DynamicReports.cmp.horizontalFlowList(chart1))
				.sortBy(column);
				
			}
			
			report.columns(
					listColumns.get("id"),
					listColumns.get("name"),
					listColumns.get("email"),
					listColumns.get("document"),
					listColumns.get("phone"),
					listColumns.get("type"),
					listColumns.get("dtCreated"));
													
		} else if (domain instanceof Order) {
			
			TextColumnBuilder<Double> columnQuantity = Columns.column("Quantidade", "quantity", DynamicReports.type.doubleType());
			TextColumnBuilder<Double> columnTotal = Columns.column("Valor Total", "subTotal",  new PriceType());
			TextColumnBuilder<String> columnData = Columns.column("Data Venda", "month_name", DynamicReports.type.stringType());
						
			if (listagem) {				
				if (nameAttributeGroup != null && !nameAttributeGroup.equals("")) {
					
					report.groupBy(grp.group(listColumns.get(nameAttributeGroup)));
					report.subtotalsAtFirstGroupFooter(DynamicReports.sbt.sum(columnQuantity), DynamicReports.sbt.text("Total Itens",columnQuantity))
					.addSubtotalAtFirstGroupFooter(DynamicReports.sbt.sum(columnTotal), DynamicReports.sbt.text("Valor Total", columnTotal));
					report.subtotalsAtFirstGroupFooter(DynamicReports.sbt.count(columnData), DynamicReports.sbt.text("Quantidade", columnData));
				}
				
				report
					.columns(
						listColumns.get("id"),
						listColumns.get("customer.name"),
						listColumns.get("customer.document"),
						listColumns.get("customer.email"),
						listColumns.get("product.name"),
						listColumns.get("product.code"),
						listColumns.get("product.make.name"),
						columnQuantity,
						columnTotal,
						columnData);
			}
						
			if (grafico) {
				// charts			
								
				FieldBuilder<String> dateField = field("month_name", type.stringType());
				FieldBuilder<Double> quantityField = field("quantity", type.doubleType());
				FieldBuilder<Double> valueField = field("subTotal", new PriceType());
				FieldBuilder<Integer> vendaIdField = field("id", type.integerType());
				
				
				CategoryChartSerieBuilder quantitySerie = cht.serie(quantityField)
						.setLabel("Quantidade Itens");
				
				Bar3DChartBuilder chart2 = cht.bar3DChart()
						.setCategory(dateField)
						.addSerie(quantitySerie)
						.setValueAxisFormat(cht.axisFormat().setLabel("Quantidade de Itens"));
				
				
				
				CategoryChartSerieBuilder valueSubtotalSerie = cht.serie(valueField)
						.setLabel("Valor Total R$");
				
				LineChartBuilder chart1 = cht.lineChart()
						.setCategory(dateField)
						.series(valueSubtotalSerie)
						.setValueAxisFormat(cht.axisFormat().setLabel("Valor Total R$"));
						
								
				report
					.fields(dateField)
					.title(
						cht.multiAxisChart()
		                  .addChart(chart1, AxisPosition.LEFT_OR_TOP)
		                  .addChart(chart2, AxisPosition.RIGHT_OR_BOTTOM));
			}			
			report.sortBy(columnData);
							
		} else if (domain instanceof Product) {
			
		      ConditionalStyleBuilder condition1 = stl.conditionalStyle(new LowerStockConditionExpression())
		    		  .setBackgroundColor(new Color(255, 210, 210));
		    		 
    		  StyleBuilder lowerStockStyle = stl.style()
    		  .conditionalStyles(condition1);
    		  		    				
			if (nameAttributeGroup != null && !nameAttributeGroup.equals("")) {
											      
				report
					.groupBy(listColumns.get(nameAttributeGroup))		
					.subtotalsAtSummary(DynamicReports.sbt.sum(Columns.column("Estoque", "quantity", DynamicReports.type.bigDecimalType())).setStyle(subtotalStyle).setLabel("Soma Estoque"))
					.subtotalsAtSummary(DynamicReports.sbt.sum(Columns.column("Preço", "price", DynamicReports.type.bigDecimalType())).setStyle(subtotalStyle).setLabel("Valor Estoque"));					
				
			}

			report.columns(
					listColumns.get("id").setStyle(lowerStockStyle),
					listColumns.get("name").setStyle(lowerStockStyle),
					listColumns.get("code").setStyle(lowerStockStyle),
					listColumns.get("barcode").setStyle(lowerStockStyle),
					listColumns.get("unitMeasure").setStyle(lowerStockStyle),
					listColumns.get("make.name").setStyle(lowerStockStyle),
					listColumns.get("price").setStyle(lowerStockStyle),
					listColumns.get("quantity").setStyle(lowerStockStyle),
					listColumns.get("quantityMinimum").setStyle(lowerStockStyle),
					listColumns.get("quantityMinimumSale").setStyle(lowerStockStyle),
					listColumns.get("totalDays").setStyle(lowerStockStyle));
			
		}
						
	}	
	
	private boolean generateReport(List<DomainEntity> entities, String nameAttributeGroup, String titleReport, boolean grafico, boolean listagem) throws Exception {
					
		DomainEntity domain = entities.get(0);
		JasperReportBuilder report = DynamicReports.report();
				
		//report.title(Templates.createTitleComponent(titleReport));
		
		configReport(report, domain, nameAttributeGroup,grafico, listagem );
		
			
			setLayoutReport(report); 
			report				
				.setDataSource(entities)
				//.toImage(new FileOutputStream(new File("/home/adriano/development/workspace/stock-web/WebContent/" +titleReport + ".png")), ImageType.PNG);
				//.toImage(new FileOutputStream(new File("/home/adriano/development/workspace/stock-web/" +titleReport + ".jpg")), ImageType.JPG)
				.toPdf(new FileOutputStream(new File("/home/adriano/development/workspace/stock-web/WebContent/" +titleReport + ".pdf")));
			
		System.out.println("Report completed...");
		return true;	

	}
	
	private void setLayoutReport(JasperReportBuilder report) {
		
		StyleBuilder boldStyle = DynamicReports.stl.style().bold();
		StyleBuilder boldCentered = DynamicReports.stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
		StyleBuilder columnHeaderStyle = DynamicReports.stl.style(boldCentered).setBorder(DynamicReports.stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);
		
		report
			.setColumnTitleStyle(columnHeaderStyle)
			//.setTemplate(Templates.reportTemplate)
			//.pageFooter(Templates.footerComponent)
			.highlightDetailEvenRows();
		
	}
	
	public void exportReport(List<DomainEntity> entities, String nameAttributeGroup, String titleReport, boolean grafico, boolean listagem) throws Exception {
				
		generateReport(entities, nameAttributeGroup, titleReport, grafico, listagem);				
	}
	
	public void exportReport(List<DomainEntity> entities, String titleReport, boolean grafico, boolean listagem) throws Exception {
		
		generateReport(entities, null, titleReport, grafico, listagem);				
	}
	
	 
	public static void main(String[] args) throws Exception  {
		
		DAOCustomer dao = new DAOCustomer();
		DAOOrder daoOrder = new DAOOrder();
		DAOProduct daoP = new DAOProduct();
		
		DynamicReportGettingStarted dr = new DynamicReportGettingStarted();
		
		//List<DomainEntity> customers = dao.consult(new Customer(), 0, null).getEntities();
		//dr.exportReport(customers,"dtCreated", "Clientes cadastrados no Sistema",true, false);
		
		List<DomainEntity> orders = daoOrder.consult(new Order()).getEntities();
		dr.exportReport(orders,"dtCreated", "Vendas", true, false);
				
		List<DomainEntity> produtos = daoP.consult(new Product()).getEntities();
		dr.exportReport(produtos, "Listagem de Produtos", false, true);
	}


}
