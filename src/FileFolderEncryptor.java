import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;

public class FileFolderEncryptor {
    public static void main(String[] args) {
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter file or folder path: ");
        String path = scanner.nextLine();

        System.out.print("Do you want to encrypt or decrypt? (e/d): ");
        String action = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        File file = new File(path);
        if (file.exists()) {
            try {
                if (file.isDirectory()) {
                    processDirectory(file, action, password);
                } else {
                    processFile(file, action, password);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("File or directory does not exist.");
        }
    }

    private static void processDirectory(File directory, String action, String password) throws Exception {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    processDirectory(file, action, password);
                } else {
                    processFile(file, action, password);
                }
            }
        }
    }

    private static void processFile(File file, String action, String password) throws Exception {
        String content = new String(Files.readAllBytes(file.toPath()));
        String resultContent;

        if ("e".equalsIgnoreCase(action)) {
            resultContent = AESUtil.encrypt(content, password);
        } else {
            try {
                resultContent = AESUtil.decrypt(content, password);
            } catch (Exception e) {
                throw new Exception("Incorrect password. File decryption failed.");
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(resultContent);
        }

        System.out.println((action.equals("e") ? "Encrypted" : "Decrypted") + " file: " + file.getAbsolutePath());
    }
}
