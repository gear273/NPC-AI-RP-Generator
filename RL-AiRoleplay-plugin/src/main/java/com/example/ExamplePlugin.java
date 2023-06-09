package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Example"
)
public class ExamplePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ExampleConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private MenuManager menuManager;

	@Override
	protected void startUp() throws Exception
	{
		//log.info("Example started!");
		eventBus.register(this);
	}

	@Override
	protected void shutDown() throws Exception
	{
		//log.info("Example stopped!");
		eventBus.unregister(this);
	}


	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event){
		if (event.getMenuAction() == MenuAction.RUNELITE && event.getMenuOption().equals("View Story"))
		{
			NPC npc = client.getCachedNPCs()[event.getId()];
			if (npc != null)
			{
				int npcId = npc.getId();
				log.info("Given NPC has ID: " +npcId);
				//send to AI API
			}
		}
	}

	private void addCustomRightClickOptionToAllNPCs(String option)
	{
		NPC[] npcs = client.getCachedNPCs();
		if (npcs != null)
		{
			for (NPC npc : npcs)
			{
				if (npc != null && npc.getName() != null)
				{
					addCustomRightClickOption(npc, option);
				}
			}
		}
	}
	private void addCustomRightClickOption(NPC npc, String option)
	{
		String targetName = Text.removeTags(npc.getName());
		menuManager.addPlayerMenuItem("View Story");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	ExampleConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ExampleConfig.class);
	}
}
