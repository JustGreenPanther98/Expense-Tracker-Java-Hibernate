package repository;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import entities.Category;
import entities.User;
import exceptions.DataAccessException;
import interfaces.CategoryRepositoryInterface;

public class CategoryRepository implements CategoryRepositoryInterface {
	Session session;

	public CategoryRepository(Session session) {
		this.session = session;
	}

	@Override
	public List<Category> getGeneralCategories() throws DataAccessException {
		try {
			Query<Category> generalCategories = session.createQuery("from Category c where c.user is NULL order by Category_Id",
					Category.class);
			List<Category> category = generalCategories.list();
			return category;
		} catch (Exception e) {
			throw new DataAccessException("Unable to fetch general categories!", e);
		}
	}

	@Override
	public List<Category> getAllCategoriesOfUser(User user) throws DataAccessException {
		try {
        Query<Category> query = session.createQuery("from Category c where c.user.User_Id= :user order by Category_Id", Category.class);
			query.setParameter("user", user.getUser_Id());
			List<Category> categories = query.list();
			return categories;

		} catch (Exception e) {
			throw new DataAccessException("Error while fetching categories of user", e);
		}
	}

	@Override
	public Category createCategory(Category category) throws DataAccessException {
		try {
			session.save(category);
			return category;
		} catch (Exception e) {
			throw new DataAccessException("Error in repository layer while creating the category",e);
		}
	}

	@Override
	public int deleteCategoryById(int categoryId, User user) throws DataAccessException {
		try {
			Query delete = session
					.createQuery("delete from Category c where c.Category_Id = :cid AND c.user = :user ");
			delete.setParameter("cid", categoryId);
			delete.setParameter("user", user);
			int res = delete.executeUpdate();
			return res;
		} catch (Exception e) {
			throw new DataAccessException("Error in deleting a category", e);
		}
	}

	@Override
	public Category getCategoryById(int categoryId, User user) throws DataAccessException {
		try {
			Query<Category> getcategory = session
					.createQuery("from Category c where c.Category_Id = :cid AND (c.user.User_Id = :uid OR c.user.User_Id is NULL)", Category.class);
			getcategory.setParameter("cid", categoryId);
			getcategory.setParameter("uid", user.getUser_Id());
			Category category = getcategory.uniqueResult();
			return category;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			throw new DataAccessException("Error in fetching the category", e);
		}
	}

}
