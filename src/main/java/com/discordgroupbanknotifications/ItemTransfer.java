package com.discordgroupbanknotifications;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public final class ItemTransfer {
    @Getter
    private final int itemId;
    @Getter
    private final String itemName;
    @Getter @Setter
    private int quantityChange;

    public ItemTransfer(int itemId, String itemName, int quantityChange) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantityChange = quantityChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemTransfer that = (ItemTransfer) o;
        return itemId == that.itemId && quantityChange == that.quantityChange && Objects.equals(itemName, that.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, itemName, quantityChange);
    }
}
