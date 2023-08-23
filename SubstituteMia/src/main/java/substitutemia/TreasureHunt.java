package substitutemia;

import java.util.ArrayList;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.listener.interaction.ButtonClickListener;

public class TreasureHunt {

    TextChannel channel;
    User user;

    int posx = 0, posy = 0, treasurex = 0, treasurey = 0;

    Message message;

    public TreasureHunt(TextChannel channel, User user) {
        this.channel = channel;
        this.user = user;
    }

    public void start() {
        channel.sendMessage("Treasure hunting time!\n" + //
                " Original credit to verysmollgecko!! \n" + //
                " Modified by Mia :3\n" + //
                "\n" + //
                "(press the **north,south,east,west** buttons to move!) NOW FIND IT!! :3\n" + //
                "--------").join();

        while (distance() == 0) {
            posx = randomInt(0, 10); 
            posy = randomInt(0, 10); 
            treasurex = randomInt(0, 10); 
            treasurey = randomInt(0, 10);
        }


        
        message = new MessageBuilder().append("Your starting coordinates are: *" + posx + "," + posy + "*\n" + //
                "---\n" + //
                "You're **" + distance() + "** spaces away :3").addActionRow(
            Button.primary("north", "North"),
            Button.primary("south", "South"),
            Button.primary("east", "East"),
            Button.primary("west", "West"),
            Button.danger("stop", "Cancel")
        ).send(channel).join();

        message.addButtonClickListener((e) -> {
            var interaction = e.getButtonInteraction();
            if (interaction.getUser().getId() == user.getId()) {
                if (interaction.getCustomId().equals("north")) {
                    posy++;
                } else if (interaction.getCustomId().equals("south")) {
                    posy--;
                } else if (interaction.getCustomId().equals("east")) {
                    posx++;
                } else if (interaction.getCustomId().equals("west")) {
                    posx--;
                } else if (interaction.getCustomId().equals("stop")) {
                    channel.sendMessage("-----\n" + //
                            "\n" + //
                            "aww.. okiee :c\n").join();
                    message.getButtonClickListeners().forEach((listener) -> {
                        message.removeListener(ButtonClickListener.class, listener);
                    });
                    message.delete();
                    return;
                } else if (interaction.getCustomId().equals("continue")) {
                    message.createUpdater().removeAllComponents().applyChanges().join();
                    message.createUpdater().addComponents(ActionRow.of(
                        Button.primary("north", "North"),
                        Button.primary("south", "South"),
                        Button.primary("east", "East"),
                        Button.primary("west", "West"),
                        Button.danger("stop", "Cancel")
                    )).applyChanges().join();
                }
                
                message.edit("Your coordinates are: *" + posx + "," + posy + "*\n" + //
                        "---\n" + //
                        "You're **" + distance() + "** spaces away :3").join();

                if (posx < 0 || posx > 10 || posy < 0 || posy > 10) {
                    message.edit("-----\n" + //
                            "\n" + //
                            "You fell off the map! :o\n" + //
                            "You're back a random location now :3").join();
                    

                    posx = treasurex;
                    posy = treasurey;
                    while (posx == treasurex && posy == treasurey) {
                        posx = randomInt(0, 10);
                        posy = randomInt(0, 10);
                    }
                    interaction.acknowledge();
                    message.createUpdater().removeAllComponents().applyChanges().join();
                    message.createUpdater().addComponents(ActionRow.of(
                        Button.primary("continue", "Continue")
                    )).applyChanges().join();
                }
                        
                if (posx == treasurex && posy == treasurey) {
                    channel.sendMessage("-----\n" + //
                            "\n" + //
                            "**You found my treasure!!** AAARRRRRRGGGHH!!!!\n" + //
                            "Good job tho :3 *headpat*").join();
                    channel.sendMessage(pat()).join();
                    message.createUpdater().removeAllComponents().applyChanges().join();
                    message.getButtonClickListeners().forEach((listener) -> {
                        message.removeListener(ButtonClickListener.class, listener);
                    });
                    return;
                }
                interaction.acknowledge();
            } else {
                interaction.createImmediateResponder().setContent("<@" + interaction.getUser().getIdAsString() + "> This is not your game! >:3").respond();
            }
        });
    }

    public static String[] pats = {
        "https://tenor.com/view/ganyu-pat-gif-26282342",
        "https://tenor.com/view/anime-head-pat-anime-head-rub-neko-anime-love-anime-gif-16121044",
        "https://tenor.com/view/pat-gif-26552870"
    };

    protected static String pat() {
        return pats[(int) (Math.random() * pats.length)];
    }

    private int distance() {
        return Math.abs(posx - treasurex) + Math.abs(posy - treasurey);
    }

    private int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
