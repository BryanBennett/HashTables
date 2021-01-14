/*
Name: Bryan Bennett
Course: CSC201B
Date: 10/27/20
Program Description: This class stores one RGB "pixel", which is read from the file specified.
"getRGB" is a unique method that returns one integer unique to each pixel, which is done using bitshifting.
 */

public class RGB {

    //Variable declarations
    protected int r;
    protected int g;
    protected int b;
    protected int rgb;                              //One integer unique to each color, created using bitshifting

    //Constructors
    public RGB(){
        this.r = 0;
        this.g = 0;
        this.b = 0;
    }
    public RGB(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    //Methods
    public int getR(){ return r; }
    public int getG(){ return g; }
    public int getB(){ return b; }

    //This is the key we are using, sum of R G and B values. *NOTE this can be the same for different colors, as it is a sum
    public int getKey(){return (r+g+b);}

    //Returns an integer unique to each color. Used for comparing pixels. R is third least significant byte, G is second, B is least sig byte.
    //Most significant byte (integer is 4 bytes) is zeros.
    public int getRGB() {
        int result = 0;
        result |= ((this.r & 0xFF) << 16);
        result |= ((this.g & 0xFF) << 8);
        result |= (this.b & 0xFF);
        result &= (0x00FFFFFF);
        this.rgb = result;
        return rgb;
    }

}
