package rafnews.backend.services;
import javax.inject.Inject;

import rafnews.backend.model.Category;
import rafnews.backend.repositories.category.ICategoryRepository;

import java.util.List;

public class CategoryService {

    public CategoryService() {
        System.out.println(this);
    }

    @Inject
    private ICategoryRepository categoryRepository;

    public Category addCategory(Category category) {
        return this.categoryRepository.addCategory(category);
    }

    public List<Category> allCategory(int page, int perPage) {
        return this.categoryRepository.allCategory(page, perPage);
    }

    public Category findCategory(String name) {
        return this.categoryRepository.findCategory(name);
    }

    public void deleteCategory(String name) {
        this.categoryRepository.deleteCategory(name);
    }


    public Category updateCategory(Category category, String name) {
        return this.categoryRepository.updateCategory(category, name);
    }
}
