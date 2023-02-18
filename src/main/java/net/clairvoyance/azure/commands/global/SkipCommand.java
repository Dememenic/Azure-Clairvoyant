package net.clairvoyance.azure.commands.global;

import net.clairvoyance.azure.lavaplayer.GuildMusicManager;
import net.clairvoyance.azure.lavaplayer.PlayerManager;
import net.clairvoyance.azure.util.VCHelper;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;

public class SkipCommand {

    private static final String name = "skip";

    public static void register(List<CommandData> commandData) {
        commandData.add(Commands.slash(name, "Skips the current song."));
    }

    public static void run(SlashCommandInteractionEvent event) {
        if (event.getName().equals(name)) {

            if (!event.getMember().getVoiceState().inAudioChannel()) {
                event.reply("You need to be in a voice channel to use this command!").setEphemeral(true).queue();
                return;
            } else if (VCHelper.isInChannel(event) && !VCHelper.isInSameChannel(event)) {
                event.reply("You need to be in the same voice channel to use this command!").setEphemeral(true).queue();
                return;
            }
            Member member = event.getMember();
            GuildMusicManager guildMusicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
            guildMusicManager.scheduler.skipTrack();
            event.reply("Track skipped.").queue();
        }
    }
}
