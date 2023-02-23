package com.discordgroupbanknotifications;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("discordgroupbanknotifications")
public interface DiscordGroupBankNotificationsConfig extends Config
{
	// Webhook config section
	@ConfigSection(
			name = "Webhook Settings",
			description = "The config for webhook content notifications",
			position = 0,
			closedByDefault = false
	)
	String webhookConfig = "webhookConfig";

	@ConfigItem(
			keyName = "webhook",
			name = "Webhook URL(s)",
			description = "The Discord Webhook URL(s) to send messages to, separated by a newline.",
			section = webhookConfig,
			position = 0
	)
	String webhook();
	// End webhook config section

	// Notifications config section
	@ConfigSection(
			name = "Notifications",
			description = "The config for the deposit and retrieve notifications",
			position = 1,
			closedByDefault = false
	)
	String notificationsConfig = "notificationsConfig";

	@ConfigItem(
			keyName = "includeDeposit",
			name = "Send Deposit Notifications",
			description = "Send messages when you deposit items to the Group Bank to discord.",
			section = notificationsConfig
	)
	default boolean sendDeposit() { return true; }

	@ConfigItem(
			keyName = "depositMessage",
			name = "Deposit Message",
			description = "Message to send to Discord on a deposit.",
			section = notificationsConfig,
			position = 1
	)
	default String depositMessage() { return "$name has deposited $amount of $item to the Group Bank."; }

	@ConfigItem(
			keyName = "includeRetrieve",
			name = "Send Retrieve Notifications",
			description = "Send messages when you retrieve items to the Group Bank to discord.",
			section = notificationsConfig,
			position = 2
	)
	default boolean sendRetrieve() { return true; }

	@ConfigItem(
			keyName = "retrieveMessage",
			name = "Retrieve Message",
			description = "Message to send to Discord on a retrieve.",
			section = notificationsConfig,
			position = 3
	)
	default String retrieveMessage() { return "$name has retrieved $amount of $item from the Group Bank."; }
	// End notifications config section
}
