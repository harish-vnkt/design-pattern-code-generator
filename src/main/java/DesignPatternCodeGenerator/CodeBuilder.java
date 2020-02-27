package DesignPatternCodeGenerator;

import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;

/**
 * Interface implemented by builder classes of each design pattern.
 * The builder classes instantiate the individual CodeGenerators in each
 * design pattern and build the code, finally writing them to file.
 *
 * @author Harish Venkataraman
 */
public interface CodeBuilder {
    // function to be implemented
    public void writeFile() throws BadLocationException, IOException;
}