package me.sudden.discordbot.schedules;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static me.sudden.discordbot.handlers.databaseHandler.resetClaims;

public class claims implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("It's Midnight, claims have been reset.");
        resetClaims();
    }
}
