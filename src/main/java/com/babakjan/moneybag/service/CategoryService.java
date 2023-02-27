package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.category.CategoryAnalyticDto;
import com.babakjan.moneybag.dto.category.CreateCategoryRequest;
import com.babakjan.moneybag.dto.category.CategoryDto;
import com.babakjan.moneybag.entity.Category;
import com.babakjan.moneybag.entity.CategoryAnalytic;
import com.babakjan.moneybag.error.exception.CategoryNotFoundException;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final AuthenticationService authenticationService;

    /**
     * Get all Categories.
     * @return list of categories
     */
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    /**
     * Get category by id.
     * @param id category id
     * @return category of specified id
     * @throws CategoryNotFoundException Category of specified id doesn't exist.
     */
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

    /**
     * Create new category. Role ADMIN is required.
     * @param request category data
     * @return created category
     */
    public Category save(CreateCategoryRequest request) {
        authenticationService.ifNotAdminThrowAccessDenied();
        return categoryRepository.save(new Category(request));
    }

    /**
     * Saves category.
     * @return saved category
     */
    public Category save(Category category) {
        return  categoryRepository.save(category);
    }

    /**
     * Delete category by id. All records in this category will be also deleted! Role ADMIN is required.
     * @param id category id
     * @throws CategoryNotFoundException Category of specified id doesn't exist.
     */
    public void delete(Long id) throws CategoryNotFoundException {
        authenticationService.ifNotAdminThrowAccessDenied();
        if (id == null) {
            throw new CategoryNotFoundException("Category id can't be null.");
        }
        if (categoryRepository.findById(id).isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }

    /**
     * Update category by id.
     * @param id category id
     * @param request category data (only fields, which will be changed)
     * @return updated category
     * @throws CategoryNotFoundException Category of specified id doesn't exist.
     */
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

    /**
     * Get analytic of categories. Role ADMIN can access analytic of all users, role USER only of their accounts.
     * @param userId user id (analytic of records of this user)
     * @param dateGe date greater or equal than (inclusive)
     * @param dateLt date lower than (exclusive)
     * @return list of category analytics
     * @throws UserNotFoundException Authenticated user doesn't exist.
     */
    public List<CategoryAnalytic> getCategoriesAnalytic(Long userId, Date dateGe, Date dateLt)
            throws UserNotFoundException {
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(userId);
        return categoryRepository.findCategoriesAnalytic(userId, dateGe, dateLt);
    }

    /**
     * Map list of categories to list of data transfer objects.
     * @param categories list of categories
     * @return list of category dtos
     */
    public static List<CategoryDto> categoriesToDtos(List<Category> categories) {
        return categories.stream().map(Category::dto).toList();
    }

    /**
     * Map list of category analytic to list of data transfer objects
     * @param categoryAnalytics list of category analytic
     * @return list of category analytic dto
     */
    public static List<CategoryAnalyticDto> categoriesAnalyticToDtos(List<CategoryAnalytic> categoryAnalytics) {
        return categoryAnalytics.stream().map(CategoryAnalytic::dto).toList();
    }
}
