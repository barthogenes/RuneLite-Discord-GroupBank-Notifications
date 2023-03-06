package com.discordgroupbanknotifications;

import java.util.ArrayList;
import java.util.List;

public class TransferMessageCreator {
    public DiscordWebhook createTransferMessages(List<ItemTransfer> itemTransfers, String playerName) {
        if (itemTransfers.size() < 1)
            return null;

        List<String> content = new ArrayList<>();
        itemTransfers.forEach(i -> {
            content.add(
                createDescription(i.getItemName(), i.getQuantityChange(), playerName)
            );
        });

        DiscordWebhook discordWebhookData = new DiscordWebhook();
        discordWebhookData.setContent(String.join("\n", content));
        return discordWebhookData;
    }

    private String createDescription(String itemName, int quantityChange, String playerName)
    {
        String msg;
        String quantity = quantityFormat(Math.abs(quantityChange));
        String itemNameSingle = itemName.endsWith("s") ? itemName.substring(0, itemName.length() - 1) : itemName;
        String itemNameMultiple = !itemName.endsWith("s") ? itemName + "s" : itemName;
        String itemNameSingleOrMultiple = quantity.equals("1") ? itemNameSingle : itemNameMultiple;
        if (quantityChange > 0) {
            msg = String.format("**%s** deposited **%s** %s to the Group Bank.", playerName, quantity, itemNameSingleOrMultiple);
        } else {
            msg = String.format("**%s** withdrew **%s** %s from the Group Bank.", playerName, quantity, itemNameSingleOrMultiple);
        }
        return msg;
    }

    private String quantityFormat(int quantity) {
        if (quantity > 9999999) {
            return String.format("%.0fM", Math.floor(quantity / 1000000.0));
        }
        if (quantity > 99999) {
            return String.format("%.0fK", Math.floor(quantity / 1000.0));
        }
        return quantity + "";
    }
}
