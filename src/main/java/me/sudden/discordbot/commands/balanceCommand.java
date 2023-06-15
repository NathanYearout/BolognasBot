package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.math.BigDecimal;
import java.util.Collections;

import static me.sudden.discordbot.handlers.databaseHandler.checkBalance;
import static me.sudden.discordbot.handlers.databaseHandler.formatNumber;
import static me.sudden.discordbot.handlers.embedHandler.embedGenerator;

public class balanceCommand extends SlashCommand {
    public balanceCommand() {
        this.guildOnly = true;
        this.name = "balance";
        this.help = "Checks your precious deef balance.";
        this.options = Collections.singletonList(new OptionData(OptionType.MENTIONABLE, "user", "User").setRequired(false));
    }

    @Override
    public void execute(SlashCommandEvent e) {
        if (e.getOption("user") == null) {

            BigDecimal deefAmount = checkBalance(e.getUser());

            embedGenerator
                    (
                            "Deef Coin",
                            "You have " + formatNumber(deefAmount) + " deef coins.",
                            null,
                            null,
                            e
                    );

        } else {

            User user = e.getOption("user").getAsUser();
            BigDecimal deefAmount = checkBalance(user);

            embedGenerator
                    (
                            "Deef Coin",
                            user.getAsMention() + " has " + formatNumber(deefAmount) + " deef coins.",
                            null,
                            null,
                            e
                    );

        }
    }
}
