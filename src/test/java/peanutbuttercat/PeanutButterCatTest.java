package peanutbuttercat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

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

    @Test
    void getCommandType_knownAndUnknownInput_returnsMatchingType() {
        PeanutButterCat peanutButterCat = createChatbot();

        assertEquals(CommandType.TODO, peanutButterCat.getCommandType("todo buy cat food"));
        assertEquals(CommandType.UNKNOWN, peanutButterCat.getCommandType("sleep"));
    }

    @Test
    void getResponse_statsAfterCompletingTask_returnsRecentCompletionCount() {
        PeanutButterCat peanutButterCat = createChatbot(fixedClock());
        peanutButterCat.getResponse("todo submit report");
        peanutButterCat.getResponse("mark 1");

        assertEquals("Tasks completed in the last 7 calendar days: 1.",
                peanutButterCat.getResponse("stats"));
    }

    @Test
    void getResponse_statsWithArguments_returnsSpecificError() {
        PeanutButterCat peanutButterCat = createChatbot(fixedClock());

        assertEquals("My whiskers don't need extra details for statistics! Use: stats",
                peanutButterCat.getResponse("stats week"));
    }

    @Test
    void getResponse_statsWithLegacyCompletedTask_reportsUnknownDateWithoutSaving() throws IOException {
        Path saveFile = temporaryDirectory.resolve("legacy").resolve("tasks.txt");
        Files.createDirectories(saveFile.getParent());
        Files.write(saveFile, List.of("T | 1 | old completed task"));
        PeanutButterCat peanutButterCat = new PeanutButterCat(
                new Storage(saveFile.toString()), new Parser(), fixedClock());

        assertEquals("Tasks completed in the last 7 calendar days: 0." + System.lineSeparator()
                        + "Note: 1 completed task has an unknown completion date and was not counted.",
                peanutButterCat.getResponse("stats"));
        assertEquals(List.of("T | 1 | old completed task"), Files.readAllLines(saveFile));
    }

    private PeanutButterCat createChatbot() {
        return createChatbot(Clock.systemDefaultZone());
    }

    private PeanutButterCat createChatbot(Clock clock) {
        Path saveFile = temporaryDirectory.resolve("data").resolve("tasks.txt");
        return new PeanutButterCat(new Storage(saveFile.toString()), new Parser(), clock);
    }

    private Clock fixedClock() {
        return Clock.fixed(Instant.parse("2026-09-09T12:00:00Z"), ZoneOffset.UTC);
    }
}
