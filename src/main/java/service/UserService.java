package service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entities.User;
import exceptions.DataAccessException;
import exceptions.ServiceException;
import interfaces.UserServiceInterface;
import repository.UserRepository;

public class UserService implements UserServiceInterface {
	private SessionFactory sessionFactory;

	public UserService(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public User createAccount(User user) throws ServiceException {
		if (user.getPassword() == null || user.getPassword().trim().length() < 4)
			return null;
		Session session = null;
		Transaction create = null;
		try {
			session = sessionFactory.openSession();
			create = session.beginTransaction();
			UserRepository userToDatabase = new UserRepository(session);
			User newUser = userToDatabase.insertUser(user);
			create.commit();
			return newUser;
		} catch (Exception e) {
			if (create != null && create.isActive()) {
				create.rollback();
			}
			throw new ServiceException("Error in serive layer to create account", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public User login(String userName, String password) throws ServiceException {
		Session session = null;
		Transaction tx = null;
		try {
			if (userName == null)
				return null;
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			UserRepository repo = new UserRepository(session);
			User loggedIn = repo.findByUserNameAndPassword(userName, password);
			tx.commit();
			return loggedIn;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException("Error in serive layer while login", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public User loginById(int userId, String password) throws ServiceException {
		Session session = null;
		Transaction tx = null;
		try {
			if (userId == 0)
				return null;
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			UserRepository repo = new UserRepository(session);
			User loggedIn = repo.findByUserIdAndPassword(userId, password);
			tx.commit();
			return loggedIn;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException("Error in service layer while login", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public boolean deleteAccount(int userId, String userName, String password) throws ServiceException {
		Session session = null;
		Transaction tx = null;
		try {
			if (userId == 0)
				return false;
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			UserRepository repo = new UserRepository(session);
			int n = repo.deleteUser(userId, userName, password);
			tx.commit();
			return n != 0;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException("Error in service layer while deleting the account", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public boolean changePassword(String userName, String oldPassword, String newPassword) throws ServiceException {
		Session session = null;
		Transaction tx = null;
		try {
			if (userName == null || newPassword == null || newPassword.trim().length() < 4)
				return false;
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			UserRepository repo = new UserRepository(session);
			int n = repo.updateUserPassword(userName, oldPassword, newPassword);
			tx.commit();
			return n != 0;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException("Error in service layer while changing password", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public boolean isUserNameExist(String userName) throws ServiceException {
		Session session = null;
		Transaction tx = null;
		try {
			if (userName == "")
				return false;
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			UserRepository repo = new UserRepository(session);
			boolean exist = repo.isUserNameExist(userName);
			tx.commit();
			return exist;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException("Error in service layer while login", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public boolean recoverAccount(User user) throws ServiceException {
		Session session = null;
		Transaction tx = null;
		try {
			if (user == null)
				return false;
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			UserRepository repo = new UserRepository(session);
			int res = repo.recoverAccount(user);
			tx.commit();
			return res != 0;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException("Error in service layer while recovering accounts", e);
		} finally {
			if (session != null)
				session.close();
		}
	}

	@Override
	public boolean deleteAccountPermanently(User user) throws ServiceException {
		Session session = null;
		Transaction tx = null;
		try {
			if (user == null)
				return false;
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			UserRepository repo = new UserRepository(session);
			int res = repo.deleteAccountPermanently(user);
			tx.commit();
			return res != 0;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException("Error in service layer while deleting account permanently", e);
		} finally {
			if (session != null)
				session.close();
		}
	}
}
