package hexlet.code.controller.impl;

import hexlet.code.controller.LabelController;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static hexlet.code.controller.impl.LabelControllerImpl.LABEL_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelControllerImpl implements LabelController {

    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String LABEL_ID = "/{id}";
    private final LabelService labelService;
    private final LabelRepository labelRepository;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Label>> getLabels() {
        return ResponseEntity.status(OK).body(labelService.getAllLabels());
    }

    @GetMapping(value = LABEL_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Label> getLabel(@PathVariable final Long id) {
        return ResponseEntity.status(OK).body(labelService.getLabel(id));
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Label> createLabel(@RequestBody @Valid final LabelDto labelDto) {
        return ResponseEntity.status(CREATED).body(labelService.createLabel(labelDto));
    }

    @PutMapping(value = LABEL_ID, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Label> updateUser(@PathVariable final Long id,
                            @RequestBody @Valid final LabelDto labelDto) {
        return ResponseEntity.status(OK).body(labelService.updateLabel(id, labelDto));
    }

    @DeleteMapping(LABEL_ID)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> deleteUser(@PathVariable final Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.status(OK).build();
    }
}
