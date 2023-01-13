package rso.itemscompare.entityeditor.models.converters;

import rso.itemscompare.entityeditor.lib.ItemPrice;
import rso.itemscompare.entityeditor.models.entities.ItemPriceEntity;

public class ItemPriceConverter {
    public static ItemPrice toDto(ItemPriceEntity entity) {
        ItemPrice dto = new ItemPrice();
        dto.setItemId(entity.getItemId());
        dto.setStoreId(entity.getStoreId());
        dto.setPrice(entity.getPrice());

        return dto;
    }

    public static ItemPriceEntity toEntity(ItemPrice dto) {
        ItemPriceEntity entity = new ItemPriceEntity();
        entity.setItemId(dto.getItemId());
        entity.setStoreId(dto.getStoreId());
        entity.setPrice(dto.getPrice());

        return entity;
    }
}
