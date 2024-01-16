package org.example.backender101homebanking.mapper;

public interface EntityMapper<D, E> {
    E convertToEntity(D dto);
    D convertToDTO(E entity);
    void updateUserFromDTO(E entity, D dto);
}
