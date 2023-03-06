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
	default String webhook() { return ""; }
	// End webhook config section
}
