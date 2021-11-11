package ir.jeykey.megastreamermode.config;

import ir.jeykey.megastreamermode.utils.Common;

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