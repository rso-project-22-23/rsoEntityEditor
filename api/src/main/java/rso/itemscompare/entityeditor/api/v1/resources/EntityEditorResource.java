package rso.itemscompare.entityeditor.api.v1.resources;

import rso.itemscompare.entityeditor.lib.Item;
import rso.itemscompare.entityeditor.models.converters.ItemConverter;
import rso.itemscompare.entityeditor.models.converters.ItemPriceConverter;
import rso.itemscompare.entityeditor.models.entities.ItemEntity;
import rso.itemscompare.entityeditor.models.entities.ItemPriceEntity;
import rso.itemscompare.entityeditor.models.entities.StoreEntity;
import rso.itemscompare.entityeditor.services.beans.ItemBean;
import rso.itemscompare.entityeditor.services.beans.ItemPriceBean;
import rso.itemscompare.entityeditor.services.beans.StoreBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@ApplicationScoped
@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EntityEditorResource {
    @Inject
    private ItemBean itemBean;

    @Inject
    private StoreBean storeBean;

    @Inject
    private ItemPriceBean itemPriceBean;

    @POST
    @Path("/add-store")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStore(@HeaderParam("storeName") String storeName) {
        // check if store with this name already exists
        try {
            storeBean.getStoreByName(storeName);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse("Store with this name already exists"))
                    .build();
        } catch (NotFoundException ignored) {
        }

        // add new store to DB and check if it is successfully saved
        int addStoreResult = storeBean.addNewStore(storeName);
        String errorMessage = null;
        if (addStoreResult > 1) {
            errorMessage = "More than one row (" + addStoreResult + ") affected when creating new store";
        } else if (addStoreResult < 1) {
            errorMessage = "Failed to save new store";
        }
        if (errorMessage != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(errorMessage)).build();
        }

        return Response.status(Response.Status.CREATED).entity(true).build();
    }

    @POST
    @Path("/add-item")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItem(@HeaderParam("barcode") String barcode, @HeaderParam("name") String itemName,
                            @HeaderParam("store") String storeName, @HeaderParam("price") double price) {
        // check if item with this barcode already exists
        try {
            itemBean.getItemByBarcode(barcode);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse("Item with this barcode already exists"))
                    .build();
        } catch (NotFoundException ignored) {
        }

        // get store id for specified store name or return error response if store with such name does not exist
        int storeId;
        try {
            storeId = storeBean.getStoreByName(storeName).getId();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse("Specified store does not exist"))
                    .build();
        }

        // add new item to DB and check if it is successfully saved
        int addItemResult = itemBean.addNewItem(barcode, itemName, storeId, price);
        String errorMessage = null;
        if (addItemResult > 1) {
            errorMessage = "More than one row (" + addItemResult + ") affected when creating new item";
        } else if (addItemResult < 1) {
            errorMessage = "Failed to save new item";
        }
        if (errorMessage != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(errorMessage)).build();
        }

        return Response.status(Response.Status.CREATED).entity(true).build();
    }

    @POST
    @Path("/edit-item-name")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editItemName(@HeaderParam("barcode") String barcode, @HeaderParam("newName") String newName) {
        Item item;
        try {
            item = itemBean.getItemByBarcode(barcode);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse("Item with this barcode does not exist"))
                    .build();
        }
        if (newName.equals(item.getItemName())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse("No changes made in the requested new name"))
                    .build();
        }

        int updateNameResult = itemBean.editItemName(item.getId(), newName);
        String errorMessage = null;
        if (updateNameResult > 1) {
            errorMessage = "More than one row (" + updateNameResult + ") affected when updating item name";
        } else if (updateNameResult < 1) {
            errorMessage = "Failed to update item name";
        }
        if (errorMessage != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(errorMessage)).build();
        }

        return Response.status(Response.Status.CREATED).entity(true).build();
    }

    @POST
    @Path("/add-item-price")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addItemPrice(@HeaderParam("barcode") String barcode, @HeaderParam("storeName") String storeName,
                                 @HeaderParam("price") double price) {
        int itemId;
        try {
            itemId = itemBean.getItemByBarcode(barcode).getId();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse("Item with this barcode does not exist"))
                    .build();
        }

        // get store id for specified store name or return error response if store with such name does not exist
        int storeId;
        try {
            storeId = storeBean.getStoreByName(storeName).getId();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse("Specified store does not exist"))
                    .build();
        }

        boolean newStore = false;
        try {
            itemPriceBean.getItemPrice(itemId, storeId);
        } catch (NotFoundException e) {
            newStore = true;
        }

        int addPriceResult = newStore ? itemPriceBean.addNewItemPriceTransaction(itemId, storeId, price)
                : itemPriceBean.editItemPrice(itemId, storeId, price);

        String errorMessage = null;
        if (addPriceResult > 1) {
            errorMessage = "More than one row (" + addPriceResult + ") affected when adding new item price";
        } else if (addPriceResult < 1) {
            errorMessage = "Failed to add new item price";
        }
        if (errorMessage != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(buildErrorResponse(errorMessage)).build();
        }

        return Response.status(Response.Status.CREATED).entity(true).build();
    }

    @GET
    @Path("/get-items")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems(@QueryParam("storeName") String storeName, @QueryParam("nameContains") String nameContains,
                             @QueryParam("storePriceOnly") boolean storePriceOnly) {
        if (storeName != null && storeName.isEmpty() || nameContains != null && nameContains.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse("Empty store name or name filter specified"))
                    .build();
        }

        // convert to empty filter (all names) if filter was not specified
        if (nameContains == null) {
            nameContains = "";
        }

        // get store id for specified store name or return error response if store with such name does not exist
        int storeId = -1;
        if (storeName != null) {
            try {
                storeId = storeBean.getStoreByName(storeName).getId();
            } catch (NotFoundException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(buildErrorResponse("Specified store does not exist"))
                        .build();
            }
        }

        // HashMap that will be returned in JSON format as final result
        HashMap<Integer, HashMap<String, Object>> resultMap = new HashMap<>();

        List<ItemEntity> itemEntities = storeId == -1 ?
                itemBean.getItems(nameContains) : itemBean.getItems(nameContains, storeId);

        if (itemEntities.isEmpty()) {
            return Response.status(Response.Status.OK).entity(new HashMap<>()).build();
        }

        ArrayList<Integer> itemIds = new ArrayList<>();
        for (ItemEntity e : itemEntities) {
            itemIds.add(e.getId());
            HashMap<String, Object> idHashMap = new HashMap<>() {{
                put("Item", e);
                put("Price", new ArrayList<Map<String, Object>>());
            }};
            resultMap.put(e.getId(), idHashMap);
        }

        List<ItemPriceEntity> itemPrices = storePriceOnly && storeId != -1 ?
                itemPriceBean.getPricesForItemsFromStore(itemIds, storeId) : itemPriceBean.getPricesForItems(itemIds);

        HashMap<Integer, StoreEntity> storeMap = storeBean.getStoresMap("");

        for (ItemPriceEntity e : itemPrices) {
            ((ArrayList<Map<String, Object>>) resultMap.get(e.getItemId()).get("Price"))
                    .add(Map.ofEntries(
                            entry("itemId", e.getItemId()),
                            entry("storeId", e.getStoreId()),
                            entry("storeName", storeMap.get(e.getStoreId()).getStoreName()),
                            entry("price", e.getPrice())
                    ));
        }

        return Response.status(Response.Status.OK).entity(resultMap).build();
    }

    @GET
    @Path("/get-stores")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStores(@QueryParam("nameContains") String nameContains) {
        if (nameContains != null && nameContains.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(buildErrorResponse("Empty store name filter specified"))
                    .build();
        }

        if (nameContains == null) {
            nameContains = "";
        }

        return Response.status(Response.Status.OK).entity(storeBean.getStores(nameContains)).build();
    }

    private JsonObject buildErrorResponse(String message) {
        return Json.createObjectBuilder().add("Error", message).build();
    }
}
