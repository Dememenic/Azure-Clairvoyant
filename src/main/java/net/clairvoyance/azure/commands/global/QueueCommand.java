package net.clairvoyance.azure.commands.global;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.clairvoyance.azure.util.VCHelper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.clairvoyance.azure.lavaplayer.GuildMusicManager;
import net.clairvoyance.azure.lavaplayer.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class QueueCommand {
    private static final String name = "queue";

    public static void register(List<CommandData> commandData) {
        commandData.add(Commands.slash(name, "Displays the current music queue."));
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
            GuildMusicManager guildMusicManager = PlayerManager.getINSTANCE().getMusicManager(event.getGuild());
            List<AudioTrack> queue = new ArrayList<>(guildMusicManager.scheduler.queue);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Current Queue");
            if(queue.isEmpty()) {
                embedBuilder.setDescription("Queue is empty");
            }
            for(int i = 0; i < queue.size(); i++) {
                AudioTrackInfo info = queue.get(i).getInfo();
                embedBuilder.addField(i+i + ":", info.title, false);
            }
            event.replyEmbeds((embedBuilder.build())).queue();
        }
    }
}