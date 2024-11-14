package me.dariansandru.io;

import me.dariansandru.io.exception.InputException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.System.exit;

public class InputDevice {

    static Scanner scanner = new Scanner(System.in);

    public String readString() throws InputException {
        try{
            return scanner.nextLine();
        }
        catch (Exception e){
            throw new InputException(e.getMessage());
        }
    }

    public Integer readInteger() throws InputException {
        try{
            return scanner.nextInt();
        }
        catch (Exception e){
            throw new InputException(e.getMessage());
        }
    }

    public Float readFloat() throws InputException {
        try{
            return scanner.nextFloat();
        }
        catch (Exception e){
            throw new InputException(e.getMessage());
        }
    }

    public List<String> readFile(String file) throws InputException {
        List<String> lineList = new ArrayList<>();

        try {
            File myFile = new File(file);
            Scanner reader = new Scanner(myFile);

            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                if (!data.isEmpty()) lineList.add(data);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            throw new InputException("The file could not be opened.");
        }

        return lineList;
    }

    public List<String> readFile(BufferedReader reader) throws InputException {
        List<String> lineList = new ArrayList<>();
        try {
            String data = reader.readLine();
            while (data != null) {
                if (!data.isEmpty()) lineList.add(data);
                data = reader.readLine();
            }
            reader.close();

        } catch (FileNotFoundException e) {
            throw new InputException("The file could not be opened.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return lineList;
    }

    public String readLine(String file, int lineNumber) throws InputException {
        try{
            File myFile = new File(file);
            Scanner reader = new Scanner(myFile);

            String data = reader.nextLine();
            int rowCount = 1;

            while (data != null){
                if (rowCount == lineNumber) return data;

                rowCount++;
                data = reader.nextLine();
            }

            reader.close();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFileEmpty(String filePath) throws InputException {
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            throw new InputException("File does not exist: " + filePath);
        }

        return file.length() == 0;
    }
}
