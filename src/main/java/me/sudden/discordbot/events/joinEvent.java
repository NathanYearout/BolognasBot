package me.sudden.discordbot.events;

import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import static me.sudden.discordbot.handlers.databaseHandler.addUser;

public class joinEvent extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent e) {
        super.onGuildMemberJoin(e);

        addUser(e.getUser());
    }
}
