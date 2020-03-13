package org.softuni.productshop.service;

import org.softuni.productshop.domain.models.service.CategoryServiceModel;

import java.util.List;

public interface CategoryService {

    void addCategory(CategoryServiceModel categoryServiceModel);

    List<CategoryServiceModel> allCategories();

    CategoryServiceModel viewCategory(String id);

    void editCategory(String id, CategoryServiceModel categoryServiceModel);

    void deleteCategory(String id);
}
