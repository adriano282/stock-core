package stock.core.impl.business;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import domain.DomainEntity;
import stock.core.IStrategy;
import stock.core.application.Result;

public abstract class AbstractStrategyValidate implements IStrategy {
	
	public Result process(DomainEntity domain) {
		return validate(domain);
		
	}	
	
	public abstract Result validate(DomainEntity domainEntity);
	
	protected boolean validateEmail(final String email) {
		
		Pattern pattern;
		Matcher matcher;
				
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9\\-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{1,})$";				
		pattern = Pattern.compile(EMAIL_PATTERN);
		
		matcher = pattern.matcher(email);
		return matcher.matches();
		
	}
	
	protected String validateNullFields(DomainEntity obj) {
		String message = null;
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			DomainEntity object = obj.getClass().newInstance();
			
			for (Field f : fields) {
				String nameMetodo = "get"+ f.getName().substring(0,1).toUpperCase().concat(f.getName().substring(1));
				
				Method[] metodos = object.getClass().getDeclaredMethods();
				
				for (Method m : metodos) {
					if (m.getName().equals(nameMetodo)) {
						if (m.invoke(obj) == null || m.invoke(obj).equals("")) {
							//if (message == null) {
								message = object.getClass().getSimpleName() + "." + f.getName() + ".empty.error.message";
								return message.toLowerCase();
							//} else {
								//message = message + "<p>O campo " + f.getName() + " esta nullo.</p>";
							//}
						}
					}
				}
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return message;
	}



}
