package substitutemia;

import org.javacord.api.event.message.MessageCreateEvent;

public class Trigger {
    
    private String trigger;
    public String getTrigger() {
        return trigger;
    }

    private String response;
    public String getResponse() {
        return response;
    }

    private int availability;
    public int getAvailability() {
        return availability;
    }

    private boolean requires_ping;

    public boolean requiresPing() {
        return requires_ping;
    }

    public Trigger(String trigger, String response, int availability, boolean requires_ping) {
        this.trigger = trigger;
        this.response = response;
        this.availability = availability;
        this.requires_ping = requires_ping;
    }

    public void evaluate(MessageCreateEvent e, int channel_status) {
        if (
            e.getMessage().getContent().contains(trigger) 
            && 
            (
                (
                    e.getMessageAuthor().isBotOwner()
                    && 
                    e.getMessage().getMentionedUsers().contains(e.getApi().getYourself())
                ) 
                ||
                (
                    channel_status > 0
                    &&
                    (channel_status | availability) == channel_status
                    && 
                    (
                        !requires_ping
                        ||
                        e.getMessage().getMentionedUsers().contains(e.getApi().getYourself())
                    )
                )
            )
        ) {
            e.getChannel().sendMessage(response).join();
        }
    }
}
