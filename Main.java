/*
Name: Bryan Bennett
Course: CSC201B
Date: 10/27/20
Program Description: This program takes command line arguments for an image file name, image dimensions, and table size and
navigates the image to pull in the pixels. It then takes each pixel and hashes it (using key "color") into an index within
a dynamically-allocated hashtable (see HashTable class). If two pixels are hashed to the same position, double-hashing is used
to find another index. If the same color is found, the count of that RGB combination is incremented in a "Freq" object. This allows
the program to keep a hashed list of all pixels from the image and their frequencies, which are sorted and printed.
 */

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        //Variable declaration, including reading command-line arguments: (fileName height width hashtableSize)
        String fileName = args[0];
        int n = Integer.parseInt(args[1]);
        int m = Integer.parseInt(args[2]);
        int arraySize = Integer.parseInt(args[3]);
        long startTime;
        long endTime;
        long timeElapsed;


        //Declare hashtable and freq used to store each successive pixel read
        HashTable ht = new HashTable(arraySize);
        Freq freq;

        //Print initial arguments, arraySize has been updated to the next highest prime if not already prime
        System.out.println("Method implemented: Double-hashing, array of Freqs");
        System.out.println("File name: "+fileName);
        System.out.println("Dimensions [n x m]: " + n + " x " + m);
        System.out.println("Initial table size (round provided value up to nearest prime): "+ ht.getArraySize());

        //Start clock after print statements
        startTime = System.nanoTime();

        //Attempt to open file, if this doesn't work catch exception.
        try {

            //Input streams used to read in .raw image file
            InputStream is = new FileInputStream(fileName);
            DataInputStream input = new DataInputStream(is);

            //Navigates image using command line args for dimensions.
            //Reads each byte, stores three successive bytes in RGB object and converted to freq
            //Freq inserted in hashTable.
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {

                    //Pulls in bytes from data input stream, which are the RGB bytes in a row. Create RGB object and store
                    RGB pixel = new RGB(input.readUnsignedByte(), input.readUnsignedByte(), input.readUnsignedByte());

                    //Create a freq using this pixel, which is initialized to count "1"
                    freq = new Freq(pixel);

                    //Insert this freq into hashtable object. Rehashing and collision resolution are determined in insertFreq
                    ht.insertFreq(freq);
                }
            }

            //Close streams
            input.close();
            is.close();

        } catch (IOException ex) { ex.printStackTrace(); }

        //Calculate elapsed time to determine program efficiency
        endTime = System.nanoTime();
        timeElapsed = endTime - startTime;

        //Sort table (which is permanent) and print all necessary components.
        ht.sort();
        System.out.println("Number of collisions (including those during rehashing): "+ ht.getNumCollisions());
        System.out.println("Number of rehashes: "+ ht.getNumRehashes());
        System.out.println("Final table size: "+ ht.getArraySize());
        System.out.println("Number of unique colors placed: "+ ht.getNumFullSpots());
        System.out.println("Execution time in nanoseconds: " + timeElapsed);
        System.out.println("Execution time in milliseconds: " + timeElapsed / 1000000);

        //This prints the 256 most common elements, after sorting occurs
        ht.print(256);

    }
}


