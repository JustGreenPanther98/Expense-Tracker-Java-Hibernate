package service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import entities.Category;
import entities.User;
import exceptions.ServiceException;
import interfaces.CategoryServiceInterface;
import repository.CategoryRepository;

public class CategoryService implements CategoryServiceInterface {

	SessionFactory sessionFactory;

	public CategoryService(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Category> getGeneralCategories() throws ServiceException {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CategoryRepository repo = new CategoryRepository(session);
			List<Category> genCategories = repo.getGeneralCategories();
			tx.commit();
			return genCategories;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException("Error in service layer while fetching the categories", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public List<Category> getAllCategories(User user) throws ServiceException {
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CategoryRepository repo = new CategoryRepository(session);
			List<Category> allCategories = repo.getAllCategoriesOfUser(user);
			tx.commit();
			return allCategories;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException("Error in service layer while fetching the categories", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public Category getCategoryById(int Category_id, User user) throws ServiceException {
		if (user == null) {
			return null;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CategoryRepository repo = new CategoryRepository(session);
			Category genCategories = repo.getCategoryById(Category_id, user);
			tx.commit();
			return genCategories;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			throw new ServiceException("Error in service layer while fetching the categories", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public Category createCategory(Category category) throws ServiceException {
		if (category == null) {
			return null;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CategoryRepository repo = new CategoryRepository(session);
			Category category1 = repo.createCategory(category);
			tx.commit();
			return category1;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException(
					"Error in serive layer while creating the category, please cross check the details in category", e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public boolean deleteCategory(int Category_id, User user) throws ServiceException {
		if (user == null || Category_id == 0) {
			return false;
		}
		Session session = null;
		Transaction tx = null;
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			CategoryRepository repo = new CategoryRepository(session);
			int deleted = repo.deleteCategoryById(Category_id, user);
			tx.commit();
			return deleted != 0;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new ServiceException(
					"Error in service layer while deleting the category, please cross check the details in category",
					e);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

}
