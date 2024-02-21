package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

import java.util.List;

public interface LabelService {

    Label createLabel(LabelDto labelDto);
    Label updateLabel(Long id, LabelDto labelDto);
    Label getLabel(Long id);
    List<Label> getAllLabels();
    void deleteLabel(Long id);
}
