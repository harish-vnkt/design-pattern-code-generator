package DesignPatternBuilders;

import DesignPatternCodeGenerator.CodeBuilder;
import DesignPatterns.AbstractFactory.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Builder for Abstract Factory design pattern
 *
 */
public class AbstractFactoryBuilder implements CodeBuilder {

    // user input
    String entryClass, clientClass, factoryClass, factoryInterface, productAClass, productBClass;
    private Logger logger;
    // output directory
    String directoryPath;

    public AbstractFactoryBuilder(String directoryPath, String entryClass, String clientClass, String factoryClass,
                                  String factoryInterface, String productAClass, String productBClass) {
        this.logger = LoggerFactory.getLogger("DesignPatternBuilders.AbstractFactoryBuilder");
        this.directoryPath = directoryPath;
        this.entryClass = entryClass;
        this.clientClass = clientClass;
        this.factoryClass = factoryClass;
        this.factoryInterface = factoryInterface;
        this.productAClass = productAClass;
        this.productBClass = productBClass;
    }

    public void writeFile() throws BadLocationException, IOException {

        // create file objects
        Entry entryObject = new Entry(this.entryClass, this.clientClass, this.factoryClass);
        Client clientObject = new Client(this.clientClass, this.productAClass, this.productBClass, this.factoryClass);
        AbstractFactory abstractFactoryObject = new AbstractFactory(this.factoryInterface,
                this.productAClass, this.productBClass);
        Factory factoryObject = new Factory(this.factoryClass, this.factoryInterface,
                this.productAClass, this.productBClass);
        ProductA productAObject = new ProductA(this.productAClass);
        ProductB productBObject = new ProductB(this.productBClass);

        // writing the generated code
        logger.debug("Creating Entry file");
        String entryFilename = this.directoryPath + entryObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(entryFilename), entryObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Client file");
        String clientFilename = this.directoryPath + clientObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(clientFilename), clientObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating AbstractFactory file");
        String abstractFactoryFilename = this.directoryPath + abstractFactoryObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(abstractFactoryFilename), abstractFactoryObject.buildCode().get(),
                StandardCharsets.UTF_8);
        logger.debug("Creating ConcreteFactory file");
        String factoryFilename = this.directoryPath + factoryObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(factoryFilename), factoryObject.buildCode().get(),
                StandardCharsets.UTF_8);
        logger.debug("Creating ProductA file");
        String productAFilename = this.directoryPath + productAObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(productAFilename), productAObject.buildCode().get(),
                StandardCharsets.UTF_8);
        logger.debug("Creating ProductB file");
        String productBFilename = this.directoryPath + productBObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(productBFilename), productBObject.buildCode().get(),
                StandardCharsets.UTF_8);

    }
}
