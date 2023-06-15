package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import me.sudden.discordbot.handlers.embedHandler;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static me.sudden.discordbot.handlers.apiHandler.executePost;

public class britishCommand extends SlashCommand {
    public britishCommand() {
        this.guildOnly = true;
        this.name = "british";
        this.help = "Converts yor text ter sound like a proper Brit.";
        this.options = Collections.singletonList(new OptionData(OptionType.STRING, "text", "Your text to convert to Brit.").setRequired(true));
    }

    @Override
    protected void execute(SlashCommandEvent e) {
        System.out.println("Currently requesting British translation.");

        try {
            String option = e.getOption("text").getAsString();
            System.out.println("Requested text to translate: '" + option + "'");
            JSONObject jo = executePost("https://api.funtranslations.com/translate/cockney.json?text=", URLEncoder.encode(option, StandardCharsets.UTF_8));
            String response;
            response = jo.getJSONObject("contents").getString("translated");

            embedHandler.embedGenerator
                    (
                            "Britfier",
                            response,
                            null,
                            null,
                            e
                    );
        } catch (Exception exception) {
            embedHandler.embedGenerator
                    (
                            "Britfier",
                            "API limit reached.",
                            null,
                            null,
                            e
                    );
            throw new RuntimeException(exception);
        }
    }
}
