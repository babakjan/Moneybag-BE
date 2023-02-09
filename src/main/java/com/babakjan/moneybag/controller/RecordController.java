package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.record.CreateRecordRequest;
import com.babakjan.moneybag.dto.record.RecordDto;
import com.babakjan.moneybag.exception.AccountNotFoundException;
import com.babakjan.moneybag.exception.CategoryNotFoundException;
import com.babakjan.moneybag.exception.RecordNotFoundException;
import com.babakjan.moneybag.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/records")
@Tag(name = "Record", description = "Record of financial transaction")
@SecurityRequirement(name = "bearer-key")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = {@Content( mediaType = "application/json", schema = @Schema()) })
})
public class RecordController {

    private final RecordService recordService;

    //get all
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return all records.")
    public List<RecordDto> getAll() {
        return RecordService.recordsToDto(recordService.getAll());
    }

    //get by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return record by id.")
    public RecordDto getById(@PathVariable Long id) throws RecordNotFoundException {
        return recordService.getById(id).dto();
    }

    //create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new record.")
    public RecordDto create(@RequestBody CreateRecordRequest request) throws CategoryNotFoundException, AccountNotFoundException {
        return recordService.save(request).dto();
    }

    //delete by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete record by id.")
    public void deleteById(@PathVariable Long id) throws RecordNotFoundException {
        recordService.deleteById(id);
    }

    //update
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update record by id.",
            description = "Update existing record by id, null or not provided fields are ignored."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated."),
            @ApiResponse(responseCode = "404", description = "Category or account not found.")
    })
    public RecordDto update(@PathVariable Long id, @RequestBody RecordDto request)
            throws RecordNotFoundException, CategoryNotFoundException, AccountNotFoundException {
        return recordService.update(id, request).dto();
    }
}
