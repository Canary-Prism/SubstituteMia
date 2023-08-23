package substitutemia;

import org.javacord.api.entity.channel.TextChannel;

public class Meow {

    private TextChannel channel;

    private int intervalmin, intervalmax, amount;

    private boolean amount_is_time;

    private Repeater repeater;

    public Meow(TextChannel channel, int intervalmin, int intervalmax, int amount, boolean amount_is_time) {
        this.channel = channel;
        this.intervalmin = intervalmin;
        this.intervalmax = intervalmax;
        this.amount = amount;
        this.amount_is_time = amount_is_time;
    }

    public void start() {
        repeater = new Repeater("meow", intervalmin, intervalmax, amount, amount_is_time, () -> {
            channel.sendMessage(meow());
        });
        repeater.start();
    }

    public void stop() {
        repeater.operational = false;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public static String[] meow_worbs = {
        "meow",
        "nyaa",
        "nya",
        "mrow",
        "meww",
        "mrow?",
        "meowww",
        "mew?",
        "にゃ〜"
    };

    protected static String meow() {
        return meow_worbs[(int)(Math.random() * meow_worbs.length)];
    }


    protected class Repeater implements Runnable {
        private Thread t;
        private long pausemin, pausemax;
        private String name;
        private RepeaterContent content;

        private long time = 0;
        
        private long terminates_after, start_time;

        private boolean is_time;

        private boolean operational = true;

        protected Repeater(String name, long pausemin, long pausemax, long terminates_after, boolean is_time, RepeaterContent content) {
            this.name = name;
            this.pausemin = pausemin;
            this.pausemax = pausemax;
            this.content = content;
            this.terminates_after = terminates_after;
            this.is_time = is_time;
        }

        @Override
        public void run() {
            if (is_time) {
                start_time = System.currentTimeMillis();
            }
            while (operational) {
                time = System.nanoTime();
                content.run();
                System.out.printf("%21s\n", System.nanoTime() - time + " nanoseconds");
                if (!is_time) {
                    terminates_after--;
                    if (terminates_after == 0) {
                        break;
                    }
                } else {
                    if (terminates_after != -1 && System.currentTimeMillis() - start_time >= terminates_after * 1000) {
                        break;
                    }
                }
                try {
                    Thread.sleep(((int)(Math.random() * (pausemax + 1 - pausemin)) + pausemin) * 1000);
                } catch (InterruptedException e) {
                    System.out.println("what?"); //should never happen
                }
            }
        }

        protected void start() {
            if (t == null) {
                t = new Thread(this, name);
                t.start();
            }
        }
    }

    /**
     * I wanted to play with lambda expressions lmao
     */
    @FunctionalInterface
    protected interface RepeaterContent {

        /**
         * The code to run
         */
        public void run();
    }
}
