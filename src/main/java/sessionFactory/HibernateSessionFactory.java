package sessionFactory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entities.Category;
import entities.Expense;
import entities.User;

public class HibernateSessionFactory{
	private static SessionFactory sessionFactory = null;
	private HibernateSessionFactory() {
		
	}
	public synchronized static SessionFactory getSessionFactory() {
		if(sessionFactory==null) {
			 Configuration configuration = new Configuration();
			 configuration.configure();
			 configuration.addAnnotatedClass(Category.class);
			 configuration.addAnnotatedClass(Expense.class);
			 configuration.addAnnotatedClass(User.class);
			 sessionFactory = configuration.buildSessionFactory();
		}
		return sessionFactory;
	}
}