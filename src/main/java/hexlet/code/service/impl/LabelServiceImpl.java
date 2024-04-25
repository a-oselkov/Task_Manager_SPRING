package hexlet.code.service.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.exception.LabelNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper mapper;

    @Override
    public Label createLabel(final LabelDto labelDto) {
        final Label label = mapper.toEntity(labelDto);
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(final Long id, final LabelDto labelDto) {
        final Label label = labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label with id = " + id + " not found"));
        mapper.updateEntity(label, labelDto);
        return label;
    }

    @Override
    public Label getLabel(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new LabelNotFoundException("Label not found"));
    }

    @Override
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Override
    public void deleteLabel(Long id) {
        Label label = getLabel(id);
        labelRepository.delete(label);
    }
}
