package io.catroll.encconvert;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ConvertFileEncoding {
    public static void convert(String filePath) {
        try {
            // Detect the current encoding of the file
            String currentEncoding = detectFileEncoding(filePath);

            // Check if the current encoding is already UTF-8
            if ("UTF-8".equalsIgnoreCase(currentEncoding)) {
                System.out.println("The file is already in UTF-8 encoding. No conversion needed.");
            } else {
                convertFileEncoding(filePath);
                System.out.println("File converted from " + currentEncoding + " to UTF-8.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Detect the encoding of the file using Apache Tika
    private static String detectFileEncoding(String filePath) throws IOException {
        byte[] bytes = readBytesFromFile(filePath);
        return detectEncoding(bytes);
    }

    // Convert the file encoding
    private static void convertFileEncoding(String filePath) throws IOException {
        try {
            // Read the original file
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, "GB2312");
            BufferedReader br = new BufferedReader(isr);

            // Read the content of the file and convert it to UTF-8
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            br.close();

            // Overwrite the original file with the UTF-8 encoded content
            FileOutputStream fos = new FileOutputStream(filePath);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(content.toString());

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static byte[] readBytesFromFile(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private static String detectEncoding(byte[] bytes) {

        if (isUTF8(bytes)) {
            return "UTF-8";
        }

        if (isGBK(bytes)) {
            return "GB2312";
        }

        return null;
    }

    private static boolean isUTF8(byte[] bytes) {
        Charset utf8Charset = Charset.forName("UTF-8");
        String decodedString = new String(bytes, utf8Charset);
        // Check if the decoded string can be successfully re-encoded to bytes without loss
        return Arrays.equals(bytes, decodedString.getBytes(utf8Charset));
    }

    private static boolean isGBK(byte[] bytes) {
        Charset gbkCharset = Charset.forName("GB2312");
        String decodedString = new String(bytes, gbkCharset);
        // Check if the decoded string can be successfully re-encoded to bytes without loss
        return Arrays.equals(bytes, decodedString.getBytes(gbkCharset));
    }
}