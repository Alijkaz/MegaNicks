package ir.jeykey.meganicks.config;

import ir.jeykey.meganicks.utils.Common;

public class Messages extends Configurable {
        public static String PREFIX;

        public Messages() {
                super("messages.yml");
        }

        @Override
        public void init() {
                PREFIX = Common.colorize(getConfig().getString("prefix"));
        }
}