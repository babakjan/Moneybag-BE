package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.record.CreateRecordRequest;
import com.babakjan.moneybag.dto.record.RecordDto;
import com.babakjan.moneybag.entity.ErrorMessage;
import com.babakjan.moneybag.entity.Record;
import com.babakjan.moneybag.error.exception.AccountNotFoundException;
import com.babakjan.moneybag.error.exception.CategoryNotFoundException;
import com.babakjan.moneybag.error.exception.RecordNotFoundException;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/records", produces = "application/json")
@Tag(name = "Record", description = "Record of financial transaction")
@SecurityRequirement(name = "bearer-key")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden. Role ADMIN is required.",
                content = @Content
        ),
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
public class RecordController {

    private final RecordService recordService;

    //get all
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return all records.", description = "Role ADMIN is required.")
    public List<RecordDto> getAll() {
        return RecordService.recordsToDto(recordService.getAll());
    }

    //get by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return record by id.", description = "Role ADMIN is required.")
    public RecordDto getById(@PathVariable Long id) throws RecordNotFoundException {
        return recordService.getById(id).dto();
    }


    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<RecordDto> getAllFilter(@And({
            @Spec( path = "label", params = "label", spec = Like.class)
    }) Specification<Record> specification, Sort sort, @RequestParam(required = false) Long userId)
            throws UserNotFoundException {
        return RecordService.recordsToDto(recordService.getAllFilter(specification, sort, userId));
    }

    //create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new record.", description = "Role ADMIN is required.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Category or account not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    public RecordDto create(@RequestBody @Valid CreateRecordRequest request)
            throws CategoryNotFoundException, AccountNotFoundException, UserNotFoundException {
        return recordService.save(request).dto();
    }

    //delete by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete record by id.", description = "Role ADMIN is required.")
    public void deleteById(@PathVariable Long id) throws RecordNotFoundException {
        recordService.deleteById(id);
    }

    //update
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update record by id.",
            description = "Update existing record by id, null or not provided fields are ignored. Role ADMIN is required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated."),
            @ApiResponse(
                responseCode = "404",
                description = "Record, category or account not found.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    public RecordDto update(@PathVariable Long id, @RequestBody @Valid RecordDto request)
            throws RecordNotFoundException, CategoryNotFoundException, AccountNotFoundException, UserNotFoundException {
        return recordService.update(id, request).dto();
    }
}
