package rso.itemscompare.entityeditor.services.beans;

import rso.itemscompare.entityeditor.lib.Store;
import rso.itemscompare.entityeditor.models.converters.StoreConverter;
import rso.itemscompare.entityeditor.models.entities.ItemEntity;
import rso.itemscompare.entityeditor.models.entities.StoreEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.List;

@RequestScoped
public class StoreBean {
    @Inject
    private EntityManager em;

    public Store getStoreById(int storeId) {
        StoreEntity entity = em.find(StoreEntity.class, storeId);
        if (entity == null) {
            throw new NotFoundException();
        }

        em.refresh(entity);
        return StoreConverter.toDto(entity);
    }

    public Store getStoreByName(String name) {
        TypedQuery<StoreEntity> query = em.createNamedQuery("StoreEntity.getByName", StoreEntity.class)
                .setParameter("storeName", name);
        List<StoreEntity> resultList = query.getResultList();
        if (resultList.size() == 1) {
            StoreEntity entity = resultList.get(0);
            em.refresh(entity);
            return StoreConverter.toDto(entity);
        }

        throw new NotFoundException();
    }

    public List<StoreEntity> getStores(String nameFilter) {
        List<StoreEntity> l = em.createNamedQuery("StoreEntity.getNameLike", StoreEntity.class)
                .setParameter("nameFilter", nameFilter)
                .getResultList();

        for (StoreEntity e : l) {
            em.refresh(e);
        }

        return l;
    }

    public HashMap<Integer, StoreEntity> getStoresMap(String nameFilter) {
        List<StoreEntity> l = getStores(nameFilter);

        HashMap<Integer, StoreEntity> stores = new HashMap<>();
        for (StoreEntity e : l) {
            stores.put(e.getId(), e);
        }

        return stores;
    }

    public int addNewStore(String storeName) {
        Query query = em.createNativeQuery("insert into store(store_name) values(?storeName)");
        query.setParameter("storeName", storeName);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        int queryResult = query.executeUpdate();
        tx.commit();

        return queryResult;
    }
}
