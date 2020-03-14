package org.softuni.productshop.service;

import org.modelmapper.ModelMapper;
import org.softuni.productshop.domain.entities.Category;
import org.softuni.productshop.domain.models.service.CategoryServiceModel;
import org.softuni.productshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addCategory(CategoryServiceModel categoryServiceModel) {
        Category category = this.categoryRepository.findByName(categoryServiceModel.getName());
        if (category != null) {
            throw new EntityExistsException();
        }

        category = this.modelMapper.map(categoryServiceModel, Category.class);
        this.categoryRepository.save(category);
    }

    @Override
    public List<CategoryServiceModel> allCategories() {
        return this.categoryRepository.findAll()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryServiceModel viewCategory(String id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return  this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public void editCategory(String id, CategoryServiceModel categoryServiceModel) {
        Category category = this.categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        category.setName(categoryServiceModel.getName());
        this.categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) {
        Category category = this.categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        this.categoryRepository.delete(category);
    }

    @Override
    public List<CategoryServiceModel> findAllCategories() {
        return this.categoryRepository.findAll()
                .stream()
                .map(c -> this.modelMapper.map(c, CategoryServiceModel.class))
                .collect(Collectors.toList());
    }
}
