package ir.jeykey.meganicks.config;

import ir.jeykey.meganicks.utils.Common;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomNames extends Configurable {
        public RandomNames() {
                super("players.txt");
        }

        @Override
        public void load() throws IOException, InvalidConfigurationException {
                // None, we won't load it as a YML file
        }

        @Override
        public void init() {

        }

        public static String get() {
                String randomLine = null;
                try {
                        final RandomAccessFile f = new RandomAccessFile(getFile(), "r");
                        final long randomLocation = (long) (Math.random() * f.length());
                        f.seek(randomLocation);
                        f.readLine();
                         randomLine = f.readLine();
                        f.close();
                } catch (IOException exception) {
                        Common.logPrefixed("There's a problem getting random name from your players.txt [ Check Stack Trace ]");
                        exception.printStackTrace();
                }
                return randomLine;
        }
}
