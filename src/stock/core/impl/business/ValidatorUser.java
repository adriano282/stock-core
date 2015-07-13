package stock.core.impl.business;

import domain.DomainEntity;
import domain.User;
import stock.core.application.Result;
import stock.core.impl.dao.DAOUser;

public class ValidatorUser extends AbstractStrategyValidate {

	@Override
	public Result validate(DomainEntity domainEntity) {
		
		Result result = new Result();
		User user = (User) domainEntity;
		
		result.setMessage(validateNullFields(user));
		String message = result.getMessage();

		if (message != null)
			return result;
				
		if (!validateEmail(user.getUsername())) {
			
			result.setMessage("O username precisa ser um email válido.");
			return result;
		}
		
		if (user.getHashCodePassword().byteValue() < 48 || user.getHashCodePassword().byteValue() > 64) {
			
			result.setMessage("A senha precisa conter entre 6 e 8 digitos.");
			return result;
		}
		
		DAOUser dao = new DAOUser();
		User user2 = dao.getUserByUsername(user.getUsername());
		
		if (user2 != null && user2.getId() != user.getId()) {
			
			result.setMessage("O usuário de username " + user.getUsername() + " já se encontra cadastrado.");
			return result;			
		}
		
		if (user.getRoot() && !dao.getUserRoot().getUsername().equals(user.getUsername())) {
			
			User userRoot = dao.getUserRoot();
			userRoot.setRoot(false);
			dao.update(userRoot);
			
		} else if (!user.getRoot() && dao.getUserRoot().getUsername().equals(user.getUsername())) {
			
			result.setMessage("O sistema precisa ter um usuário ROOT. Edite o usuário que deseja tornar ROOT." );
						
		}
		
		return result;
	}

}
