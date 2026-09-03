package peanutbuttercat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests commands processed through the chatbot's reusable application interface. */
class PeanutButterCatTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponse_addThenList_returnsFormattedResponses() {
        PeanutButterCat peanutButterCat = createChatbot();

        assertEquals("Purr-fect! I've added this task to my cat basket:" + System.lineSeparator()
                        + "[T][ ] buy cat food" + System.lineSeparator()
                        + "My cat basket now holds 1 task.",
                peanutButterCat.getResponse("todo buy cat food"));
        assertEquals("Here are the tasks in my cat basket:" + System.lineSeparator()
                        + "1.[T][ ] buy cat food",
                peanutButterCat.getResponse("list"));
    }

    @Test
    void getResponse_invalidCommand_returnsErrorAndKeepsTasks() {
        PeanutButterCat peanutButterCat = createChatbot();

        assertEquals("Hiss-terical mix-up! I don't know that command yet. Try another one, purr-lease!",
                peanutButterCat.getResponse("sleep"));
        assertEquals("Here are the tasks in my cat basket:", peanutButterCat.getResponse("list"));
    }

    @Test
    void isExitCommand_byeInput_returnsTrue() {
        PeanutButterCat peanutButterCat = createChatbot();

        assertTrue(peanutButterCat.isExitCommand(" bye "));
        assertFalse(peanutButterCat.isExitCommand("list"));
    }

    private PeanutButterCat createChatbot() {
        Path saveFile = temporaryDirectory.resolve("data").resolve("tasks.txt");
        return new PeanutButterCat(new Storage(saveFile.toString()), new Parser());
    }
}
