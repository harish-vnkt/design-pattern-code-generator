package DesignPatternBuilders;

import DesignPatternCodeGenerator.CodeBuilder;
import DesignPatterns.ChainOfResponsibility.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.io.File;

public class ChainOfResponsibilityBuilder implements CodeBuilder {

    String senderClass, handlerClass, receiver1, receiver2;
    String directoryPath = "generated_patterns/ChainOfResponsibility/";

    public ChainOfResponsibilityBuilder(String senderClass, String handlerClass,
                                        String receiver1, String receiver2) {
        this.senderClass = senderClass;
        this.handlerClass = handlerClass;
        this.receiver1 = receiver1;
        this.receiver2 = receiver2;
    }

    public void writeFile() throws BadLocationException, IOException {

        // create file objects
        Sender senderObject = new Sender(this.senderClass, this.handlerClass, this.receiver1, this.receiver2);
        Handler handlerObject = new Handler(this.handlerClass);
        Receiver1 receiver1Object = new Receiver1(this.receiver1, this.handlerClass);
        Receiver2 receiver2Object = new Receiver2(this.receiver2, this.handlerClass);

        // write the generated code
        String senderFilename = this.directoryPath + senderObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(senderFilename), senderObject.buildCode().get(), StandardCharsets.UTF_8);
        String handlerFilename = this.directoryPath + handlerObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(handlerFilename), handlerObject.buildCode().get(), StandardCharsets.UTF_8);
        String receiver1Filename = this.directoryPath + receiver1Object.fileName + ".java";
        FileUtils.writeStringToFile(new File(receiver1Filename),
                receiver1Object.buildCode().get(), StandardCharsets.UTF_8);
        String receiver2Filename = this.directoryPath + receiver2Object.fileName + ".java";
        FileUtils.writeStringToFile(new File(receiver2Filename),
                receiver2Object.buildCode().get(), StandardCharsets.UTF_8);

    }

}
