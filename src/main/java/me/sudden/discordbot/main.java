package me.sudden.discordbot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import me.sudden.discordbot.commands.*;
import me.sudden.discordbot.schedules.claims;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static me.sudden.discordbot.handlers.databaseHandler.addUser;
import static org.quartz.CronScheduleBuilder.cronSchedule;

public class main {
    public static void main(String[] args) throws Exception {
        /*
        project started development on 6/11/2022.
        I am using a custom fork of JDA utils called JDA-Chewtils.
        The goal of this bot is to be an all around utility bot for my own server.
        */
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId("525695788689522688");

        // Bot prefix
        builder.setPrefix("s/");

        // Commands
        builder.addSlashCommand(new balanceCommand());
        builder.addSlashCommand(new payCommand());
        builder.addSlashCommand(new setCommand());
        builder.addSlashCommand(new waifuCommand());
        builder.addSlashCommand(new dailyCommand());
        builder.addSlashCommand(new giveCommand());
        builder.addSlashCommand(new kanyeCommand());
        builder.addSlashCommand(new britishCommand());
        builder.addSlashCommand(new tossCommand());
        builder.addCommand(new pingCommand());

        // Build the CommandClient instance
        CommandClient commandClient = builder.build();

        // Build
        JDA jda = JDABuilder
                .create(System.getenv("BOT_TOKEN"), GatewayIntent.GUILD_MEMBERS)
                //.enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(commandClient)
                .setActivity(Activity.watching("The Deef Empire"))
                .build()
                .awaitReady();

        // Variables
        String guildID = "980923873807700058";
        Guild guild = jda.getGuildById(guildID);
        guild.updateCommands();

        // If a member doesn't have an entry in the me.sudden.discordbot.database then add it
        if (guild != null) {
            // filters out member ID's from guild.getMemberCache
            List<Object> memberIDs = guild.getMemberCache().applyStream(stream -> stream.map(Member::getIdLong).collect(Collectors.toList()));

            for (int i = 0; i < memberIDs.size(); i++) {

                User user = jda.getUserById(memberIDs.get(i).toString());
                // Check if members ID's are in me.sudden.discordbot.database then add the missing
                addUser(user);
            }
        } else {
            System.out.println("Guild is NULL.");
        }

        // Scheduler to reset daily claims to true
        JobDetail job = JobBuilder.newJob(claims.class)
                .withIdentity("claims", "group1").build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("claims", "group1")
                .withSchedule(cronSchedule("0 0 0 ? * * *"))
                .forJob(job)
                .build();
        // schedule it
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }
}
