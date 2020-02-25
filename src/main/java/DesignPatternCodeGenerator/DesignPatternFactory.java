package DesignPatternCodeGenerator;

import com.typesafe.config.*;
import DesignPatternBuilders.*;
import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;

public class DesignPatternFactory {

    private int patternNumber;
    private Config configObject;

    public DesignPatternFactory(int patternNumber) throws IOException, BadLocationException {
        this.patternNumber = patternNumber;
        this.configObject = ConfigFactory.load();
        callBuilder();;
    }

    private void callBuilder() throws IOException, BadLocationException {
        if (patternNumber == 1) {
            AbstractFactoryBuilder abstractFactoryBuilder = new AbstractFactoryBuilder(
                    this.configObject.getString("AbstractFactory.entryClass"),
                    this.configObject.getString("AbstractFactory.clientClass"),
                    this.configObject.getString("AbstractFactory.factoryClass"),
                    this.configObject.getString("AbstractFactory.factoryInterfaceClass"),
                    this.configObject.getString("AbstractFactory.productAClass"),
                    this.configObject.getString("AbstractFactory.productBClass")
            );
            abstractFactoryBuilder.writeFile();
        }
    }

}
