/*
Name: Bryan Bennett
Course: CSC201B
Date: 10/27/20
Program Description: "Freq" is effectively an RGB combination that also stores the frequency this color appears in the image.
This is useful for frequency analysis, as the top 256 most frequent colors are printed from main.
 */

public class Freq extends RGB {

    //Variable declarations
    private int count;

    //Constructor
    public Freq(RGB pixel){
        super(pixel.getR(), pixel.getG(), pixel.getB());
        this.count = 1;
    }

    //Methods
    public void incrementCount(){ this.count++; }
    public int getCount(){ return this.count; }

}
