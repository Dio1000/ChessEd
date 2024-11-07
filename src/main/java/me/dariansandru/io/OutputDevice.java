package me.dariansandru.io;

import me.dariansandru.io.exception.OutputException;

import java.io.*;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class OutputDevice {

    public void write(String line){
        System.out.print(line);
    }

    public void write(Integer value){
        System.out.print(value);
    }

    public void write(Float value){
        System.out.println(value);
    }

    public void writeLine() { System.out.println(); }

    public void writeLine(String line){
        System.out.println(line);
    }

    public void writeLine(Integer value){
        System.out.println(value);
    }

    public void writeLine(Float value){
        System.out.println(value);
    }

    public void writeToFile(List<String> list, String fileName) throws OutputException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : list) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new OutputException("Could not write to file.");
        }
    }

    public void emptyFile(String fileName) throws OutputException {
        try{
            File file = new File(fileName);
            if (file.exists()){
                if (!file.delete()){
                    throw new IOException("Could not delete " + fileName);
                }
            }

            File parent = file.getParentFile();
            if (!parent.exists()){
                if (!parent.mkdirs()){
                    throw new IOException("Could not create directory for " + fileName);
                }
            }

            if (!file.createNewFile()) throw new IOException("Could not create empty file.");
        }catch(IOException e){
            throw new OutputException("Could not erase from file.");
        }
    }
}
