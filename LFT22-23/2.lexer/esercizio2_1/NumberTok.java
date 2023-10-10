package esercizio2_1;

public class NumberTok extends Token {

    //  Classe che ti permette di creare un token
    //  passando come stringa un numero
    //  token che contiene numeri
    int n =0 ;
    public NumberTok(int tag, int n) {
        super(tag);
        this.n = n;
    }
    public String toString(){
        return "<" + tag + ", " + n + ">";
    }
}