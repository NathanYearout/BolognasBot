package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.JSONObject;

import java.util.Collections;

import static me.sudden.discordbot.handlers.apiHandler.executePost;
import static me.sudden.discordbot.handlers.embedHandler.embedGenerator;

public class waifuCommand extends SlashCommand {

    public waifuCommand() {
        this.guildOnly = true;
        this.name = "waifu";
        this.help = "Get random images of varying animes.";
        this.options = Collections.singletonList(new OptionData(OptionType.STRING, "option", "").setRequired(false));
    }

    @Override
    protected void execute(SlashCommandEvent e) {
        String option;
        try {
            option = e.getOption("option").getAsString();
            System.out.println("Currently requesting waifu category: " + option);
            JSONObject jo = executePost("https://api.waifu.im/random/?selected_tags=", option);
            String response;
            response = jo.getJSONArray("images").getJSONObject(0).getString("url");
            System.out.println(response);
            e.reply(response).queue();
        } catch (Exception exception) {
            embedGenerator(
                    "Waifu",
                    "Please enter a valid option.",
                    "Options:",
                    "",
                    e
            );
        }
    }
}
