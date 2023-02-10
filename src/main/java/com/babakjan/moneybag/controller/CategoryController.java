package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.category.CategoryDto;
import com.babakjan.moneybag.dto.category.CreateCategoryRequest;
import com.babakjan.moneybag.dto.category.UpdateCategoryRequest;
import com.babakjan.moneybag.exception.CategoryNotFoundException;
import com.babakjan.moneybag.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category of record.")
@SecurityRequirement(name = "bearer-key")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = {@Content( mediaType = "application/json", schema = @Schema()) })
})
public class CategoryController {

    private final CategoryService categoryService;

    //get all
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return all categories.", description = "Role ADMIN is required.")
    public List<CategoryDto> getAll() {
        return CategoryService.categoriesToDtos(categoryService.getAll());
    }

    //get by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return category by id.", description = "Role ADMIN is required.")
    public CategoryDto getById(@PathVariable Long id) throws CategoryNotFoundException {
        return categoryService.getById(id).dto();
    }

    //create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new category.", description = "Role ADMIN is required.")
    public CategoryDto create(@RequestBody @Valid CreateCategoryRequest request) {
        return categoryService.save(request).dto();
    }

    //delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete category by id.",
            description = "All records in this category will be also deleted! Role ADMIN is required."
    )
    public void delete(@PathVariable Long id) throws CategoryNotFoundException {
        categoryService.delete(id);
    }

    //update
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update category by id.",
            description = "Update existing category by id, null or not provided fields are ignored. Role ADMIN is required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated."),
            @ApiResponse(responseCode = "404", description = "Category or any of it's records not found.")
    })
    public CategoryDto update(@PathVariable Long id, @RequestBody @Valid UpdateCategoryRequest request)
            throws CategoryNotFoundException {
        return categoryService.update(id, request).dto();
    }
}
