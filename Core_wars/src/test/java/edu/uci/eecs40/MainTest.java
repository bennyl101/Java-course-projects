package edu.uci.eecs40;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testDummySimulator(){
        Main.main(new String[]{"src/test/resources/dummySimulatorTest/program_x.txt", "src/test/resources/dummySimulatorTest/program_y.txt"});
        String result;
        try{
            // For large files do not load all lines in memory
            List<String> allLines = Files.readAllLines(Paths.get("src/test/resources/dummySimulatorTest/expected_output.txt"));
            result = String.join("\n", allLines);
            Assert.assertEquals(result, outContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("File exception");
        }


    }

}
