package me.sudden.discordbot.handlers;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import static com.mongodb.client.model.Filters.eq;

public class databaseHandler {
    // Connect to me.sudden.discordbot.database
    static ConnectionString connectionString = new ConnectionString("Connection String Here");
    static MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .serverApi(ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build())
            .build();
    static MongoClient mongoClient = MongoClients.create(settings);
    static MongoDatabase database = mongoClient.getDatabase("database");

    static MongoCollection<Document> collection = database.getCollection("collection");

    // Adds a user to the me.sudden.discordbot.database
    public static void addUser(User user) {
        String ID = user.getId();
        Long int_ID = Long.parseLong(ID);
        String tag = user.getAsTag();

        if (collection.find(eq("discord_id", int_ID)).first() == null) {
            collection.insertOne(new Document()
                    .append("discord_id", int_ID)
                    .append("deef", new BigDecimal(0))
                    .append("daily_available", Boolean.TRUE)
            );
            System.out.println("Retrieved new user: " + tag + " ID: " + ID + " [Added to database!]");
        } else {
            System.out.println("Retrieved user: " + tag + " ID: " + ID + " [Already exists in database.]");
        }
    }

    public static BigDecimal checkBalance(User user) {
        Long long_ID = Long.parseLong(user.getId());

        Bson filter = Filters.eq("discord_id", long_ID);
        Bson projection = Projections.fields(Projections.include("deef"), Projections.excludeId());

        Document document = collection.find(filter)
                .projection(projection)
                .first();

        BigDecimal balance = new BigDecimal(document.get("deef").toString());

        System.out.println("Mongo replied with a balance of " + balance);

        return balance;
    }

    public static void setBalance(User user, BigDecimal changeAmount) {
        long id = Long.parseLong(user.getId());

        // Change senders balance
        collection.updateOne(Filters.eq("discord_id", id), Updates.set("deef", changeAmount));
    }

    public static void giveAmount(User user, BigDecimal giveAmount) {
        long id = Long.parseLong(user.getId());
        BigDecimal newBalance;

        newBalance = giveAmount.add(checkBalance(user));

        // Change senders balance
        collection.updateOne(Filters.eq("discord_id", id), Updates.set("deef", newBalance));
    }

    public static void payAmount(User sender, User receiver, BigDecimal payAmount, SlashCommandEvent e) {
        BigDecimal newSendBal;
        BigDecimal newReceiverBalance;
        BigDecimal receiverBalance;

        // Get senders balance
        BigDecimal sendOrigBal = checkBalance(sender);


        // See if sender has enough
        if (sendOrigBal.compareTo(payAmount) < 0) {
            embedHandler.embedGenerator
                    (
                            "Deef Coin",
                            "You do not have enough deef.",
                            "Balance",
                            formatNumber(sendOrigBal),
                            e
                    );
        } else {
            // Change senders balance
            newSendBal = sendOrigBal.subtract(payAmount);

            setBalance(sender, newSendBal);

            // Change receivers balance
            receiverBalance = checkBalance(receiver);
            newReceiverBalance = receiverBalance.add(payAmount);
            setBalance(receiver, newReceiverBalance);

            embedHandler.embedGenerator
                    (
                            "Deef Coin",
                            receiver.getAsMention() + " has been paid " + formatNumber(payAmount) + " deef by " + sender.getAsMention() + ".",
                            "Balance",
                            formatNumber(newSendBal),
                            e
                    );
        }
    }

    public static void resetClaims() {
        collection.updateMany(Filters.eq("daily_available", false), Updates.set("daily_available", true));
    }

    public static void setUserClaim(User user, boolean available) {
        long idLong = Long.parseLong(user.getId());

        // Change senders balance
        collection.updateOne(Filters.eq("discord_id", idLong), Updates.set("daily_available", available));
    }

    public static boolean canClaim(User user) {
        long idLong = Long.parseLong(user.getId());

        Bson filter = Filters.eq("discord_id", idLong);
        Bson projection = Projections.fields(Projections.include("daily_available"));

        // Grabs the document and then checks claimAvailable
        Document document = collection.find(filter)
                .projection(projection)
                .first();

        boolean claimAvailable = document.getBoolean("daily_available");

        System.out.println("Mongo replied with " + claimAvailable);

        return claimAvailable;
    }

    public static String formatNumber(BigDecimal number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("<:deef:986389059935035403>"); // Don't use null.
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(number);
    }

    // Used to see if there's more than two decimal places
    public static boolean isInvalidAmount(BigDecimal amount) {
        return (amount.scale() > 2);
    }
}
