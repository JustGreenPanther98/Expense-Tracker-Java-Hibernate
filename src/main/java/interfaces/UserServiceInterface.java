package interfaces;

import entities.User;
import exceptions.ServiceException;

public interface UserServiceInterface {
	User createAccount(User user) throws ServiceException;  //register
	User login(String userName, String password)  throws ServiceException; //login
    User loginById(int userId, String password) throws ServiceException ;
	boolean deleteAccount(int userId,String userName,String password) throws ServiceException;
	boolean recoverAccount(User user)  throws ServiceException;
	boolean deleteAccountPermanently(User user) throws ServiceException;
	boolean changePassword(String userName,String oldPassword,String newPassword) throws ServiceException;
	boolean isUserNameExist(String userName)  throws ServiceException;

}
