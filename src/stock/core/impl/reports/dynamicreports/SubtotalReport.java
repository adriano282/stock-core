package stock.core.impl.reports.dynamicreports;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.subtotal.AggregationSubtotalBuilder;
import net.sf.dynamicreports.report.builder.subtotal.PercentageSubtotalBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;




public class SubtotalReport {

   private StyleBuilder boldStyle;
   private TextColumnBuilder<Integer> column2;

   public SubtotalReport() {	   
	   build();
   }
   private void build() {	
		
	   boldStyle = DynamicReports.stl.style().bold().setHorizontalAlignment(HorizontalAlignment.RIGHT);

	
		TextColumnBuilder<String> column1 = DynamicReports.col.column("Column1", "column1", DynamicReports.type.stringType());
		column2 = DynamicReports.col.column("Column2", "column2", DynamicReports.type.integerType());
		ColumnGroupBuilder columnGroup = DynamicReports.grp.group(column1);
		
		try {
			JasperReportBuilder report = DynamicReports.report();
			//create new report design
			report
				.setPageColumnsPerPage(2)
				   .setSubtotalStyle(boldStyle)
				   .columns(column1, column2)
				   .groupBy(columnGroup)
		    
		   //subtotals
		   .subtotalsAtTitle(createSubtotal("This is a title sum"))
		   .subtotalsAtPageHeader(createSubtotal("This is a page header sum"))
		   .subtotalsAtPageFooter(createSubtotal("This is a page footer sum"))
		   .subtotalsAtColumnHeader(createSubtotal("This is a column header sum"))
		   .subtotalsAtColumnFooter(createSubtotal("This is a column footer sum"))
		   .subtotalsAtLastPageFooter(createSubtotal("This is a last page footer sum"))
		   .subtotalsAtSummary(createSubtotal("This is a summary sum"))
		   .subtotalsAtGroupHeader(columnGroup, createSubtotal("This is a group header sum"))
		   .subtotalsAtGroupFooter(columnGroup, createSubtotal("This is a group footer sum"))
		   .subtotalsOfPercentageAtGroupHeader(columnGroup, createPercSubtotal("This is a group header perc."))
		   .subtotalsOfPercentageAtGroupFooter(columnGroup, createPercSubtotal("This is a group footer perc."))
		    
		   .setDataSource(createDataSource())//set datasource
		   .show();//create and show report
		} catch (DRException e) {
			e.printStackTrace();
		}
	}

	private AggregationSubtotalBuilder<Integer> createSubtotal(String label) {
		return DynamicReports.sbt.sum(column2).setLabel(label).setLabelStyle(boldStyle);
	}

	private PercentageSubtotalBuilder createPercSubtotal(String label) {
		return DynamicReports.sbt.percentage(column2).setLabel(label).setLabelStyle(boldStyle);
	}

	private JRDataSource createDataSource() {
		DRDataSource dataSource = new DRDataSource("column1", "column2");
		int row = 1;
		
		for (int i = 1; i <= 2; i++) {
			for (int j = 0; j < 50; j++) {
				dataSource.add("group" + i, row++);
			}       
		}
		return dataSource;
	}
	
	public static void main(String[] args) {
		new SubtotalReport();
	}
}