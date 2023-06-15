package me.sudden.discordbot.handlers;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class embedHandler {

    //TODO redo this shit, I want to make it an override where you can use "this."
    public static void embedGenerator(String title, String description, String fieldName, String fieldValue, SlashCommandEvent e) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setAuthor("Bolognas Bot", null, "https://cdn.discordapp.com/avatars/985278208876380170/31b9d5b5ba01fed60a23d0f0fba0dee9.webp?size=80");
        eb.setTitle(title);
        eb.setColor(Color.RED);
        eb.setDescription(description);

        if (fieldName != null && fieldValue != null) {
            eb.addField(fieldName, fieldValue, true);
        }

        e.replyEmbeds(eb.build()).queue();
    }
}
