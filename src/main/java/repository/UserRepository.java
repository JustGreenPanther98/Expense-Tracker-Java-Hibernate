package repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import entities.User;
import exceptions.DataAccessException;
import interfaces.UserRepositoryInterface;

public class UserRepository implements UserRepositoryInterface {
	Session session;

	public UserRepository(Session session) {
		this.session = session;
	}

	// Register
	public User insertUser(User user) throws DataAccessException {
		try {
			session.save(user);
			return user;
		} catch (Exception e) {
			throw new DataAccessException("Failed to persist User entity", e);
		}
	}

	// login
	public User findByUserNameAndPassword(String userName, String password) throws DataAccessException {
		try {
			Query<User> query = session.createQuery(
					"from User u where u.User_Name = :username AND u.User_Password = :password AND u.Status='active'",
					User.class);
			query.setParameter("username", userName);
			query.setParameter("password", password);
			User user = query.uniqueResult();
			return user;
		} catch (Exception e) {
			throw new DataAccessException("Failed to fetch user for login!");
		}
	}

	public User findByUserIdAndPassword(int userId, String password) throws DataAccessException {
		try {
			Query<User> query = session.createQuery(
					"from User u where u.User_Id = :userid AND u.User_Password = :password AND u.Status='active'",
					User.class);
			query.setParameter("userid", userId);
			query.setParameter("password", password);
			User user = query.uniqueResult();
			return user;
		} catch (Exception e) {
			throw new DataAccessException("Failed to fetch user by User id!");
		}
	}

	public int deleteUser(int userId, String userName, String password) throws DataAccessException {
		try {
			Query query = session.createQuery(
					"update User u set u.Status='deleted' where u.User_Id = :userid AND u.User_Name = :username AND u.User_Password = :password");
			query.setParameter("userid", userId);
			query.setParameter("password", password);
			query.setParameter("username", userName);
			int res = query.executeUpdate();
			return res;
		} catch (Exception e) {
			throw new DataAccessException("Failed to delete user");
		}
	}

	public int updateUserPassword(String userName, String oldPassword, String newPassword) throws DataAccessException {
		try {
			Query query = session.createQuery(
					"update User u set u.User_Password = :new where u.User_Name = :username AND u.User_Password = :old AND u.Status = 'active'");
			query.setParameter("new", newPassword);
			query.setParameter("old", oldPassword);
			query.setParameter("username", userName);
			int res = query.executeUpdate();
			return res;
		} catch (Exception e) {
			throw new DataAccessException("Failed to update user password");
		}
	}

	@Override
	public boolean isUserNameExist(String userName) throws DataAccessException {
		try {
			Query<User> query = session.createQuery("from User u where u.User_Name = :username",
					User.class);
			query.setParameter("username", userName);
			User user = query.uniqueResult();
			return user != null;
		} catch (Exception e) {
			throw new DataAccessException("Failed to fetch user for login!");
		}
	}

	@Override
	public int recoverAccount(User user) throws DataAccessException {
		try {
			Query query = session.createQuery(
					"Update User u set u.Status='active' where u.User_Id = :uid AND u.User_Name = :un AND u.User_Password = :ps AND u.Status = 'deleted'");
			query.setParameter("uid", user.getUser_Id());
			query.setParameter("un", user.getUser_Name());
			query.setParameter("ps", user.getPassword());
			int res = query.executeUpdate();
			return res;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			throw new DataAccessException("Failed to fetch user for login!");
		}
	}

	@Override
	public int deleteAccountPermanently(User user) throws DataAccessException {
		try {
			Query  query = session.createQuery(
					"delete from User u where u.User_Id = :uid AND u.User_Name = :un AND u.User_Password = :ps AND u.Status = 'active'");
			query.setParameter("uid", user.getUser_Id());
			query.setParameter("un", user.getUser_Name());
			query.setParameter("ps", user.getPassword());
			int res = query.executeUpdate();
			return res;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			System.out.println(e.getCause());
			throw new DataAccessException("Failed to fetch user for login!");
		}
	}
}
