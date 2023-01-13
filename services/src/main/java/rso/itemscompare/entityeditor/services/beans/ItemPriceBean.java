package rso.itemscompare.entityeditor.services.beans;

import rso.itemscompare.entityeditor.lib.ItemPrice;
import rso.itemscompare.entityeditor.models.converters.ItemPriceConverter;
import rso.itemscompare.entityeditor.models.entities.ItemEntity;
import rso.itemscompare.entityeditor.models.entities.ItemPriceEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;
import java.util.List;

@RequestScoped
public class ItemPriceBean {
    @Inject
    EntityManager em;

    public ItemPrice getItemPrice(int itemId, int storeId) {
        List<ItemPriceEntity> l = em.createNamedQuery("ItemPriceEntity.getPrice", ItemPriceEntity.class)
                .setParameter("itemId", itemId)
                .setParameter("storeId", storeId)
                .getResultList();
        if (l.size() != 1) {
            throw new NotFoundException();
        }
        ItemPriceEntity e = l.get(0);
        em.refresh(e);
        return ItemPriceConverter.toDto(e);
    }

    public int addNewItemPrice(int itemId, int storeId, double price) {
        Query query = em.createNativeQuery("insert into item_price (item_id, store_id, price) values(?itemId, ?storeId, ?price)");
        query.setParameter("itemId", itemId);
        query.setParameter("storeId", storeId);
        query.setParameter("price", price);

        return query.executeUpdate();
    }

    public int addNewItemPriceTransaction(int itemId, int storeId, double price) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        int queryResult = addNewItemPrice(itemId, storeId, price);
        tx.commit();

        return queryResult;
    }

    public int editItemPrice(int itemId, int storeId, double newPrice) {
        Query query = em.createNativeQuery("update item_price set price = ?newPrice where item_id = ?itemId and store_id = ?storeId")
                .setParameter("newPrice", newPrice)
                .setParameter("itemId", itemId)
                .setParameter("storeId", storeId);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        int queryResult = query.executeUpdate();
        tx.commit();

        return queryResult;
    }

    public List<ItemPriceEntity> getPricesForItems(List<Integer> itemIds) {
        List<ItemPriceEntity> l = em.createNamedQuery("ItemPriceEntity.getPriceForIds", ItemPriceEntity.class)
                .setParameter("itemIds", itemIds)
                .getResultList();

        for (ItemPriceEntity e : l) {
            em.refresh(e);
        }

        return l;
    }

    public List<ItemPriceEntity> getPricesForItemsFromStore(List<Integer> itemIds, int storeId) {
        List<ItemPriceEntity> l = em.createNamedQuery("ItemPriceEntity.getPriceForIdsWhereStore", ItemPriceEntity.class)
                .setParameter("itemIds", itemIds)
                .setParameter("storeId", storeId)
                .getResultList();

        for (ItemPriceEntity e : l) {
            em.refresh(e);
        }

        return l;
    }
}
