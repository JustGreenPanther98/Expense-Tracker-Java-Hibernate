package interfaces;

import java.util.List;

import entities.Category;
import entities.User;
import exceptions.ServiceException;

public interface CategoryServiceInterface {
	List<Category> getGeneralCategories() throws ServiceException;
	List<Category> getAllCategories(User user) throws ServiceException;
	Category getCategoryById(int Category_id,User user) throws ServiceException;
	Category createCategory(Category category) throws ServiceException;
	boolean deleteCategory(int Category_id,User user) throws ServiceException;
}
