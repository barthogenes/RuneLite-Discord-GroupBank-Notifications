package com.discordgroupbanknotifications;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ItemMapTest {

    @Test
    public void createItemMap() {
        Item[] items = Arrays.asList(
                new Item(1, "Iron Bar", 5),
                new Item(1, "Iron Bar", 20),
                new Item(2, "Bread", 50),
                new Item(2, "Bread", 100),
                new Item(0, "The One Ring", 1),
                new Item(-1, "Excalibur", 1),
                new Item(3, "Palantir", 0),
                null
        ).toArray(new Item[0]);
        Map<Integer, Item> expected = Maps.newHashMap(ImmutableMap.of(
                1, new Item(1, "Iron Bar", 25),
                2, new Item(2, "Bread", 150),
                0, new Item(0, "The One Ring", 1),
                3, new Item(3, "Palantir", 0)
        ));

        Map<Integer, Item> actual = new ItemMap(items).getItemMap();

        assertEquals(expected, actual);
    }

    @Test
    public void getItemTransferWithdrawAll() {
        ItemMap initial = new ItemMap(List.of(
                new Item(1, "Iron Bar", 5),
                new Item(2, "Iron Ore", 5)
        ).toArray(new Item[0]));
        ItemMap modified = new ItemMap(List.of(
                new Item(1, "Iron Bar", 0)
        ).toArray(new Item[0]));
        List<ItemTransfer> expected = new ArrayList<>(List.of(
                new ItemTransfer(1, "Iron Bar", -5),
                new ItemTransfer(2, "Iron Ore", -5)
        ));

        List<ItemTransfer> actual = initial.getItemTransfers(modified);

        assertEquals(expected, actual);
    }

    @Test
    public void getItemTransferWithdrawSome() {
        ItemMap initial = new ItemMap(List.of(
                new Item(1, "Iron Bar", 10)
        ).toArray(new Item[0]));
        ItemMap modified = new ItemMap(List.of(
                new Item(1, "Iron Bar", 5)
        ).toArray(new Item[0]));
        List<ItemTransfer> expected = new ArrayList<>(List.of(
                new ItemTransfer(1, "Iron Bar", -5))
        );

        List<ItemTransfer> actual = initial.getItemTransfers(modified);

        assertEquals(expected, actual);
    }

    @Test
    public void getItemTransferWithdrawNone() {
        ItemMap initial = new ItemMap(List.of(
                new Item(1, "Iron Bar", 10)
        ).toArray(new Item[0]));
        ItemMap modified = new ItemMap(List.of(
                new Item(1, "Iron Bar", 10)
        ).toArray(new Item[0]));
        List<ItemTransfer> expected = new ArrayList<>();

        List<ItemTransfer> actual = initial.getItemTransfers(modified);

        assertEquals(expected, actual);
    }

    @Test
    public void getItemTransferDepositAll() {
        ItemMap initial = new ItemMap(new Item[0]);
        ItemMap modified = new ItemMap(List.of(
                new Item(1, "Iron Bar", 10)
        ).toArray(new Item[0]));
        List<ItemTransfer> expected = new ArrayList<>(List.of(
                new ItemTransfer(1, "Iron Bar", 10))
        );

        List<ItemTransfer> actual = initial.getItemTransfers(modified);

        assertEquals(expected, actual);
    }

    @Test
    public void getItemTransferDepositSome() {
        ItemMap initial = new ItemMap(new Item[0]);
        ItemMap modified = new ItemMap(List.of(
                new Item(1, "Iron Bar", 5)
        ).toArray(new Item[0]));
        List<ItemTransfer> expected = new ArrayList<>(List.of(
                new ItemTransfer(1, "Iron Bar", 5))
        );

        List<ItemTransfer> actual = initial.getItemTransfers(modified);

        assertEquals(expected, actual);
    }

    @Test
    public void getItemTransferDepositNone() {
        ItemMap initial = new ItemMap(new Item[0]);
        ItemMap modified = new ItemMap(new Item[0]);
        List<ItemTransfer> expected = new ArrayList<>();

        List<ItemTransfer> actual = initial.getItemTransfers(modified);

        assertEquals(expected, actual);
    }

    @Test
    public void getItemTransfers() {
        ItemMap initial = new ItemMap(List.of(
                new Item(1, "Iron Bar", 5),
                new Item(2, "Bread", 50),
                new Item(3, "Logs", 12),
                new Item(4, "Coins", 1000)
        ).toArray(new Item[0]));
        ItemMap modified = new ItemMap(List.of(
                new Item(1, "Iron Bar", 0),
                new Item(2, "Bread", 60),
                new Item(3, "Logs", 15),
                new Item(4, "Coins", 500),
                new Item(5, "Iron Ore", 20)
        ).toArray(new Item[0]));
        List<ItemTransfer> expected = new ArrayList<>(Arrays.asList(
                new ItemTransfer(1, "Iron Bar", -5),
                new ItemTransfer(2, "Bread", 10),
                new ItemTransfer(3, "Logs", 3),
                new ItemTransfer(4, "Coins", -500),
                new ItemTransfer(5, "Iron Ore", 20)
        ));

        List<ItemTransfer> actual = initial.getItemTransfers(modified);

        assertEquals(expected, actual);
    }
}