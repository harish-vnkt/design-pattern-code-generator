package DesignPatternBuilders;

import org.junit.AfterClass;
import org.junit.Test;

import org.eclipse.jface.text.BadLocationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Integration to test code generation of each design pattern
 *
 * @author Harish Venkataraman
 */
public class BuilderTest {

    private Logger logger = LoggerFactory.getLogger("DesignPatternBuilders.BuilderTest");

    /**
     * Used for testing if the AbstractFactory code is being generated
     * with mock inputs without error
     */
    @Test
    public void shouldBuildAbstractFactory() throws IOException, BadLocationException {
        logger.debug("Testing Abstract Factory design pattern");
        AbstractFactoryBuilder abstractFactoryBuilder = new AbstractFactoryBuilder(
                "generated_patterns/AbstractFactory/", "Entry", "Client", "Factory",
                "FactoryInterface", "ProductA", "ProductB"
        );
        abstractFactoryBuilder.writeFile();
    }

    /**
     * Used for testing if the Builder code is being generated
     * with mock inputs without error
     */
    @Test
    public void shouldBuildBuilder() throws IOException, BadLocationException {
        logger.debug("Testing Builder design pattern");
        BuilderBuilder builderBuilder = new BuilderBuilder(
                "generated_patterns/Builder/","Director", "Builder", "ComplexObject",
                "ProductInterface", "ProductA", "ProductB"
        );
        builderBuilder.writeFile();
    }

    /**
     * Used for testing if the ChainOfResponsibility code is being generated
     * with mock inputs without error
     */
    @Test
    public void shouldBuildChainOfResponsibility() throws IOException, BadLocationException {
        logger.debug("Testing ChainOfResponsibility design pattern");
        ChainOfResponsibilityBuilder chainOfResponsibilityBuilder = new ChainOfResponsibilityBuilder(
                "generated_patterns/ChainOfResponsibility/","Sender", "Handler", "Receiver1",
                "Receiver2"
        );
        chainOfResponsibilityBuilder.writeFile();
    }

    /**
     * Used for testing if the Facade code is being generated
     * with mock inputs without error
     */
    @Test
    public void shouldBuildFacade() throws IOException, BadLocationException {
        logger.debug("Testing Facade design pattern");
        FacadeBuilder facadeBuilder = new FacadeBuilder(
                "generated_patterns/Facade/","Facade", "Facade1", "Class1",
                "Class2", "Class3", "method1", "method2",
                "method3"
        );
        facadeBuilder.writeFile();
    }

    /**
     * Used for testing if the Factory code is being generated
     * with mock inputs without error
     */
    @Test
    public void shouldBuildFactory() throws IOException, BadLocationException {
        logger.debug("Testing Factory design pattern");
        FactoryBuilder factoryBuilder = new FactoryBuilder(
                "generated_patterns/Factory/","Creator", "Creator1", "Product",
                "ProductA"
        );
        factoryBuilder.writeFile();
    }

    /**
     * Used for testing if the Mediator code is being generated
     * with mock inputs without error
     */
    @Test
    public void shouldBuildMediator() throws IOException, BadLocationException {
        logger.debug("Testing Mediator design pattern");
        MediatorBuilder mediatorBuilder = new MediatorBuilder(
                "generated_patterns/Mediator/","Mediator", "Mediator1", "Colleague",
                "Colleague1", "Colleague2"
        );
        mediatorBuilder.writeFile();
    }

    /**
     * Used for testing if the Visitor code is being generated
     * with mock inputs without error
     */
    @Test
    public void shouldBuildVisitor() throws IOException, BadLocationException {
        logger.debug("Testing Visitor design pattern");
        VisitorBuilder visitorBuilder = new VisitorBuilder(
                "generated_patterns/Visitor/","Client", "Visitor", "Visitor1",
                "Element", "Element1", "Element2"
        );
        visitorBuilder.writeFile();
    }

    @AfterClass
    public static void deleteGeneratedFolder() throws IOException {
        FileUtils.deleteDirectory(new File("generated_patterns/"));
        Logger temporaryLogger = LoggerFactory.getLogger("DesignPatternBuilders.BuilderTest");
        temporaryLogger.debug("Deleted generated patterns");
    }

}
