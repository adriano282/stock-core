package stock.core.impl.reports.cewolf;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import stock.core.impl.dao.DAOReports;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.links.CategoryItemLinkGenerator;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;

public class CountProductByStorageLevel implements DatasetProducer, CategoryToolTipGenerator, CategoryItemLinkGenerator, Serializable {

	@Override
	public String generateLink(Object dataset, int series, Object category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateToolTip(CategoryDataset data, int series, int item) {
		DAOReports dao = new DAOReports();
		DefaultPieDataset dataset = dao.getCountProductByStorageLevel();
		return (String) dataset.getKeys().get(series);
	}

	@Override
	public Object produceDataset(Map params) throws DatasetProduceException {
		DAOReports dao = new DAOReports();
		return dao.getCountProductByStorageLevel();
	}

	@Override
	public boolean hasExpired(Map params, Date since) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getProducerId() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
