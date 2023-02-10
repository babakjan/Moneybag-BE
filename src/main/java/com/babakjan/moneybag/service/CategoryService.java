package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.category.CategoryDto;
import com.babakjan.moneybag.dto.category.CreateCategoryRequest;
import com.babakjan.moneybag.entity.Category;
import com.babakjan.moneybag.entity.Record;
import com.babakjan.moneybag.exception.CategoryNotFoundException;
import com.babakjan.moneybag.repository.CategoryRepository;
import com.babakjan.moneybag.repository.RecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final RecordRepository recordRepository;

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
        return categoryRepository.save(new Category(request));
    }

    //save
    public Category save(Category category) {
        return  categoryRepository.save(category);
    }

    //delete
    public void delete(Long id) throws CategoryNotFoundException {
        if (id == null) {
            throw new CategoryNotFoundException("Category id can't be null.");
        }
        categoryRepository.deleteById(id);
    }

    //update
    @Transactional
    public Category update(Long id, CategoryDto categoryDto) throws CategoryNotFoundException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException(id);
        }

        if (null != categoryDto.getName() && !"".equalsIgnoreCase(categoryDto.getName())) {
            optionalCategory.get().setName(categoryDto.getName());
        }
        if (null != categoryDto.getIcon() && !"".equalsIgnoreCase(categoryDto.getIcon())) {
            optionalCategory.get().setIcon(categoryDto.getIcon());
        }
        if (null != categoryDto.getRecordIds()) {
            List<Record> records = recordRepository.findAllById(categoryDto.getRecordIds());

            for (Record record : records) {
                record.setCategory(optionalCategory.get());
                optionalCategory.get().addRecord(record);
            }
        }

        return categoryRepository.save(optionalCategory.get());
    }

    public static List<CategoryDto> categoriesToDtos(List<Category> categories) {
        return categories
                .stream().map(Category::dto)
                .collect(Collectors.toList());
    }
}
