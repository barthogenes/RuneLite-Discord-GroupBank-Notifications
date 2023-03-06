package com.discordgroupbanknotifications;

import lombok.Getter;

import java.util.*;

public class ItemMap {

    @Getter
    private final Map<Integer, Item> itemMap;

    public ItemMap(Item[] items) {
        this.itemMap = createItemMap(items);
    }

    public Map<Integer, Item> createItemMap(Item[] items) {
        Map<Integer, Item> itemMap = new HashMap<>();
        for (Item item : items) {
            if (item == null || item.getId() < 0 || item.getQuantity() < 0)
                continue;

            final int id = item.getId();
            if (itemMap.containsKey(id)) {
                itemMap.put(id, new Item(id, item.getName(), item.getQuantity() + itemMap.get(id).getQuantity()));
            } else {
                itemMap.put(id, new Item(id, item.getName(), item.getQuantity()));
            }
        }
        return itemMap;
    }

    public List<ItemTransfer> getItemTransfers(ItemMap other){
        Map<Integer, Item> thisItems = getItemMap();
        Map<Integer, Item> otherItems = other.getItemMap();

        List<ItemTransfer> result = new ArrayList<>();

        for (Integer itemId : otherItems.keySet()) {
            Item otherItem = otherItems.get(itemId);
            if (thisItems.containsKey(itemId)) {
                Item thisItem = thisItems.get(itemId);
                int quantityDifference = otherItem.getQuantity() - thisItem.getQuantity();
                if (quantityDifference != 0) {
                    result.add(new ItemTransfer(itemId, thisItem.getName(), quantityDifference));
                }
            } else {
                result.add(new ItemTransfer(itemId, otherItem.getName(), otherItem.getQuantity()));
            }
        }

        for (Integer itemId : thisItems.keySet()) {
            Item thisItem = thisItems.get(itemId);
            if (!otherItems.containsKey(itemId)) {
                result.add(new ItemTransfer(itemId, thisItem.getName(), -thisItem.getQuantity()));
            }
        }

        return result;
    }
}
