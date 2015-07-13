package stock.core.impl.reports.condicional;

import java.math.BigDecimal;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.definition.ReportParameters;

public class LowerStockConditionExpression extends AbstractSimpleExpression<Boolean> {
	
	private static final long serialVersionUID = 1L;
	
	public Boolean evaluate(ReportParameters reportParameters) {
	
		BigDecimal quantityMinimum = reportParameters.getValue("quantityMinimum");
		BigDecimal quantity = reportParameters.getValue("quantity");
	
		return quantity.compareTo(quantityMinimum) < 0;
	}

}
