package uraniumape.mcbot.tests;

import org.junit.jupiter.api.Test;
import uraniumape.mcbot.Bot;
import uraniumape.mcbot.storage.Bots;
import java.util.List;

import javax.script.ScriptException;


public class BotTest {

    //Does not work.
    @Test
    public void testEval() throws ScriptException, NoSuchMethodException {
        List<Bot> bots = Bots.getInstance().getBots();

        for(Bot bot : bots) {
            bot.readMessage("test");
        }
    }
}
