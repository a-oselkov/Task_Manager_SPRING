package hexlet.code.service.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.mapper.LabelMapper;
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
    private final LabelMapper mapper;

    @Override
    public Label createLabel(final LabelDto labelDto) {
        final Label label = mapper.toLabel(labelDto);
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(final Long id, final LabelDto labelDto) {
        final Label label = labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label with id = " + id + " not found"));
        mapper.updateLabel(label, labelDto);
        return label;
    }
}
