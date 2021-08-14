package rafnews.backend.repositories.category;

import java.util.List;

import rafnews.backend.model.Category;


public interface ICategoryRepository {
	 public Category addCategory(Category category);
	 public List<Category> allCategory(int page, int perPage);
	 public Category findCategory(String name);
	 public void deleteCategory(String name);
	 public Category updateCategory(Category category, String name);
}
