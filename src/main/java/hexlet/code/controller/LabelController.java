package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    private static final String  ID = "/{id}";

    @Autowired
    private LabelService labelService;
    @Autowired
    private LabelRepository labelRepository;

    @GetMapping
    public List<Label> getLabels() {
        return labelRepository.findAll();
    }

    @GetMapping(ID)
    public Label getUser(@PathVariable final Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @PreAuthorize("hasAuthority('USER')")
    public Label createLabel(@RequestBody @Valid final LabelDto labelDto) {
        return labelService.createLabel(labelDto);
    }

    @PutMapping(ID)
    @PreAuthorize("hasAuthority('USER')")
    public Label updateUser(@PathVariable final Long id,
                           @RequestBody @Valid final LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @DeleteMapping(ID)
    @PreAuthorize("hasAuthority('USER')")
    public String deleteUser(@PathVariable final Long id) {
        labelRepository.deleteById(id);
        return "Label with id " + id + " deleted";
    }
}
