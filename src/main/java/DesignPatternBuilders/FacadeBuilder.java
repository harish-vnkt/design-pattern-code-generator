package DesignPatternBuilders;

import DesignPatternCodeGenerator.CodeBuilder;
import DesignPatterns.Facade.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

/**
 * Builder for Facade design pattern
 *
 */
public class FacadeBuilder implements CodeBuilder {

    String facadeClass, facadeSubClass, class1, class2, class3, method1, method2, method3;
    String directoryPath = "generated_patterns/Facade/";
    private Logger logger;

    public FacadeBuilder(String facadeClass, String facadeSubClass, String class1, String class2, String class3,
                         String method1, String method2, String method3) {
        this.logger = LoggerFactory.getLogger("DesignPatternBuilders.FacadeBuilder");
        this.facadeClass = facadeClass;
        this.facadeSubClass = facadeSubClass;
        this.class1 = class1;
        this.class2 = class2;
        this.class3 = class3;
        this.method1 = method1;
        this.method2 = method2;
        this.method3 = method3;
    }

    public void writeFile() throws BadLocationException, IOException {

        // create file objects
        Facade facadeObject = new Facade(this.facadeClass);
        FacadeSubClass facadeSubClassObject = new FacadeSubClass(this.facadeSubClass, this.facadeClass,
                this.class1, this.class2, this.class3);
        Component class1Object = new Component(this.class1, this.method1);
        Component class2Object = new Component(this.class2, this.method2);
        Component class3Object = new Component(this.class3, this.method3);

        // write the generated code
        logger.debug("Creating Facade file");
        String facadeFilename = this.directoryPath + facadeObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(facadeFilename), facadeObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating FacadeSubclass file");
        String facadeSubClassFilename = this.directoryPath + facadeSubClassObject.fileName + ".java";
        FileUtils.writeStringToFile(new File(facadeSubClassFilename),
                facadeSubClassObject.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Class1 file");
        String class1Filename = this.directoryPath + class1Object.fileName + ".java";
        FileUtils.writeStringToFile(new File(class1Filename), class1Object.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Class2 file");
        String class2Filename = this.directoryPath + class2Object.fileName + ".java";
        FileUtils.writeStringToFile(new File(class2Filename), class2Object.buildCode().get(), StandardCharsets.UTF_8);
        logger.debug("Creating Class3 file");
        String class3Filename = this.directoryPath + class3Object.fileName + ".java";
        FileUtils.writeStringToFile(new File(class3Filename), class3Object.buildCode().get(), StandardCharsets.UTF_8);

    }

}
