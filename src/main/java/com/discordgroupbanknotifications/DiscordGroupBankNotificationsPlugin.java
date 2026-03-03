package net.runelite.client.plugins.discordgroupbanknotifications;

import com.google.common.base.Strings;
import com.google.inject.Provides;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.*;
import java.util.stream.Collectors;

import static net.runelite.http.api.RuneLiteAPI.GSON;

@Slf4j
@PluginDescriptor(
	name = "Discord Group Bank Notifications"
)
public class DiscordGroupBankNotificationsPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private DiscordGroupBankNotificationsConfig config;
	@Inject
	private ItemManager itemManager;
	@Inject
	private TransferMessageCreator transferMessageCreator;
	@Inject
	private ApiTool apiTool;

	private static final int OPEN_SHARED_STORAGE = 24772680;
	private static final int SAVE_SHARED_STORAGE = 47448091;
	private static final int BACK_TO_BANK_SHARED_STORAGE = 47448073;
	private static final int CLOSE_SHARED_STORAGE = 47448067;

	private ItemMap initialSharedBankItems = null;
	private ItemMap modifiedSharedBankItems = null;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Discord Group Bank Notifications started!");
		super.startUp();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Discord Group Bank Notifications stopped!");
		initialSharedBankItems = null;
		modifiedSharedBankItems = null;
		super.shutDown();
	}

	@Subscribe
	private void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.GROUP_STORAGE.getId())
			return;

		ItemContainer container = event.getItemContainer();
		if (container == null)
			return;

		ItemMap snapshot = new ItemMap(mapItems(container.getItems()));

		if (initialSharedBankItems == null)
		{
			initialSharedBankItems = snapshot;
			return;
		}

		modifiedSharedBankItems = snapshot;
	}

	private Item[] mapItems(net.runelite.api.Item[] items) {
		return Arrays.stream(items).map(i ->
				new Item(itemManager.canonicalize(i.getId()), itemManager.getItemComposition(i.getId()).getMembersName(), i.getQuantity())
		).toArray(Item[]::new);
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event) {
		if (!isEnabled())
			return;

		final int param1 = event.getParam1();
		final MenuAction menuAction = event.getMenuAction();
		if (menuAction != MenuAction.CC_OP)
			return;

		if (param1 == OPEN_SHARED_STORAGE)
		{
			initialSharedBankItems = null;
			modifiedSharedBankItems = null;
		}
		else if (param1 == SAVE_SHARED_STORAGE
				|| param1 == BACK_TO_BANK_SHARED_STORAGE
				|| param1 == CLOSE_SHARED_STORAGE)
		{
			if (initialSharedBankItems == null || modifiedSharedBankItems == null)
			{
				log.debug("Skipping webhook — no valid snapshot");
				initialSharedBankItems = null;
				modifiedSharedBankItems = null;
				return;
			}

			List<ItemTransfer> itemTransfers =
					initialSharedBankItems.getItemTransfers(modifiedSharedBankItems);

			if (itemTransfers == null || itemTransfers.isEmpty())
			{
				initialSharedBankItems = null;
				modifiedSharedBankItems = null;
				return;
			}

			Player player = client.getLocalPlayer();
			if (player == null)
			{
				initialSharedBankItems = null;
				modifiedSharedBankItems = null;
				return;
			}

			sendWebhook(
					transferMessageCreator.createTransferMessages(
							itemTransfers,
							player.getName()
					)
			);

			initialSharedBankItems = null;
			modifiedSharedBankItems = null;
		}
	}

	private void sendWebhook(DiscordWebhook discordWebhook) {
		String configUrl = config.webhook();
		if (Strings.isNullOrEmpty(configUrl)) { return; }

		List<String> webhookUrls = Arrays.stream(configUrl.split("\n"))
				.filter(u -> u.length() > 0)
				.map(String::trim)
				.collect(Collectors.toList());

		String jsonStr = GSON.toJson(discordWebhook);
		webhookUrls.forEach(url -> apiTool.postRaw(url, jsonStr, "application/json")
			.handle((_v, e) ->
			{
				if (e != null)
					log.error(e.getMessage());
				return null;
			}));
	}

	private boolean isEnabled() {
		return isLoggedIn() && isCorrectWorldType();
	}

	private boolean isLoggedIn() {
		return client.getGameState() == GameState.LOGGED_IN && client.getLocalPlayer() != null;
	}

	private boolean isCorrectWorldType() {
		EnumSet<WorldType> worldTypes = client.getWorldType();
		for (WorldType worldType : worldTypes) {
			if (worldType == WorldType.SEASONAL ||
					worldType == WorldType.DEADMAN ||
					worldType == WorldType.TOURNAMENT_WORLD ||
					worldType == WorldType.PVP_ARENA) {
				return false;
			}
		}

		return true;
	}

	@Provides
	DiscordGroupBankNotificationsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DiscordGroupBankNotificationsConfig.class);
	}
}
