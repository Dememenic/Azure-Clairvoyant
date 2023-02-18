package net.clairvoyance.azure.commands.global;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.clairvoyance.azure.lavaplayer.GuildMusicManager;
import net.clairvoyance.azure.lavaplayer.PlayerManager;
import net.clairvoyance.azure.util.VCHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.List;

public class NowPlayingCommand {
    private static final String name = "nowplaying";

    public static void register(List<CommandData> commandData) {
        commandData.add(Commands.slash(name, "Displays the song that's currently playing."));
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
            if (guildMusicManager.scheduler.audioPlayer.getPlayingTrack() == null) {
                event.reply("I am not playing anything").queue();
            }
            AudioTrackInfo info = guildMusicManager.audioPlayer.getPlayingTrack().getInfo();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Currently Playing");
            embedBuilder.setDescription("**Name:** `" + info.title + "`");
            embedBuilder.appendDescription("\n**Author:** `" + info.author + "`");
            embedBuilder.appendDescription("\n**URL:** `" + info.uri + "`");
            embedBuilder.appendDescription("\n**Duration:** `" + info.length + "`");
            event.replyEmbeds(embedBuilder.build()).queue();
        }
    }
}
