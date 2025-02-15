package fr.aerwyn81.headblocks.events;

import fr.aerwyn81.headblocks.HeadBlocks;
import fr.aerwyn81.headblocks.api.events.HeadClickEvent;
import fr.aerwyn81.headblocks.api.events.HeadDeletedEvent;
import fr.aerwyn81.headblocks.handlers.ConfigHandler;
import fr.aerwyn81.headblocks.handlers.LanguageHandler;
import fr.aerwyn81.headblocks.placeholders.InternalPlaceholders;
import fr.aerwyn81.headblocks.utils.FireworkUtils;
import fr.aerwyn81.headblocks.utils.FormatUtils;
import fr.aerwyn81.headblocks.utils.PlayerUtils;
import fr.aerwyn81.headblocks.utils.Version;
import fr.aerwyn81.headblocks.utils.xseries.XSound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.UUID;

public class OnPlayerInteractEvent implements Listener {

    private final HeadBlocks main;
    private final ConfigHandler configHandler;
    private final LanguageHandler languageHandler;

    public OnPlayerInteractEvent(HeadBlocks main) {
        this.main = main;
        this.configHandler = main.getConfigHandler();
        this.languageHandler = main.getLanguageHandler();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();

        // Check if the correct hand is used
        if (block == null || !isMainHand(e)) {
            return;
        }

        // Check if clickedBlock is a head
        if (!isClickedBlockIsHeadBlocks(block)) {
            return;
        }

        Location clickedLocation = block.getLocation();
        UUID headUuid = main.getHeadHandler().getHeadAt(clickedLocation);

        // Check if the head is a head of the plugin
        if (headUuid == null) {
            return;
        }

        Player player = e.getPlayer();

        // Actions to destroy the head only if player has the permission and the creative gamemode
        if (e.getAction() == Action.LEFT_CLICK_BLOCK && player.getGameMode() == GameMode.CREATIVE && PlayerUtils.hasPermission(player, "headblocks.admin")) {
            if (!player.isSneaking()) {
                e.setCancelled(true);
                player.sendMessage(languageHandler.getMessage("Messages.CreativeSneakRemoveHead"));
                return;
            }

            // Remove the head from the ground
            main.getHeadHandler().removeHead(headUuid);

            // If resetPlayerData is enabled, reset all player data for the head
            if (configHandler.shouldResetPlayerData()) {
                main.getStorageHandler().removeHead(headUuid);
            }

            // Send player success message
            player.sendMessage(languageHandler.getMessage("Messages.HeadRemoved")
                    .replaceAll("%x%", String.valueOf(clickedLocation.getX()))
                    .replaceAll("%y%", String.valueOf(clickedLocation.getY()))
                    .replaceAll("%z%", String.valueOf(clickedLocation.getZ()))
                    .replaceAll("%world%", clickedLocation.getWorld() != null ? clickedLocation.getWorld().getName() : FormatUtils.translate("&cUnknownWorld")));

            // Trigger the event HeadDeleted
            Bukkit.getPluginManager().callEvent(new HeadDeletedEvent(headUuid, clickedLocation));
            return;
        }

        // Check if the player has the permission to click on the head
        if (!PlayerUtils.hasPermission(player, "headblocks.use")) {
            String message = languageHandler.getMessage("Messages.NoPermissionBlock");

            if (!message.trim().isEmpty()) {
                player.sendMessage(message);
            }
            return;
        }

        // Check if the player has already clicked on the head
        if (main.getStorageHandler().hasAlreadyClaimedHead(player.getUniqueId(), headUuid)) {
            String message = languageHandler.getMessageWithPlaceholders(player, "Messages.AlreadyClaimHead");

            if (!message.trim().isEmpty()) {
                player.sendMessage(message);
            }

            // Already own song if not empty
            String songName = configHandler.getHeadClickAlreadyOwnSound();
            if (!songName.trim().isEmpty()) {
                try {
                    XSound.play(player, configHandler.getHeadClickAlreadyOwnSound());
                } catch (Exception ex) {
                    HeadBlocks.log.sendMessage(FormatUtils.translate("&cError cannot play sound on head click! Cannot parse provided name..."));
                }
            }

            // Already own particles if enabled
            if (Version.getCurrent().isNewerOrSameThan(Version.v1_13) && configHandler.isHeadClickParticlesEnabled()) {
                String particleName = configHandler.getHeadClickParticlesAlreadyOwnType();
                int amount = configHandler.getHeadClickParticlesAmount();
                double size = amount == 1 ? 0 : .5f;

                try {
                    player.getWorld().spawnParticle(Particle.valueOf(particleName), clickedLocation.clone().add(.5f, .1f, .5f), amount, size, size, size, 0);
                } catch (Exception ex) {
                    HeadBlocks.log.sendMessage(FormatUtils.translate("&cError particle name " + particleName + " cannot be parsed!"));
                }
            }

            // Trigger the event HeadClick with no success because the player already own the head
            Bukkit.getPluginManager().callEvent(new HeadClickEvent(headUuid, player, clickedLocation, false));
            return;
        }

        // Save player click in storage
        main.getStorageHandler().savePlayer(player.getUniqueId(), headUuid);

        // Success messages if not empty
        List<String> messages = configHandler.getHeadClickMessages();
        if (messages.size() != 0) {
            player.sendMessage(InternalPlaceholders.parse(player, messages));
        }

        // Success song if not empty
        String songName = configHandler.getHeadClickNotOwnSound();
        if (!songName.trim().isEmpty()) {
            try {
                XSound.play(player, songName);
            } catch (Exception ex) {
                HeadBlocks.log.sendMessage(FormatUtils.translate("&cError cannot play sound on head click! Cannot parse provided name..."));
            }
        }

        // Send title to the player if enabled
        if (configHandler.isHeadClickTitleEnabled() && Version.getCurrent().isNewerThan(Version.v1_10)) {
            String firstLine = InternalPlaceholders.parse(player, configHandler.getHeadClickTitleFirstLine());
            String subTitle = InternalPlaceholders.parse(player, configHandler.getHeadClickTitleSubTitle());
            int fadeIn = configHandler.getHeadClickTitleFadeIn();
            int stay = configHandler.getHeadClickTitleStay();
            int fadeOut = configHandler.getHeadClickTitleFadeOut();

            player.sendTitle(firstLine, subTitle, fadeIn, stay, fadeOut);
        }

        // Fire firework if enabled
        if (configHandler.isFireworkEnabled()) {
            List<Color> colors = configHandler.getHeadClickFireworkColors();
            List<Color> fadeColors = configHandler.getHeadClickFireworkFadeColors();
            boolean isFlickering = configHandler.isFireworkFlickerEnabled();
            int power = configHandler.getHeadClickFireworkPower();

            Location loc = power == 0 ? clickedLocation.clone() : clickedLocation.clone().add(0, 0.5, 0);

            FireworkUtils.launchFirework(loc, isFlickering,
                    colors.size() == 0, colors, fadeColors.size() == 0, fadeColors, power);
        }

        // Prevent trigger commands rewards if current is contained in tieredRewards and enabled in config
        if (!main.getConfigHandler().isPreventCommandsOnTieredRewardsLevel() || !main.getRewardHandler().currentIsContainedInTiered(main.getHeadBlocksAPI().getPlayerHeads(player.getUniqueId()).size())) {
            // Commands list if not empty
            if (configHandler.getHeadClickCommands().size() != 0) {
                Bukkit.getScheduler().runTaskLater(main, () -> configHandler.getHeadClickCommands().forEach(reward ->
                        main.getServer().dispatchCommand(main.getServer().getConsoleSender(), InternalPlaceholders.parse(player, reward))), 1L);
            }
        }

        // Check and reward if triggerRewards is used
        main.getRewardHandler().giveReward(player);

        // Trigger the event HeadClick with success
        Bukkit.getPluginManager().callEvent(new HeadClickEvent(headUuid, player, clickedLocation, true));
    }

    private boolean isMainHand(PlayerInteractEvent e) {
        if (Version.getCurrent() == Version.v1_8) {
            return true;
        }

        return e.getHand() == EquipmentSlot.HAND;
    }

    private boolean isClickedBlockIsHeadBlocks(Block block) {
        // Specific case where we only check if the block type if a skull
        if (Version.getCurrent().isOlderOrSameThan(Version.v1_12)) {
            return block.getType().name().equals("SKULL");
        }

        return block.getType() == main.getHeadHandler().getPluginHead().getType();
    }
}
