package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.category.CreateCategoryRequest;
import com.babakjan.moneybag.dto.category.CategoryDto;
import com.babakjan.moneybag.entity.Category;
import com.babakjan.moneybag.error.exception.CategoryNotFoundException;
import com.babakjan.moneybag.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final AuthenticationService authenticationService;

    //get all
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    //get by id
    public Category getById(Long id) throws CategoryNotFoundException {
        if (id == null) {
            throw new CategoryNotFoundException("Category id can't be null.");
        }
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        return optionalCategory.get();
    }

    //create
    public Category save(CreateCategoryRequest request) {
        authenticationService.ifNotAdminThrowAccessDenied();
        return categoryRepository.save(new Category(request));
    }

    //save
    public Category save(Category category) {
        return  categoryRepository.save(category);
    }

    //delete
    public void delete(Long id) throws CategoryNotFoundException {
        authenticationService.ifNotAdminThrowAccessDenied();
        if (id == null) {
            throw new CategoryNotFoundException("Category id can't be null.");
        }
        categoryRepository.deleteById(id);
    }

    //update
    @Transactional
    public Category update(Long id, CategoryDto request) throws CategoryNotFoundException {
        authenticationService.ifNotAdminThrowAccessDenied();

        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException(id);
        }

        if (null != request.getName() && !"".equalsIgnoreCase(request.getName())) {
            optionalCategory.get().setName(request.getName());
        }
        if (null != request.getIcon() && !"".equalsIgnoreCase(request.getIcon())) {
            optionalCategory.get().setIcon(request.getIcon());
        }
        if (null != request.getColor() && !"".equalsIgnoreCase(request.getColor())) {
            optionalCategory.get().setColor(request.getColor());
        }

        return categoryRepository.save(optionalCategory.get());
    }

    public static List<CategoryDto> categoriesToDtos(List<Category> categories) {
        return categories.stream().map(Category::dto).toList();
    }
}
