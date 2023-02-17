package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.category.CreateCategoryRequest;
import com.babakjan.moneybag.dto.category.CategoryDto;
import com.babakjan.moneybag.entity.ErrorMessage;
import com.babakjan.moneybag.error.exception.CategoryNotFoundException;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/categories", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category of record.")
@SecurityRequirement(name = "bearer-key")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized. Authentication is required.",
                content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
        )
})
public class CategoryController { //TODO add security

    private final CategoryService categoryService;

    //get all
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return all categories.")
    public List<CategoryDto> getAll() {
        return CategoryService.categoriesToDtos(categoryService.getAll());
    }

    //get by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return category by id.")
    public CategoryDto getById(@PathVariable Long id) throws CategoryNotFoundException {
        return categoryService.getById(id).dto();
    }

    //create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured({"ADMIN"})
    @Operation(summary = "Create new category.", description = "Role ADMIN is required.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. Role ADMIN is required.",
                    content = @Content
            ),
    })
    public CategoryDto create(@RequestBody @Valid CreateCategoryRequest request) {
        return categoryService.save(request).dto();
    }

    //delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ADMIN"})
    @Operation(
            summary = "Delete category by id.",
            description = "All records in this category will be also deleted! Role ADMIN is required."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. Role ADMIN is required.",
                    content = @Content
            ),
    })
    public void delete(@PathVariable Long id) throws CategoryNotFoundException {
        categoryService.delete(id);
    }

    //update
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ADMIN"})
    @Operation(
            summary = "Update category by id.",
            description = "Update existing category by id, null or not provided fields are ignored. Role ADMIN is required."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. Role ADMIN is required.",
                    content = @Content
            ),
    })
    public CategoryDto update(@PathVariable Long id, @RequestBody @Valid CategoryDto request)
            throws CategoryNotFoundException {
        return categoryService.update(id, request).dto();
    }
}
