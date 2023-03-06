package com.discordgroupbanknotifications;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class TransferMessageCreatorTest {
    private Object[] parametersForSingleItemTransfer() {
        return new Object[]{
            new Object[]{
                "Wise Old Man",
                new ItemTransfer(1, "Abyssal whip", 1),
                "**Wise Old Man** deposited **1** Abyssal whip to the Group Bank."
            },
            new Object[]{
                "Wise Old Man",
                new ItemTransfer(1, "Abyssal whip", 99999),
                "**Wise Old Man** deposited **99999** Abyssal whips to the Group Bank."
            },
            new Object[]{
                "Wise Old Man",
                new ItemTransfer(1, "Abyssal whip", 100000),
                "**Wise Old Man** deposited **100K** Abyssal whips to the Group Bank."
            },
            new Object[]{
                "Wise Old Man",
                new ItemTransfer(1, "Abyssal whip", -1),
                "**Wise Old Man** withdrew **1** Abyssal whip from the Group Bank."
            },
            new Object[]{
                "Wise Old Man",
                new ItemTransfer(1, "Coins", -1),
                "**Wise Old Man** withdrew **1** Coin from the Group Bank."
            },
            new Object[]{
                "Wise Old Man",
                new ItemTransfer(1, "Abyssal whip", -9999999),
                "**Wise Old Man** withdrew **9999K** Abyssal whips from the Group Bank."
            },
            new Object[]{
                "Wise Old Man",
                new ItemTransfer(1, "Abyssal whip", -10000000),
                "**Wise Old Man** withdrew **10M** Abyssal whips from the Group Bank."
            },
            new Object[]{
                "Wise Old Man",
                new ItemTransfer(1, "Abyssal whip", -5),
                "**Wise Old Man** withdrew **5** Abyssal whips from the Group Bank."
            }
        };
    }

    @Test
    @Parameters
    @SuppressWarnings("JUnitMalformedDeclaration")
    public void singleItemTransfer(String playerName, ItemTransfer itemTransfer, String description) {
        TransferMessageCreator transferMessageCreator = new TransferMessageCreator();

        DiscordWebhook actual = transferMessageCreator.createTransferMessages(Collections.singletonList(
                itemTransfer
        ), playerName);

        assertEquals(description, actual.getContent());
    }
}