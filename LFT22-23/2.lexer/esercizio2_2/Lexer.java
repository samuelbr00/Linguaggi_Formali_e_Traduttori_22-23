package esercizio2_2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexer { //espressione regolare che accetta(ma anche stringhe errate) la sintassi degli "tag" in java e genera i token
//ogni token corrisponde ad un frammento della sintassi java
//+ tokenizza anche i commenti nella sintassi java

    // analizzatore lessicale che genera i token appartenenti ad una determinata sintassi
    // ogni token corrisponde ad un frammento della sintass
    public static int line = 1;
    private char peek = ' '; // carattere corrente

    //metodo che si occupa di scorrere la stringa in input (leggere carattere successivo)
    //metodo che si occupa di scorrere la stringa in input
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }
    //va alla prossima linea o char se legge i seguenti peek
    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') {line++;}
            readch(br);
        }
        
        switch (peek) { //analizzo nello switch il carattere corrente peek
            case '!': //nel caso in cui è '!'
                peek = ' ';
                return Token.not;  //ritorno il token 'not' == !

            case '(':  //nel caso in cui è '('
                peek = ' ';
                return Token.lpt; // ritorno il token '('

            case ')':
                peek = ' ';
                return Token.rpt; // ritorno il token ')'

            case '[':
                peek = ' ';
                return Token.lpq; // ritorno il token '[' = lpq

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
                if (Character.isLetter(peek) || peek == '_') { //GESTIONE CARATTERI LETTERARI e carattere '_'
                    String s = ""; //sfrutto s come accumulatore
                    while(Character.isLetter(peek) || peek == '_' || Character.isDigit(peek)){// Nuova definizione di Identificatore
                        s = s + peek;//accumula i caratteri finche non vede uno spazio che delimita una word
                        readch(br);
                    }
                    
                    // confrontiamo s con le word della sintassi, se c'e' corrispondenza return esercizio2_1.Word, altrimenti crea un nuovo ID

                    if(s.compareTo("assign") == 0){ //se la stringa è 'assign'
                        return Word.assign; //ritorno la parola Word 'assign' che ho già nei tag
                    }else if(s.compareTo("to") == 0){ // se  è to
                        return Word.to; //ritorno la parola Word 'to' che ho già nei tag
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
                 
                } else if (Character.isDigit(peek)) { // gestione numeri

                    int sum = 0;
                    // devo convertire char in int
                    // uso n per ottenere il valore corrente (0 == 48 in ascii)
                    // moltiplico il totale x10 e aggiungo la cifra n
                    // leggo next char fin quando e' un numero
                    while(Character.isDigit(peek) && !Character.isLetter(peek) && peek != '_'){
                        int n = (int) peek - 48; // uso n per ottenere il valore corrente (0 == 48 in ascii)
                        sum = sum*10 + n; // moltiplico il totale x10 e aggiungo la cifra n
                        readch(br);
                    }
                    
                    return new NumberTok(Tag.NUM, sum); //ritorno un nuovo NumberTok con il Tag Num e il valore sum che calcolo

                } else {
                        System.err.println("Erroneous character: " 
                                + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "2.lexer/esercizio2_2/Input2_2.lft"; // input file
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}