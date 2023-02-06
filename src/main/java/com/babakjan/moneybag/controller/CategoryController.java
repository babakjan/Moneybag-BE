package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.DTO.CategoryDTO;
import com.babakjan.moneybag.DTO.CreateCategoryDTO;
import com.babakjan.moneybag.entity.Category;
import com.babakjan.moneybag.exception.CategoryNotFoundException;
import com.babakjan.moneybag.exception.RecordNotFoundException;
import com.babakjan.moneybag.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //get all
    @GetMapping
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    //get by id
    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) throws CategoryNotFoundException {
        return categoryService.getById(id);
    }

    //create
    @PostMapping
    public Category create(@RequestBody CreateCategoryDTO createCategoryDTO) {
        return categoryService.save(createCategoryDTO);
    }

    //update
    @PutMapping("/{id}")
    public CategoryDTO update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO)
            throws RecordNotFoundException, CategoryNotFoundException {
        return categoryService.update(id, categoryDTO);
    }

    //delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws CategoryNotFoundException {
        categoryService.delete(id);
    }
}
