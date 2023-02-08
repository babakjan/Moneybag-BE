package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.category.CategoryDto;
import com.babakjan.moneybag.dto.category.CreateCategoryRequest;
import com.babakjan.moneybag.entity.Category;
import com.babakjan.moneybag.exception.CategoryNotFoundException;
import com.babakjan.moneybag.exception.RecordNotFoundException;
import com.babakjan.moneybag.repository.CategoryRepository;
import com.babakjan.moneybag.repository.RecordRepository;
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
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll()
                .stream().map(CategoryDto::new)
                .collect(Collectors.toList());
    }

    //get by id
    public CategoryDto getById(Long id) throws CategoryNotFoundException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        return new CategoryDto(optionalCategory.get());
    }

    //create
    public CategoryDto save(CreateCategoryRequest request) {
        return new CategoryDto(categoryRepository.save(new Category(request)));
    }

    //delete
    public void delete(Long id) throws CategoryNotFoundException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        categoryRepository.deleteById(id);
    }

    //update
    public CategoryDto update(Long id, CategoryDto categoryDto) throws CategoryNotFoundException, RecordNotFoundException {
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
            optionalCategory.get().setRecords(recordRepository.findAllById(categoryDto.getRecordIds()));
        }

        categoryRepository.save(optionalCategory.get());
        return categoryDto;
    }
}
