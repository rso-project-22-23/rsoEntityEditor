package rso.itemscompare.entityeditor.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "store")
@NamedQueries(value = {
        @NamedQuery(name = "StoreEntity.getByName",
                query = "SELECT se FROM StoreEntity se WHERE se.storeName = :storeName"),
        @NamedQuery(name = "StoreEntity.getNameLike",
                query = "SELECT se FROM StoreEntity se WHERE se.storeName LIKE CONCAT('%',:nameFilter,'%')")
})
public class StoreEntity {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
