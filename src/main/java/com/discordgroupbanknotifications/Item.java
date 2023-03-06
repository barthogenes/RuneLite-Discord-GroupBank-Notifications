package com.discordgroupbanknotifications;

import lombok.Value;

import java.util.Objects;

@Value
public class Item
{
    int id;
    String name;
    int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && quantity == item.quantity && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity);
    }
}
