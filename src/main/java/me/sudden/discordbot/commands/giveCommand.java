package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static me.sudden.discordbot.handlers.databaseHandler.*;
import static me.sudden.discordbot.handlers.embedHandler.embedGenerator;

public class giveCommand extends SlashCommand {
    public giveCommand() {
        this.name = "give";
        this.help = "Gives someone deef.";
        this.guildOnly = true;

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.MENTIONABLE, "user", "User to pay.").setRequired(true));
        options.add(new OptionData(OptionType.NUMBER, "amount", "Amount to give.").setRequired(true));
        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent e) {
        User receiver = e.getOption("user").getAsUser();
        Double inputedNumber = e.getOption("amount").getAsDouble();
        BigDecimal giveAmount = BigDecimal.valueOf(inputedNumber);

        if (!isInvalidAmount(giveAmount)) {
            giveAmount(receiver, giveAmount);

            embedGenerator
                    (
                            "Deef Coin",
                            receiver.getAsMention() + " has been given " + formatNumber(giveAmount) + " deef by " + e.getUser().getAsMention() + ".",
                            receiver.getName() + "'s Balance",
                            formatNumber(checkBalance(receiver)),
                            e);
        } else {
            embedGenerator(
                    "Deef Coin",
                    "Invalid pay amount entered: " + inputedNumber,
                    "Please enter a valid pay amount.",
                    "Example: /pay @Sudden 23.94",
                    e
            );
        }
    }
}
