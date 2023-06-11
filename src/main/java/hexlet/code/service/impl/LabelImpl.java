package hexlet.code.service.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LabelImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public Label createLabel(final LabelDto labelDto) {
        final Label label = fromDto(labelDto);
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(final Long id, final LabelDto labelDto) {
        final Label label = fromDto(labelDto);
        label.setId(id);
        return labelRepository.save(label);
    }
    private Label fromDto(LabelDto labelDto) {
        return Label.builder()
                .name(labelDto.getName())
                .build();
    }
}
