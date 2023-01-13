package rso.itemscompare.entityeditor.services.beans;

import rso.itemscompare.entityeditor.lib.Item;
import rso.itemscompare.entityeditor.models.converters.ItemConverter;
import rso.itemscompare.entityeditor.models.entities.ItemEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.*;

@RequestScoped
public class ItemBean {
    @Inject
    private EntityManager em;

    @Inject
    private ItemPriceBean itemPriceBean;

    public Item getItemById(int itemId) {
        ItemEntity entity = em.find(ItemEntity.class, itemId);
        if (entity == null) {
            throw new NotFoundException();
        }

        em.refresh(entity);
        return ItemConverter.toDto(entity);
    }

    public Item getItemByBarcode(String barcode) {
        TypedQuery<ItemEntity> query = em.createNamedQuery("ItemEntity.getByBarcode", ItemEntity.class)
                .setParameter("barcode", barcode);
        List<ItemEntity> resultList = query.getResultList();
        if (resultList.size() == 1) {
            ItemEntity entity = resultList.get(0);
            em.refresh(entity);
            return ItemConverter.toDto(entity);
        }

        throw new NotFoundException();
    }

    public int addNewItem(String barcode, String name, int storeId, double price) {
        Query query = em.createNativeQuery("insert into item(barcode, item_name) values(?barcode, ?name)");
        query.setParameter("barcode", barcode);
        query.setParameter("name", name);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        int queryResult = query.executeUpdate();
        int savePriceResult = 0;
        if (queryResult == 1) {
            try {
                Item newItem = getItemByBarcode(barcode);
                savePriceResult = itemPriceBean.addNewItemPrice(newItem.getId(), storeId, price);
            } catch (NotFoundException ignored) {
            }
        }
        tx.commit();

        return savePriceResult;
    }

    public int editItemName(int itemId, String newName) {
        Query query = em.createNativeQuery("update item set item_name = ?newName where id = ?itemId")
                .setParameter("newName", newName)
                .setParameter("itemId", itemId);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        int queryResult = query.executeUpdate();
        tx.commit();

        return queryResult;
    }

    public List<ItemEntity> getItems(String nameFilter) {
        List<ItemEntity> l = em.createNamedQuery("ItemEntity.getNameLike", ItemEntity.class)
                .setParameter("nameFilter", nameFilter)
                .getResultList();

        for (ItemEntity e : l) {
            em.refresh(e);
        }

        return l;
    }

    public List<ItemEntity> getItems(String nameFilter, int storeId) {
        List<ItemEntity> l = em.createNamedQuery("ItemEntity.getNameLikeIfHasStorePrice", ItemEntity.class)
                .setParameter("nameFilter", nameFilter)
                .setParameter("storeId", storeId)
                .getResultList();

        for (ItemEntity e : l) {
            em.refresh(e);
        }

        return l;
    }
}
