package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String LABEL_ID = "/{id}";

    private final LabelService labelService;
    private final LabelRepository labelRepository;

    @Operation(summary = "Get a list of all labels")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponse(responseCode = "200", description = "List of tasks",
            content = @Content(schema = @Schema(implementation = Label.class))
    )
    @GetMapping
    public List<Label> getLabels() {
        return labelRepository.findAll();
    }

    @Operation(summary = "Get a label by its id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the task",
                    content = @Content(schema = @Schema(implementation = Label.class))),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping(LABEL_ID)
    public Label getLabel(@PathVariable final Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label with id " + id + " not found"));
    }

    @Operation(summary = "Create a new label")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Label created",
                    content = @Content(schema = @Schema(implementation = Label.class))),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public Label createLabel(@RequestBody @Valid final LabelDto labelDto) {
        return labelService.createLabel(labelDto);
    }

    @Operation(summary = "Update the label")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label updated",
                    content = @Content(schema = @Schema(implementation = Label.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Label with that id not found"),
            @ApiResponse(responseCode = "422", description = "Incorrect input data")
    })
    @PutMapping(LABEL_ID)
    @PreAuthorize("hasAuthority('USER')")
    public Label updateUser(@PathVariable final Long id,
                            @RequestBody @Valid final LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @Operation(summary = "Delete the label")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label deleted"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Label with that id not found")
    })
    @DeleteMapping(LABEL_ID)
    @PreAuthorize("hasAuthority('USER')")
    public void deleteUser(@PathVariable final Long id) {
        labelRepository.deleteById(id);
    }
}
