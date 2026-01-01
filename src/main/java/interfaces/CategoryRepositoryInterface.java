package interfaces;

import java.util.List;
import entities.Category;
import entities.User;
import exceptions.DataAccessException;

public interface CategoryRepositoryInterface {
	List<Category> getGeneralCategories() throws DataAccessException;
	List<Category> getAllCategoriesOfUser(User user) throws DataAccessException;
	Category createCategory(Category category) throws DataAccessException;
	Category getCategoryById(int Category_id, User user) throws DataAccessException;
	int deleteCategoryById(int Category_id,User user) throws DataAccessException;
}
