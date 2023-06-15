package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static me.sudden.discordbot.handlers.databaseHandler.isInvalidAmount;
import static me.sudden.discordbot.handlers.databaseHandler.payAmount;
import static me.sudden.discordbot.handlers.embedHandler.embedGenerator;

public class payCommand extends SlashCommand {
    public payCommand() {
        this.name = "pay";
        this.help = "Pay up.";
        this.guildOnly = true;

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.MENTIONABLE, "user", "User to pay.").setRequired(true));
        options.add(new OptionData(OptionType.NUMBER, "amount", "Amount to pay.").setRequired(true));
        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent e) {
        BigDecimal payAmount;
        payAmount = BigDecimal.valueOf(e.getOption("amount").getAsDouble());

        if (!isInvalidAmount(payAmount)) {
            payAmount
                    (
                            e.getUser(),
                            e.getOption("user").getAsUser(),
                            payAmount,
                            e);
        } else {
            embedGenerator(
                    "Deef Coin",
                    "Invalid pay amount entered: " + payAmount,
                    "Please enter a valid pay amount.",
                    "Example: /pay @Sudden 23.94",
                    e
            );
        }
    }
}
