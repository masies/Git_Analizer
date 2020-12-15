package com.group4.softwareanalytics;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class LOCExtractorTest {



    @Test
    public void locExtractorTest()
    {
        createFile();

        List<Integer> lines = LOCExtractor.extractLines("locTest.java");

        assertNotNull(lines);

        assertEquals(9,lines.size());

    }

    public void createFile()
    {
        try {
            File myObj = new File("locTest.java");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("locTest.java");
            myWriter.write("/**\n" +
                    " * @author: BeginnersBook.com\n" +
                    " * @description: Program to Calculate Area of rectangle\n" +
                    " */\n" +
                    "import java.util.Scanner;\n" +
                    "class AreaOfRectangle {\n" +
                    "   public static void main (String[] args)\n" +
                    "   {\n" +
                    "\t   Scanner scanner = new Scanner(System.in);\n" +
                    "\t   System.out.println(\"Enter the length of Rectangle:\");\n" +
                    "\t   double length = scanner.nextDouble();\n" +
                    "\t   System.out.println(\"Enter the width of Rectangle:\");\n" +
                    "\t   double width = scanner.nextDouble();\n" +
                    "\t   //Area = length*width;\n" +
                    "\t   double area = length*width;\n" +
                    "\t   System.out.println(\"Area of Rectangle is:\"+area);\n" +
                    "   }\n" +
                    "}");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}