package interfaces;

import entities.User;
import exceptions.DataAccessException;

public interface UserRepositoryInterface {
	User insertUser(User user) throws DataAccessException; //register
	boolean isUserNameExist(String userName) throws DataAccessException;
	User findByUserNameAndPassword(String userName, String password)  throws DataAccessException; //login
    User findByUserIdAndPassword(int userId, String password)  throws DataAccessException;
	int deleteUser(int userId,String userName,String password)  throws DataAccessException;
	int recoverAccount(User user)  throws DataAccessException;
	int deleteAccountPermanently(User user) throws DataAccessException;
	int updateUserPassword(String userName,String oldPassword,String newPassword)  throws DataAccessException;
}
