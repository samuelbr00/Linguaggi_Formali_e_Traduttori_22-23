package esercizio2_2;

public class NumberTok extends Token {
    int n =0 ;
    public NumberTok(int tag, int n) {
        super(tag);
        this.n = n;
    }
    public String toString(){
        return "<" + tag + ", " + n + ">";
    }
}