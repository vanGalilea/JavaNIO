package nl.dikkeTim;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try (FileOutputStream binFile = new FileOutputStream("data.dat");
             FileChannel binChannel = binFile.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(100);
            byte[] outputBytes = "Hello World!".getBytes();
            buffer.put(outputBytes);
            buffer.putInt(112);
            buffer.putInt(-1212);
            byte[] outputBytes2 = "Hello Again".getBytes();
            buffer.put(outputBytes2);
            buffer.putInt(119);
            buffer.flip();
            binChannel.write(buffer);

//            buffer.put(outputBytes).putInt(112).putInt(-1212).put(outputBytes2).putInt(119); <- dit ook mogelijk 'chainen'

            RandomAccessFile ra = new RandomAccessFile("data.dat", "rwd");
            FileChannel channel = ra.getChannel();

            ByteBuffer readBuffer = ByteBuffer.allocate(100);

            channel.read(readBuffer);
            readBuffer.flip();
            byte[] inputString = new byte[outputBytes.length];
            readBuffer.get(inputString);
            System.out.println("inputString is: " + new String(inputString));
            System.out.println("int1 is: " + readBuffer.getInt());
            System.out.println("int2 is: " + readBuffer.getInt());
            byte[] inputString2 = new byte[outputBytes2.length];
            readBuffer.get(inputString2);
            System.out.println("inputString2 is: " + new String(inputString2));
            System.out.println("int3 is: " + readBuffer.getInt());

            RandomAccessFile copyFile = new RandomAccessFile("dataCopy.dat", "rwd");
            FileChannel copyChannel = copyFile.getChannel();
            channel.position(0);
            long numTransferred = copyChannel.transferFrom(channel, 0, channel.size());
            System.out.println("num transferred: " + numTransferred);

            channel.close();
            ra.close();
            copyChannel.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
