package hexlet.code.service.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class LabelImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public Label createLabel(final LabelDto labelDto) {
        final Label label = fromDto(labelDto);
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(final Long id, final LabelDto labelDto) {
        final Label label = labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label with id = " + id + " not found"));
        merge(label, labelDto);
        return label;
    }
    private Label fromDto(LabelDto labelDto) {
        return Label.builder()
                .name(labelDto.getName())
                .build();
    }
    private void merge(final Label label, final LabelDto labelDto) {
        final Label newLabel = fromDto(labelDto);
        label.setName(newLabel.getName());
    }
}
