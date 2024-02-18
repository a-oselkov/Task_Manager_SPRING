package hexlet.code.controller.impl;

import hexlet.code.controller.LabelController;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
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

import static hexlet.code.controller.impl.LabelControllerImpl.LABEL_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelControllerImpl implements LabelController {

    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String LABEL_ID = "/{id}";
    private final LabelService labelService;
    private final LabelRepository labelRepository;

    @GetMapping
    public List<Label> getLabels() {
        return labelRepository.findAll();
    }

    @GetMapping(LABEL_ID)
    public Label getLabel(@PathVariable final Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label with id " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public Label createLabel(@RequestBody @Valid final LabelDto labelDto) {
        return labelService.createLabel(labelDto);
    }

    @PutMapping(LABEL_ID)
    @PreAuthorize("hasAuthority('USER')")
    public Label updateUser(@PathVariable final Long id,
                            @RequestBody @Valid final LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @DeleteMapping(LABEL_ID)
    @PreAuthorize("hasAuthority('USER')")
    public void deleteUser(@PathVariable final Long id) {
        labelRepository.deleteById(id);
    }
}
