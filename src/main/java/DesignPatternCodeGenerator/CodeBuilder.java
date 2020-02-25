package DesignPatternCodeGenerator;

import org.eclipse.jface.text.BadLocationException;

import java.io.IOException;

public interface CodeBuilder {
    public void writeFile() throws BadLocationException, IOException;
}