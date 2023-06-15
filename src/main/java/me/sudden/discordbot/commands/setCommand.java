package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static me.sudden.discordbot.handlers.databaseHandler.formatNumber;
import static me.sudden.discordbot.handlers.databaseHandler.setBalance;
import static me.sudden.discordbot.handlers.embedHandler.embedGenerator;

public class setCommand extends SlashCommand {

    public setCommand() {
        this.guildOnly = true;
        this.name = "set";
        this.help = "Sets someone's deef balance.";
        this.ownerCommand = true;

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.MENTIONABLE, "user", "User to set.").setRequired(true));
        options.add(new OptionData(OptionType.NUMBER, "amount", "Amount to set.").setRequired(true));
        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent e) {
        User receiver = e.getOption("user").getAsUser();
        BigDecimal amount = BigDecimal.valueOf(e.getOption("amount").getAsDouble());

        setBalance(receiver, amount);

        embedGenerator
                (
                        "Deef Coin",
                        receiver.getAsMention() + "'s balance has been set to " + formatNumber(amount) + ".",
                        null,
                        null,
                        e
                );
    }
}
