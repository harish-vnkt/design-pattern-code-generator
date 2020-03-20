package DesignPatternBuilders;

import DesignPatternCodeGenerator.CodeBuilder;
import DesignPatterns.Mediator.Colleague;
import DesignPatterns.Mediator.ConcreteColleague;
import DesignPatterns.Mediator.MediatorClass;
import DesignPatterns.Mediator.MediatorInterface;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

/**
 * Builder for Mediator design pattern
 *
 */
public class MediatorBuilder implements CodeBuilder {

    String mediatorInterface, mediatorClass, colleagueInterface, colleague1Class, colleague2Class;
    String directoryPath;
    private Logger logger;

    public MediatorBuilder(String directoryPath, String mediatorInterface, String mediatorClass, String colleagueInterface,
                           String colleague1Class, String colleague2Class) {
        this.logger = LoggerFactory.getLogger("DesignPatternBuilders.Mediator");
        this.directoryPath = directoryPath;
        this.mediatorInterface = mediatorInterface;
        this.mediatorClass = mediatorClass;
        this.colleagueInterface = colleagueInterface;
        this.colleague1Class = colleague1Class;
        this.colleague2Class = colleague2Class;
    }

    public void writeFile() throws BadLocationException, IOException {

        // create file objects
        MediatorInterface mediatorInterfaceObject =
                new MediatorInterface(this.mediatorInterface, this.colleagueInterface);
        MediatorClass mediatorClassObject =
                new MediatorClass(this.mediatorClass, this.mediatorInterface, this.colleagueInterface,
                        this.colleague1Class, this.colleague2Class);
        Colleague colleagueInterfaceObject =
                new Colleague(this.colleagueInterface, this.mediatorInterface);
        ConcreteColleague colleague1Object = new ConcreteColleague(this.colleague1Class,
                this.colleagueInterface);
        ConcreteColleague colleague2Object = new ConcreteColleague(this.colleague2Class,
                this.colleagueInterface);

        // write the generated code to files
        logger.debug("Creating Mediator Interface file");
        String mediatorInterfaceFilename = this.directoryPath + mediatorInterfaceObject.fileName
                + ".java";
        FileUtils.writeStringToFile(new File(mediatorInterfaceFilename),
                mediatorInterfaceObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Mediator file");
        String mediatorClassFilename = this.directoryPath + mediatorClassObject.fileName
                + ".java";
        FileUtils.writeStringToFile(new File(mediatorClassFilename),
                mediatorClassObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Colleague Interface file");
        String colleagueInterfaceFilename = this.directoryPath + colleagueInterfaceObject.fileName
                + ".java";
        FileUtils.writeStringToFile(new File(colleagueInterfaceFilename),
                colleagueInterfaceObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Colleague1 file");
        String colleague1Filename = this.directoryPath + colleague1Object.fileName
                + ".java";
        FileUtils.writeStringToFile(new File(colleague1Filename),
                colleague1Object.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Colleague2 file");
        String colleague2Filename = this.directoryPath + colleague2Object.fileName
                + ".java";
        FileUtils.writeStringToFile(new File(colleague2Filename),
                colleague2Object.buildCode().get(), StandardCharsets.UTF_8);
        
    }

}
