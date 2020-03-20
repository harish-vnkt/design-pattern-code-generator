package DesignPatternBuilders;

import DesignPatternCodeGenerator.CodeBuilder;
import DesignPatterns.Factory.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

/**
 * Builder for Factory design pattern
 *
 */
public class FactoryBuilder implements CodeBuilder {

    String creatorClass, subCreatorClass, productInterface, productAClass;
    String directoryPath;
    private Logger logger;

    public FactoryBuilder(String directoryPath, String creatorClass, String subCreatorClass,
                          String productInterface, String productAClass) {
        this.logger = LoggerFactory.getLogger("DesignPatternBuilders.FactoryBuilder");
        this.directoryPath = directoryPath;
        this.creatorClass = creatorClass;
        this.subCreatorClass = subCreatorClass;
        this.productInterface = productInterface;
        this.productAClass = productAClass;
    }

    public void writeFile() throws BadLocationException, IOException {

        // create file objects
        Creator creatorObject = new Creator(this.creatorClass, this.productInterface);
        SubCreator subCreatorObject = new SubCreator(this.subCreatorClass, this.creatorClass, this.productInterface,
                this.productAClass);
        Product productObject = new Product(this.productInterface);
        ProductA productAObject = new ProductA(this.productAClass, this.productInterface);

        // write the generated code
        String creatorFilename = this.directoryPath + creatorObject.fileName + ".java";
        String subCreatorFilename = this.directoryPath + subCreatorObject.fileName + ".java";
        String productFilename = this.directoryPath + productObject.fileName + ".java";
        String productAFilename = this.directoryPath + productAObject.fileName + ".java";
        logger.debug("Creating Creator file");
        FileUtils.writeStringToFile(new File(creatorFilename),
                creatorObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating CreatorSubclass file");
        FileUtils.writeStringToFile(new File(subCreatorFilename),
                subCreatorObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Product file");
        FileUtils.writeStringToFile(new File(productFilename),
                productObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating ProductA file");
        FileUtils.writeStringToFile(new File(productAFilename),
                productAObject.buildCode().get(), StandardCharsets.UTF_8);

    }
}
