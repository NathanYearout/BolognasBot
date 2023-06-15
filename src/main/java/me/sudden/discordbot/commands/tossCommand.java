package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static me.sudden.discordbot.handlers.databaseHandler.*;
import static me.sudden.discordbot.handlers.embedHandler.embedGenerator;

public class tossCommand extends SlashCommand {
    public tossCommand() {
        this.guildOnly = true;
        this.name = "toss";
        this.help = "Challenge someone to a coin toss for credits.";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.MENTIONABLE, "user", "User to challenge.").setRequired(true));
        options.add(new OptionData(OptionType.NUMBER, "wager", "Amount to wager.").setRequired(true));
        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent e) {
        User challengeReceiver = e.getOption("user").getAsUser();
        BigDecimal wager = BigDecimal.valueOf(e.getOption("wager").getAsDouble());

        if (!isInvalidAmount(wager)) {
            System.out.println("Toss!");
            // Check if both the user's have enough.

            if (wager.compareTo(checkBalance(e.getUser())) > 0) {
                embedGenerator(
                        "Coin Toss",
                        "You do not have enough deef.",
                        "Balance",
                        formatNumber(checkBalance(e.getUser())),
                        e
                );
            } else if (wager.compareTo(checkBalance(challengeReceiver)) > 0) {
                embedGenerator(
                        "Coin Toss",
                        "The person you challenged does not have enough deef.",
                        challengeReceiver.getAsTag() + " Balance",
                        formatNumber(checkBalance(challengeReceiver)),
                        e
                );
            } else {
                // Have the receiver accept the challenge
                EmbedBuilder acceptDecline = new EmbedBuilder();
                acceptDecline.setAuthor("Bolognas Bot", null, "https://cdn.discordapp.com/avatars/985278208876380170/31b9d5b5ba01fed60a23d0f0fba0dee9.webp?size=80");
                acceptDecline.setTitle("Coin Toss", null);
                acceptDecline.setDescription(challengeReceiver.getAsMention() + " you have been challenged to a coin toss by " + e.getUser().getAsMention() + ".");
                acceptDecline.addField("Do you accept?)", "Wager: " + formatNumber(wager) + " Payout: " + formatNumber(wager.multiply(BigDecimal.valueOf(2))), true);

                acceptDecline.setColor(Color.RED);

                e.getChannel().sendMessageEmbeds(acceptDecline.build()).queue(message -> {
                    message.addReaction("\u2705").queue();
                    message.addReaction("\uD83D\uDEAB").queue();
                });

                //TODO Add event waiter which detects for which emote was pressed. Make it a separate class and check message ID.

                int randomNum = ThreadLocalRandom.current().nextInt(0, 1);
            }
        } else {
            embedGenerator(
                    "Coin Toss",
                    "Invalid wager amount entered: " + wager,
                    "Please enter a valid wager amount.",
                    "Example: /toss @Sudden 23.94",
                    e
            );
        }
    }
}
