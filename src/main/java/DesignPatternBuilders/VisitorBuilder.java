package DesignPatternBuilders;

import DesignPatternCodeGenerator.CodeBuilder;
import DesignPatterns.Visitor.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

/**
 * Builder for Visitor design pattern
 *
 */
public class VisitorBuilder implements CodeBuilder {

    String clientClass, visitorClass, visitorConcreteClass, elementClass, element1Class, element2Class;
    String directoryPath = "generated_patterns/Visitor/";
    private Logger logger;

    public VisitorBuilder(String clientClass, String visitorClass, String visitorConcreteClass,
                          String elementClass, String element1Class, String element2Class) {
        this.logger = LoggerFactory.getLogger("DesignPatternBuilders.Visitor");
        this.clientClass = clientClass;
        this.visitorClass = visitorClass;
        this.visitorConcreteClass = visitorConcreteClass;
        this.elementClass = elementClass;
        this.element1Class = element1Class;
        this.element2Class = element2Class;
    }

    public void writeFile() throws BadLocationException, IOException {

        // create file objects
        Client clientObject = new Client(this.clientClass, this.visitorClass, this.visitorConcreteClass,
                this.element1Class, this.element2Class);
        Visitor visitorObject = new Visitor(this.visitorClass, this.element1Class, this.element2Class);
        VisitorConcrete visitorConcreteObject = new VisitorConcrete(this.visitorConcreteClass,
                this.visitorClass, this.element1Class, this.element2Class);
        Element elementObject = new Element(this.elementClass, this.visitorClass);
        ElementConcrete elementConcreteObject1 = new ElementConcrete(this.element1Class,
                this.visitorClass, this.elementClass);
        ElementConcrete elementConcreteObject2 = new ElementConcrete(this.element2Class,
                this.visitorClass, this.elementClass);

        // write the generated code
        logger.debug("Creating Client file");
        String clientFilename = this.directoryPath + clientObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(clientFilename),
                clientObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Visitor file");
        String visitorFilename = this.directoryPath + visitorObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(visitorFilename),
                visitorObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating VisitorConcrete file");
        String visitorConcreteFilename = this.directoryPath + visitorConcreteObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(visitorConcreteFilename),
                visitorConcreteObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Element file");
        String elementFilename = this.directoryPath + elementObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(elementFilename),
                elementObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Element1 file");
        String elementConcreteFilename1 = this.directoryPath + elementConcreteObject1.fileName + ".java";
        FileUtils.writeStringToFile(new File(elementConcreteFilename1),
                elementConcreteObject1.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Element2 file");
        String elementConcreteFilename2 = this.directoryPath + elementConcreteObject2.fileName + ".java";
        FileUtils.writeStringToFile(new File(elementConcreteFilename2),
                elementConcreteObject2.buildCode().get(), StandardCharsets.UTF_8);

    }

}
