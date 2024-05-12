package uraniumape.mcbot.tests;

import org.junit.jupiter.api.Test;
import uraniumape.mcbot.Bot;
import uraniumape.mcbot.script.responses.Message;
import uraniumape.mcbot.storage.Bots;
import java.util.List;

import javax.script.ScriptException;


public class BotTest {

    @Test
    public void testEval() throws ScriptException, NoSuchMethodException {
        Message message = new Message(null, "!test");
        String testScript =
                "function onMessageSend(bot, message) {" +
                    "var content = message.getContent();" +
                    "if(content.equals(\"!test\")) {" +
                        "print(\"eval is working\");" +
                    "}" +
                "}";

        Bot bot = new Bot("Test Bot", testScript);

        bot.readMessage(message);
    }
}
