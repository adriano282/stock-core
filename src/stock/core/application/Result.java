package stock.core.application;

import java.util.List;

import domain.DomainEntity;

public class Result extends ApplicationEntity {	
	
	public static final String
		INFO = "INFO",
		WARNING = "WARNING",
		DANGER = "DANGER",
		SUCCESS = "SUCCESS";
		
	private String message;
	private String typeMessage;
	private List<DomainEntity> entities;
	private int countEntities;
	private DomainEntity entityFiltered;
	
	
	public String getTypeMessage() {
		return typeMessage;
	}
	public void setTypeMessage(String typeMessage) {
		this.typeMessage = typeMessage;
	}
	
	public List<DomainEntity> getEntities() {
		return entities;
	}
	public void setEntities(List<DomainEntity> entities) {
		this.entities = entities;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public DomainEntity getEntityFiltered() {
		return entityFiltered;
	}
	public void setEntityFiltered(DomainEntity entityFilter) {
		this.entityFiltered = entityFilter;
	}
	public int getCountEntities() {
		return countEntities;
	}
	public void setCountEntities(int countEntities) {
		this.countEntities = countEntities;
	}

}
