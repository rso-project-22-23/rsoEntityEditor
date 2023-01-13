package rso.itemscompare.entityeditor.models.entities;


import javax.persistence.*;

@Entity
@Table(name = "item")
@NamedQueries(value = {
        @NamedQuery(name = "ItemEntity.getByBarcode",
        query = "SELECT ie FROM ItemEntity ie WHERE ie.barcode = :barcode"),
        @NamedQuery(name = "ItemEntity.getNameLike",
        query = "SELECT ie FROM ItemEntity ie WHERE ie.itemName LIKE CONCAT('%',:nameFilter,'%')"),
        @NamedQuery(name = "ItemEntity.getNameLikeIfHasStorePrice",
                query = """
                    SELECT ie FROM ItemEntity ie, ItemPriceEntity ipe
                    WHERE ie.id = ipe.itemId
                    AND ie.itemName LIKE CONCAT('%',:nameFilter,'%')
                    AND ipe.storeId = :storeId
                    """)
})
public class ItemEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "barcode", nullable = false)
    private String barcode;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
