package rso.itemscompare.entityeditor.models.converters;

import rso.itemscompare.entityeditor.lib.Item;
import rso.itemscompare.entityeditor.models.entities.ItemEntity;

public class ItemConverter {
    public static Item toDto(ItemEntity entity) {
        Item dto = new Item();
        dto.setId(entity.getId());
        dto.setBarcode(entity.getBarcode());
        dto.setItemName(entity.getItemName());

        return dto;
    }

    public static ItemEntity toEntity(Item dto) {
        ItemEntity entity = new ItemEntity();
        entity.setId(dto.getId());
        entity.setBarcode(dto.getBarcode());
        entity.setItemName(dto.getItemName());

        return entity;
    }
}
