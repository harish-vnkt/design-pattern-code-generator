package DesignPatternCodeGenerator;

import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;
import java.util.Scanner;

public class UserEntry {
    public static void main(String[] args) throws IOException, BadLocationException {
        while(true) {
            try {
                System.out.println("Select design pattern to generate\n" +
                        "1 - Abstract Factory\n" +
                        "2 - Builder\n" +
                        "3 - Chain of Responsibility\n" +
                        "4 - Facade\n" +
                        "5 - Factory\n" +
                        "6 - Mediator\n" +
                        "7 - Visitor\n");
                Scanner scanner = new Scanner(System.in);
                int i = scanner.nextInt();
                new DesignPatternFactory(i);
                break;
            }
            catch (Exception e) {
                System.out.println("Enter again\n");
            }
        }
    }
}
