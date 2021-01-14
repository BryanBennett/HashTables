/*
Name: Bryan Bennett
Course: CSC201B
Date: 10/27/20
Program Description: This class creates a hashtable object in main that stores Freq objects. Array size, the hashtable, and various variables for
counting purposes are tracked as well. Double-hashing is used for collision resolution (see "insertFreq" method), and whenever the
table becomes half full it is rehashed.
 */

public class HashTable {

    //Variables
    protected int numFullSpots;
    protected int numRehashes;
    protected int totalColorsPlaced;
    protected int arraySize;
    protected int numCollisions;
    protected Freq[] hashtable;

    //Constructor
    public HashTable(int arrayLength){
        //Check if arraySize is prime, if not find next highest prime
        this.arraySize = closePrime(arrayLength, 1);

        //Declare hashtable and initialize all other variables as zero
        hashtable = new Freq[this.arraySize];
        numFullSpots = 0; numRehashes = 0; totalColorsPlaced = 0; numCollisions = 0;
    }


    //Methods
    public int getArraySize() { return arraySize; }
    public int getNumRehashes(){ return numRehashes; }
    public int getNumCollisions(){ return numCollisions; }
    public int getNumFullSpots() {return numFullSpots;}

    //Iterative method that hashes color and attempts to insert it. If the index is full with
    // another color, double-hashing used to resolve collision inside while loop.
    public void insertFreq(Freq freq){

        //If hashtable is more than half full, rehash
        if ( numFullSpots >= (arraySize/2) ) { reHash(); }

        //Acts as H1(x), which is the normal hashing equation prior to collision resolution
        int H_x = freq.getKey() % arraySize;

        //Variables used in double hashing collision resolution. R is the closest prime smaller than table size
        int R = closePrime(arraySize - 1, 0);
        int H_rehash = (R - (freq.getKey() % R));

        int index;
        int i;

        //If there isn't already a freq (color) in this index, place this new one
        if ( (hashtable[H_x] == null) ) {
            hashtable[H_x] = freq;
            numFullSpots++;
            totalColorsPlaced++;
        }
        //If there is a freq (color) there, check if its the same color. If so increment count
        else if ( hashtable[H_x].getRGB() == freq.getRGB() ) {
            hashtable[H_x].incrementCount();
            totalColorsPlaced++;
        }
        //There is a freq there and it's not the same color. Collision, double-hash by incrementing i
        else {
            i = 1;
            while (true) {

                //Increment numCollisions, get double-hash index
                numCollisions++;
                index = (H_x + (i * H_rehash)) % arraySize;

                //If there isn't already a freq (color) in this index, place this new one
                if ( (hashtable[index] == null) ) {
                    hashtable[index] = freq;
                    numFullSpots++;
                    totalColorsPlaced++;
                    return;
                }
                //If there is a freq (color) there, check if its the same color. If so increment count
                else if ( hashtable[index].getRGB() == freq.getRGB() ) {
                    hashtable[index].incrementCount();
                    totalColorsPlaced++;
                    return;
                }
                //Increment i, start over again
                i++;
            }
        }
    }


    //Called when array will be more than half full after an insert. Pulls freqs out of table and rehashes
    // them in a table of 2X the current size, rounded up to the nearest prime
    public void reHash(){

        Freq[] tmp = hashtable;                                 //Store old hashtable
        Freq freq;                                              //Used to store each freq from old table temporarily
        int oldArraySize = this.arraySize;                      //Used to navigate old hashtable

        this.arraySize = closePrime(2*oldArraySize, 1);  //Update arraySize to be 2* old size, rounded up to next prime
        hashtable = new Freq[arraySize];                        //Reallocate new hashtable
        numRehashes++;                                          //Increment numRehashes
        this.numFullSpots = 0;                                  //Update numFullSpots and totalColorsPlaced to reflect new table
        this.totalColorsPlaced = 0;

        //Navigate old hashtable. If there is something at a particular index, rehash it
        for (int i = 0; i < oldArraySize; i++) {
            freq = tmp[i];
            if (freq != null) { insertFreq(freq); }
        }
    }


    //Insertion sort, sorts in descending order (largest on top).
    // Takes into account null locations and sorts them down the list.
    //NOTE* this method permanently sorts the hashtable, which will put
    // the colors in the incorrect hash indices. Use this only after you are done with insertions
    public void sort(){
        Freq tmp;
        for (int i = 1; i < arraySize; i++){
            for (int j = i; j > 0; j--){
                //If value @x-1 < value @x, move value @x left
                if (hashtable[j-1] != null && hashtable[j] != null){
                    if (hashtable[j - 1].getCount() < hashtable[j].getCount()) {
                        tmp = hashtable[j];
                        hashtable[j] = hashtable[j - 1];
                        hashtable[j - 1] = tmp;
                    }
                }
                //If value @x-1 is empty and value @x is not empty, swap them
                else if ((hashtable[j-1] == null) && (hashtable[j] != null)){
                    hashtable[j-1] = hashtable[j];
                    hashtable[j] = null;
                }
            }
        }
    }

    //Print "printMax" most common colors in descending frequency.
    //If there are fewer unique colors than printMax, update printMax
    //Note this method only makes sense on a sorted array
    public void print(int printMax){

        Freq tmp;                                                               //Temporarily stores each freq to make print statement shorter
        if (printMax > numFullSpots) { printMax = numFullSpots; }               //If there aren't that many unique colors, stop after last unique color
        System.out.printf("\nPrinting %d most common colors:\n", printMax);

        for (int i = 0; i < printMax; i++){
            tmp = hashtable[i];
            if (tmp != null) {
                System.out.printf("[%d]: R = %d, G = %d, B = %d, Count = %d\n", i + 1, tmp.getR(), tmp.getG(), tmp.getB(), tmp.getCount());
            }
        }
        System.out.printf("\n");
    }

    // if n is prime, return n. If not... if y <= 0, look for next closest prime smaller than n.
    // If y > 0, look for closest prime larger than n
    protected int closePrime(int n, int y) {
        if ( isPrime(n) ) { return n; }
        else if ( y > 0 ) { return closePrime( n+1, 1); }
        else { return closePrime( n-1, 0); }
    }

    //Checks if a number "x" is prime, returns a boolean
    protected boolean isPrime(int x) {
        if (x <= 1) {return false;}
        else if (x == 2) {return true;}
        else if ( (x % 2) == 0 ) {return false;}
        for(int i = 3; i <= Math.sqrt(x); i += 2) { if (x % i == 0) { return false;} }
        return true;
    }

}
