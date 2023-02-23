package com.discordgroupbanknotifications;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DiscordGroupBankNotificationsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DiscordGroupBankNotificationsPlugin.class);
		RuneLite.main(args);
	}
}