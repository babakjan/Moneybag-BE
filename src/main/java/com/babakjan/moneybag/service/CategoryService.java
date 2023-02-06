package com.babakjan.moneybag.service;

import com.babakjan.moneybag.DTO.CategoryDTO;
import com.babakjan.moneybag.DTO.CreateCategoryDTO;
import com.babakjan.moneybag.entity.Category;
import com.babakjan.moneybag.exception.CategoryNotFoundException;
import com.babakjan.moneybag.exception.RecordNotFoundException;
import com.babakjan.moneybag.repository.CategoryRepository;
import com.babakjan.moneybag.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecordRepository recordRepository;

    //get all
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    //get by id
    public Category getById(Long id) throws CategoryNotFoundException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException(id);
        }
        return optionalCategory.get();
    }

    //create
    public Category save(CreateCategoryDTO createCategoryDTO) {
        System.out.println(createCategoryDTO.getName() + " " + createCategoryDTO.getIcon());
        return categoryRepository.save(createCategoryDTO.toCategory());
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
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) throws CategoryNotFoundException, RecordNotFoundException {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new CategoryNotFoundException(id);
        }

        if (null != categoryDTO.getName() && !"".equalsIgnoreCase(categoryDTO.getName())) {
            optionalCategory.get().setName(categoryDTO.getName());
        }
        if (null != categoryDTO.getIcon() && !"".equalsIgnoreCase(categoryDTO.getIcon())) {
            optionalCategory.get().setIcon(categoryDTO.getIcon());
        }
        if (null != categoryDTO.getRecordIds()) {
            optionalCategory.get().setRecords(recordRepository.findAllById(categoryDTO.getRecordIds()));
        }

        categoryRepository.save(optionalCategory.get());
        return categoryDTO;
    }
}
