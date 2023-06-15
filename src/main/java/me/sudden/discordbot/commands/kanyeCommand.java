package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import me.sudden.discordbot.handlers.embedHandler;
import org.json.JSONObject;

import static me.sudden.discordbot.handlers.apiHandler.executePost;

public class kanyeCommand extends SlashCommand {
    
    // Disabled because of recent events... Only for reference purposes.

    public kanyeCommand() {
        this.name = "kanye";
        this.help = "Says a random Kanye quote.";
        this.guildOnly = true;
    }

    @Override
    protected void execute(SlashCommandEvent e) {
        try {
            System.out.println("Currently requesting kanye quote.");

            JSONObject jo = executePost("https://api.kanye.rest", "/");
            String response;
            response = jo.getString("quote");

            embedHandler.embedGenerator
                    (
                            "Kanye Quote",
                            response + "\n- Kanye Omari West",
                            null,
                            null,
                            e
                    );
        } catch (Exception exception) {
            embedHandler.embedGenerator
                    (
                            "Kanye Quote",
                            "Error while fetching Kanye quote.",
                            null,
                            null,
                            e
                    );
            throw new RuntimeException(exception);
        }
    }
}
