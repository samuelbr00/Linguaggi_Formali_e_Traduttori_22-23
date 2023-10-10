package esercizio3_2;

import java.io.BufferedReader;
import java.io.IOException;


public class Lexer {//espressione regolare che accetta(ma anche stringhe errate) la sintassi degli "tag" in java e genera i token
//ogni token corrisponde ad un frammento della sintassi java 
//+ tokenizza anche i commenti nella sintassi java

    public static int line = 1;
    private char peek = ' ';
    //metodo che si occupa di scorrere la stringa in input
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }
    //va alla prossima linea se legge i seguenti peek,viene incluso il metodoche  gestisece il caso in cui 
    //il peek =='/' ci sono piu token che iniziano con '/' .es. /* // /
    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r'  || peek == '/' ) {
            if (peek == '/') {
                readch(br);
                if (peek == '*') {
                    readch(br);
                    boolean closed = true;
                    while (closed && peek != (char) -1) {
                        if (peek == '*') {
                            readch(br);
                            if (peek == '/') {
                                closed = false;
                            }
                        }
                        if (peek == '\n') {
                            line++;
                        }
                        readch(br);
                    }
                    if (closed && peek == (char) -1) {
                        System.out.println("Il commento è stato chiuso in maniera errata.");
                        return null;
                    }
                } else if (peek == '/') {
                    while (peek != '\n' && peek != (char) -1) {
                        readch(br);
                    }
                } else {
                    return Token.div;
                }
            }
           
            if (peek == '\n') {line++;}
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;

            case '[':
                peek = ' ';
                return Token.lpq;

            case ']':
                peek = ' ';
                return Token.rpq;
            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                peek = ' ';
                return Token.div;
                
            case ';':
                peek = ' ';
                return Token.semicolon;

            case ',':
                peek = ' ';
                return Token.comma;
	
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }


            case '|':
                readch(br);
                if(peek == '|'){
                    peek = ' ';
                    return Word.or;
                }else{
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }

            case '<':
                readch(br);
                switch(peek){
                    case '>':
                        peek = ' ';
                        return Word.ne;
                    
                    case '=':
                        peek = ' ';
                        return Word.le;

                    default:
                        peek = ' ';
                        return Word.lt;
                }

            case '>':
                readch(br);
                switch(peek){
                    case '=':
                        peek = ' ';
                        return Word.ge;

                    default:
                        peek = ' ';
                        return Word.gt;
                }

            case '=':
                readch(br);
                if(peek == '='){
                    peek = ' ';
                    return Word.eq;
                }else{
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }

          
            case (char)-1:
                return new Token(Tag.EOF);

            default:
                if (Character.isLetter(peek) || peek == '_') {

                    String s = "";
                    while(Character.isLetter(peek) || peek == '_' || Character.isDigit(peek)){    // Nuova definizione di Identificatore
                        s = s + peek;
                        readch(br);
                    }
                    //accumula i caratteri finche non vede uno spazio che delimita un word
                    //poi viene confrontato con delle
                    //per correttezza dell'espressione regolare si doveva fare con gli switch

                    if(s.compareTo("assign") == 0){
                        return Word.assign;
                    }else if(s.compareTo("to") == 0){
                        return Word.to;
                    }else if(s.compareTo("conditional") == 0){
                        return Word.conditional;
                    }if(s.compareTo("option") == 0){
                        return Word.option;
                    }if(s.compareTo("do") == 0){
                        return Word.dotok;
                    }else if(s.compareTo("else") == 0){
                        return Word.elsetok;
                    }else if(s.compareTo("while") == 0){
                        return Word.whiletok;
                    }else if(s.compareTo("begin") == 0){
                        return Word.begin;
                    }else if(s.compareTo("end") == 0){
                        return Word.end;
                    }else if(s.compareTo("print") == 0){
                        return Word.print;
                    }else if(s.compareTo("read") == 0){
                        return Word.read;
                    }else{
                        return new Word(Tag.ID, s);
                    }
                 
                } else if (Character.isDigit(peek)) {

                    int sum = 0;
                    //sfrutto il numero di cifre per formare il numero , ogni volta che leggo un altro nummero
                    // moltiplico per 10 per far spazio ad una nuova cifra
                    //se è l ultima cifra prendera il posto delle unita
                    // mentre se c'è un altra dopo si spostera sulle diecine quando faro x10
                    while(Character.isDigit(peek) && !Character.isLetter(peek) && peek != '_'){
                        int n = (int) peek - 48;
                        sum = sum*10 + n;
                        readch(br);
                    }
                            
                    return new NumberTok(Tag.NUM, sum); // Converte int in String, 48 == 0 in ascii

                } else {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
    }
}

