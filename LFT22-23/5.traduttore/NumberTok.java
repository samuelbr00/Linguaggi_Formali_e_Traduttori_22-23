

public class NumberTok extends Token {
    public int n ;
    public NumberTok(int tag, int n) {
        super(tag);
        this.n = n;
    }
    public String toString(){
        return "<" + tag + ", " + n + ">";
    }
}