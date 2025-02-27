package com.example;

import java.io.*;
import java.util.Scanner;

interface FileSearchListener {
    void onFileFound(String filePath);
}

public class GicaRecursion {
    public static void searchFiles(File directory, String extension, FileSearchListener listener) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        searchFiles(file, extension, listener);
                    } else if (file.getName().endsWith(extension)) {
                        listener.onFileFound(file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter directory path: ");
        String directoryPath = scanner.nextLine();

        System.out.print("Enter file extension to search for (e.g., .txt, .java): ");
        String extension = scanner.nextLine();

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory. Please enter a valid path.");
            return;
        }

        System.out.println("Searching...");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("search_results.txt"))) {
            FileSearchListener listener = new FileSearchListener() {
                public void onFileFound(String filePath) {
                    System.out.println("File found: " + filePath);
                    try {
                        writer.write(filePath + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            searchFiles(directory, extension, listener);
            System.out.println("Search completed. Results saved to search_results.txt.");

        } catch (IOException e) {
            System.out.println("Error writing to file.");
            e.printStackTrace();
        }
    }
}
