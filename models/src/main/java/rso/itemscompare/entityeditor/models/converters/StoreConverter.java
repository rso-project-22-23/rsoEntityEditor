package rso.itemscompare.entityeditor.models.converters;

import rso.itemscompare.entityeditor.lib.Store;
import rso.itemscompare.entityeditor.models.entities.StoreEntity;

public class StoreConverter {
    public static Store toDto(StoreEntity entity) {
        Store dto = new Store();
        dto.setId(entity.getId());
        dto.setStoreName(entity.getStoreName());

        return dto;
    }

    public static StoreEntity toEntity(Store dto) {
        StoreEntity entity = new StoreEntity();
        entity.setId(dto.getId());
        entity.setStoreName(dto.getStoreName());

        return entity;
    }
}
