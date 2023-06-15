package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

import java.math.BigDecimal;

import static me.sudden.discordbot.handlers.databaseHandler.*;
import static me.sudden.discordbot.handlers.embedHandler.embedGenerator;

public class dailyCommand extends SlashCommand {

    public dailyCommand() {
        this.guildOnly = true;
        this.name = "daily";
        this.help = "Collect your daily deef.";
    }

    long maidenless = 980996986645938196L;
    long merchants = 980984022891130931L;
    long artisans = 980984100770963456L;
    long peasants = 980984136103764028L;
    long ronin = 980984206949752902L;
    long samurai = 980984306845487125L;
    long diamyos = 980984347274403870L;
    long shogun = 980984542787686410L;

    @Override
    protected void execute(SlashCommandEvent e) {
        if (canClaim(e.getUser())) {
            BigDecimal rewardAmount;
            String roleName;

            // Check what roles the user has and set daily giveAmount accordingly
            // TODO Make this nicer, absolutely horrendous code
            if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == shogun)) {
                rewardAmount = new BigDecimal(45);
                roleName = "shogun";
            } else if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == diamyos)) {
                rewardAmount = new BigDecimal(40);
                roleName = "diamyo";
            } else if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == samurai)) {
                rewardAmount = new BigDecimal(35);
                roleName = "samurai";
            } else if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == ronin)) {
                rewardAmount = new BigDecimal(30);
                roleName = "ronin";
            } else if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == peasants)) {
                rewardAmount = new BigDecimal(25);
                roleName = "peasant";
            } else if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == artisans)) {
                rewardAmount = new BigDecimal(20);
                roleName = "artisan";
            } else if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == merchants)) {
                rewardAmount = new BigDecimal(15);
                roleName = "merchant";
            } else if (e.getMember().getRoles().stream().anyMatch(role -> role.getIdLong() == maidenless)) {
                rewardAmount = new BigDecimal(10);
                roleName = "maidenless";
            } else {
                rewardAmount = new BigDecimal(10);
                roleName = "maidenless";
            }
            giveAmount(e.getUser(), rewardAmount);

            embedGenerator
                    (
                            "Daily Deef",
                            e.getUser().getAsMention() + " has been given " + formatNumber(rewardAmount) + " for being a " + roleName + ".",
                            "Balance",
                            checkBalance(e.getUser()).toString(),
                            e
                    );

            setUserClaim(e.getUser(), false);
        } else {
            embedGenerator
                    (
                            "Daily Deef",
                            "You can not claim again today.",
                            null,
                            null,
                            e
                    );
        }
    }
}
