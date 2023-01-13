package rso.itemscompare.entityeditor.models.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(PriceKey.class)
@Table(name = "item_price")
@NamedQueries(value = {
        @NamedQuery(name = "ItemPriceEntity.getPrice",
        query = "SELECT ipe FROM  ItemPriceEntity ipe WHERE ipe.itemId = :itemId AND ipe.storeId = :storeId"),
        @NamedQuery(name = "ItemPriceEntity.getPriceForIds",
                query = "SELECT ipe FROM ItemPriceEntity ipe WHERE ipe.itemId IN :itemIds"),
        @NamedQuery(name = "ItemPriceEntity.getPriceForIdsWhereStore",
                query = "SELECT ipe FROM ItemPriceEntity ipe WHERE ipe.itemId IN :itemIds AND ipe.storeId = :storeId")
})
public class ItemPriceEntity {
    @Id
    @Column(name = "item_id", nullable = false)
    private int itemId;

    @Id
    @Column(name = "store_id", nullable = false)
    private int storeId;

    @Column(name = "price", nullable = false)
    private double price;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class PriceKey implements Serializable {
    private int itemId;
    private int storeId;
}
