package hexlet.code.service.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LabelImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public Label createLabel(final LabelDto labelDto) {
        final Label label = new Label();
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(final Long id, final LabelDto labelDto) {
        final Label label = labelRepository.findById(id).get();
        label.setName(labelDto.getName());
        return labelRepository.save(label);
    }
}
