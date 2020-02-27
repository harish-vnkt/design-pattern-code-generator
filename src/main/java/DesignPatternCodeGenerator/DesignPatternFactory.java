package DesignPatternCodeGenerator;

import com.typesafe.config.*;
import DesignPatternBuilders.*;
import org.eclipse.jface.text.BadLocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * Abstract factory for generating design patterns. This class
 * is instantiated in the user-facing class and used to send
 * the user input to the factory. The factory calls the appropriate
 * builder class to build the generated code of a design pattern.
 *
 * @author Harish Venkataraman
 */
public class DesignPatternFactory {

    // indicates the design pattern chosen
    private int patternNumber;
    // object that takes user input from config file
    private Config configObject;
    // logger object
    private static Logger logger;

    /**
     * Constructor used to set some of the fields
     *
     * @param patternNumber indicates the chosen design pattern
     * @throws IOException
     * @throws BadLocationException
     */
    public DesignPatternFactory(int patternNumber) throws IOException, BadLocationException {
        this.patternNumber = patternNumber;
        // reads user input from config file
        this.configObject = ConfigFactory.load();
        // instantiate logger
        logger = LoggerFactory.getLogger("DesignPatternCodeGenerator.DesignPatternFactory");
        // function called at instantiation
        callBuilder();
    }

    /**
     * This function calls the appropriate builder for the chosen
     * design pattern
     *
     * @throws IOException
     * @throws BadLocationException
     */
    private void callBuilder() throws IOException, BadLocationException {

        if (patternNumber == 1) {
            logger.debug("Building Abstract Factory design pattern");
            AbstractFactoryBuilder abstractFactoryBuilder = new AbstractFactoryBuilder(
                    this.configObject.getString("AbstractFactory.entryClass"),
                    this.configObject.getString("AbstractFactory.clientClass"),
                    this.configObject.getString("AbstractFactory.factoryClass"),
                    this.configObject.getString("AbstractFactory.factoryInterface"),
                    this.configObject.getString("AbstractFactory.productAClass"),
                    this.configObject.getString("AbstractFactory.productBClass")
            );
            abstractFactoryBuilder.writeFile();
        }

        else if (patternNumber == 2) {
            logger.debug("Building Builder design pattern");
            BuilderBuilder builderBuilder = new BuilderBuilder(
                    this.configObject.getString("Builder.directorClass"),
                    this.configObject.getString("Builder.builderClass"),
                    this.configObject.getString("Builder.complexObjectClass"),
                    this.configObject.getString("Builder.productInterface"),
                    this.configObject.getString("Builder.productAClass"),
                    this.configObject.getString("Builder.productBClass")
            );
            builderBuilder.writeFile();
        }

        else if (patternNumber == 3) {
            logger.debug("Building Factory design pattern");
            FactoryBuilder factoryBuilder = new FactoryBuilder(
                    this.configObject.getString("Factory.creatorClass"),
                    this.configObject.getString("Factory.subCreatorClass"),
                    this.configObject.getString("Factory.productInterface"),
                    this.configObject.getString("Factory.productAClass")
            );
            factoryBuilder.writeFile();
        }

        else if (patternNumber == 4) {
            logger.debug("Building Facade design pattern");
            FacadeBuilder facadeBuilder = new FacadeBuilder(
                    this.configObject.getString("Facade.facadeClass"),
                    this.configObject.getString("Facade.facadeSubClass"),
                    this.configObject.getString("Facade.class1"),
                    this.configObject.getString("Facade.class2"),
                    this.configObject.getString("Facade.class3"),
                    this.configObject.getString("Facade.method1"),
                    this.configObject.getString("Facade.method2"),
                    this.configObject.getString("Facade.method3")
            );
            facadeBuilder.writeFile();;
        }

        else if (patternNumber == 5) {
            logger.debug("Building Chain of Responsibility design pattern");
            ChainOfResponsibilityBuilder corBuilder = new ChainOfResponsibilityBuilder(
                    this.configObject.getString("ChainOfResponsibility.senderClass"),
                    this.configObject.getString("ChainOfResponsibility.handlerClass"),
                    this.configObject.getString("ChainOfResponsibility.receiver1Class"),
                    this.configObject.getString("ChainOfResponsibility.receiver2Class")
            );
            corBuilder.writeFile();
        }

        else if (patternNumber == 6) {
            logger.debug("Building Mediator design pattern");
            MediatorBuilder mediatorBuilder = new MediatorBuilder(
                    this.configObject.getString("Mediator.mediatorInterface"),
                    this.configObject.getString("Mediator.mediatorClass"),
                    this.configObject.getString("Mediator.colleagueAbstractClass"),
                    this.configObject.getString("Mediator.colleague1Class"),
                    this.configObject.getString("Mediator.colleague2Class")
            );
            mediatorBuilder.writeFile();
        }

        else if (patternNumber == 7) {
            logger.debug("Building Visitor design pattern");
            VisitorBuilder visitorBuilder = new VisitorBuilder(
                    this.configObject.getString("Visitor.clientClass"),
                    this.configObject.getString("Visitor.visitorAbstractClass"),
                    this.configObject.getString("Visitor.visitorConcreteClass"),
                    this.configObject.getString("Visitor.elementAbstractClass"),
                    this.configObject.getString("Visitor.element1Class"),
                    this.configObject.getString("Visitor.element2Class")
            );
            visitorBuilder.writeFile();
        }
        
    }

}
