package stock.core.impl.reports.dynamicreports;

import net.sf.dynamicreports.report.builder.datatype.DoubleType;

public class PriceType extends DoubleType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getPattern() {
		return "R$ #,###.00";
	}

}
