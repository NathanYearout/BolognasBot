package me.sudden.discordbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class pingCommand extends Command {
    public pingCommand() {
        this.name = "ping";
        this.help = "Pings the bot.";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent e) {
        e.reply("Pong!");
    }
}
