package hexlet.code.mapper;

import org.mapstruct.MappingTarget;

public interface Mappable<E, D> {

    E toEntity(D dto);
    void updateEntity(@MappingTarget E entity, D dto);
}
