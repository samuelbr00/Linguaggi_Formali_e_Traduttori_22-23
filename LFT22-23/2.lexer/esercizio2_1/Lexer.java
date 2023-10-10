package esercizio2_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lexer { //espressione regolare che accetta(ma anche stringhe errate) la sintassi degli "tag" in java e genera i token
//ogni token corrisponde ad un frammento della sintassi java
//+ tokenizza anche i commenti nella sintassi java

    // analizzatore lessicale che genera i token appartenenti ad una determinata sintassi
                    // ogni token corrisponde ad un frammento della sintassi java 

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
    //EEK  che restituisce il valore memorizzato nella cella di memoria specificata
    //va alla prossima linea se legge i seguenti peek,viene incluso il metodoche  gestisece il caso in cui
    //il peek =='/' ci sono piu token che iniziano con '/' .es. /* // /
    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') {line++;}
            readch(br);
        }
        
        switch (peek) { //analizzo nello switch il carattere corrente peek
            case '!': //nel caso in cui è '!'
                peek = ' ';
                return Token.not; //ritorno il token 'not' che corrisp a !

            case '(': //nel caso in cui è '('
                peek = ' ';
                return Token.lpt; // ritorno token lpt che corrisponde a parentesi tonda aperta

            case ')':
                peek = ' ';
                return Token.rpt; // ritorno rpt che corrisp a tonda chiusa

            case '[':
                peek = ' ';
                return Token.lpq; // lpq quadra aperta

            case ']':
                peek = ' ';
                return Token.rpq; // quadra chiusa rqp
            case '{':
                peek = ' ';
                return Token.lpg; // graffa aperta lpg

            case '}':
                peek = ' ';
                return Token.rpg; // graffa chiusa rpg

            case '+':
                peek = ' ';
                return Token.plus; // plus == +

            case '-':
                peek = ' ';
                return Token.minus; //minus == -

            case '*':
                peek = ' ';
                return Token.mult; // mult == *

            case '/':
                peek = ' ';
                return Token.div; // div == /
                
            case ';':
                peek = ' ';
                return Token.semicolon; // ; == semicolon

            case ',':
                peek = ' ';
                return Token.comma; // virgola == comma


	
            case '&': //nel caso in cui è '&'
                readch(br);
                if (peek == '&') { //se peek è seguito anche da un'altra "&"
                    peek = ' ';
                    return Word.and; //ritorno il token 'and'
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek ); //altrimenti stampo errore
                    return null;
                }
                
            case '|': //nel caso in cui è '|'
                readch(br);
                if(peek == '|'){ //nel caso in cui è '|'
                    peek = ' ';
                    return Word.or; //ritorno il token 'or'
                }else{
                    System.err.println("Erroneous character" //altrimenti stampo errore
                            + " after & : "  + peek );
                    return null;
                }

            case '<': //nel caso in cui è '<'
                readch(br);
                switch(peek){ //analizzo il secondo carattere di peek, se c'è
                    case '>': //se peek2 è seguito anche da un altro ">"
                        peek = ' ';
                        return Word.ne; //ritorno il token 'ne' ne == <>
                    
                    case '=': //se peek2 è seguito anche da "="
                        peek = ' ';
                        return Word.le; //ritorno il token 'le'  le == <=

                    default: // se non c'è un secondo carattere
                        peek = ' ';
                        return Word.lt; //ritorno il token 'lt' lt == less than
                }

            case '>': // stessa cosa di prima
                readch(br);
                switch(peek){
                    case '=': // >= ge
                        peek = ' ';
                        return Word.ge;

                    default:
                        peek = ' ';
                        return Word.gt; // > == gt
                }

            case '=': // uguale
                readch(br);
                if(peek == '='){ // == uguale
                    peek = ' ';
                    return Word.eq;
                }else{
                    System.err.println("Erroneous character" // altrimenti erroe
                            + " after & : "  + peek );
                    return null;
                }
         
            case (char)-1:
                return new Token(Tag.EOF); //ritorno il tag che termina

            default: //se non ho segni, parentesi, operatori ecc. gestisco i caratteri
                if (Character.isLetter(peek)) { //GESTIONE CARATTERI LETTERARI
                    String s = ""; //sfrutto s come accumulatore
                    while(Character.isLetter(peek) || peek == '_' || Character.isDigit(peek)){// Nuova definizione di Identificatore
                        s = s + peek;//accumula i caratteri finche non vede uno spazio che delimita una word
                        readch(br);
                    }
                    // confrontiamo s con le word della sintassi, se c'e' corrispondenza
                    // return esercizio2_1.Word, altrimenti crea un nuovo ID

                    //accumula i caratteri finche non vede uno spazio che delimita un word
                    //poi viene confrontato con delle
                    //per correttezza dell'espressione regolare si doveva fare con gli switch
                    // confrontiamo s con le word della sintassi, se c'e' corrispondenza
                    // return esercizio2_1.Word, altrimenti crea un nuovo ID

                    if(s.compareTo("assign") == 0){ //se la stringa è 'assign'
                        return Word.assign; //ritorno la parola Word 'assign' che ho già nei tag
                    }else if(s.compareTo("to") == 0){  //se la stringa è 'to'
                        return Word.to; //ritorno la parola Word 'to' che ho già nei tag
                    }else if(s.compareTo("conditional") == 0){ //se la stringa è 'conditional'
                        return Word.conditional; //ritorno la parola Word 'conditional' che ho già nei tag
                    }if(s.compareTo("option") == 0){ // se option
                        return Word.option; // option
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
                        return Word.read; //ritorno la parola Word 'read' che ho già nei tag
                    }else{
                        return new Word(Tag.ID, s); //altrimenti ritorno una nuova Word con un tag Id e la stringa s che ho analizzato
                    }
                 
                } else if (Character.isDigit(peek)) { // GESTIONE NUMERI

                    int sum = 0; //inizializzo accumulatore sum a 0
                    //se il carattere analizzato è un numero e non è una lettera
                    //ogni volta che ricevo un char gli togli il char 0 (... - ' 0 ') e ottengo il suo valore numerico
                    //che aggiungo al num temporaneo della lettura
                    // (per aggiungere scannerizzato  devo moltiplicare per 10 il n temporaneo e poi aggiungo quello scannerizzato cioe n  )
                    while(Character.isDigit(peek) && !Character.isLetter(peek)){
                        int n = (int) peek - 48; // uso n per ottenere il valore corrente (0 == 48 in ASCII)
                        sum = sum*10 + n; // moltiplico il totale x 10 e aggiungo la cifra n
                        readch(br);

                    }
                    
                    return new NumberTok(Tag.NUM, sum); //ritorno un nuovo NumberTok con il Tag Num e il valore sum che calcolo

                } else {
                        System.err.println("Erroneous character: "
                                + peek ); //altrimenti stampo errore
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "2.lexer/esercizio2_1/Input2_1.lft"; // input file
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