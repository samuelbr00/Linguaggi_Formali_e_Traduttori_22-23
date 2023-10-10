package esercizio2_3;

import java.io.*;


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
        //quando il carattere è ' ' o '/t' o '/n' o '' o '/r' o '/'
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r'  || peek == '/' ) {
            if (peek == '/') { //se peek corrisponde a '/'
                readch(br); ;//scorro
                if (peek == '*') { //se peek corrisponde a '*'
                    readch(br); ;//scorro
                    boolean closed = true; //creo e setto la variabile boolean closed a true
                    while (closed && peek != (char) -1) { //finchè ho "/*" e non ho EOF
                        if (peek == '*') { //se peek corrisponde a '*'
                            readch(br); //scorro
                            if (peek == '/') { //se peek corrisponde a '/'
                                closed = false; //setto closed a false
                            }
                        }
                        if (peek == '\n') { //se peek corrisponde a '/n
                            line++; //creo nuova linea
                        }
                        readch(br); //scorro
                    } //raggiungo la situazione in cui ho anche "...*/"
                    if (closed && peek == (char) -1) { //se ho closed e anche EOF il commento è chiuso in maniera sbagliata
                        System.out.println("Il commento è stato chiuso in maniera errata."); //commento chiuso in maniera errata
                        return null; //ritorno null se c'è errore
                    }
                } else if (peek == '/') { //se peek corrisponde a '/'
                    while (peek != '\n' && peek != (char) -1) { //se peek non è '/n' e nemmeno EOF
                        readch(br); //scorro
                    }
                } else {
                    return Token.div; //altrimenti torna un nuovo token div
                }
            }
           
            if (peek == '\n') {line++;} //se peek corrisponde a '/n' aumento la righe
            readch(br);
        }

        switch (peek) {  //analizzo nello switch il carattere corrente peek
            case '!': // se è '!'
                peek = ' ';
                return Token.not; // ritorno token.not == !

            case '(': // se è '('
                peek = ' ';
                return Token.lpt; //ritorno toke.lpt == (

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

                    // confrontiamo s con le word della sintassi, se c'e' corrispondenza return esercizio2_1.Word, altrimenti crea un nuovo ID

                    if(s.compareTo("assign") == 0){ //se la stringa è 'assign'
                        return Word.assign; //ritorno la parola Word 'assign' che ho già nei tag
                    }else if(s.compareTo("to") == 0){ //se la stringa è 'to'
                        return Word.to; //ritorno la parola Word 'to' che ho già nei tag
                    }else if(s.compareTo("conditional") == 0){ //se la stringa è 'conditional'
                        return Word.conditional; //ritorno la parola Word 'conditional' che ho già nei tag
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
                        return new Word(esercizio2_2.Tag.ID, s);
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
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "2.lexer/esercizio2_3/Input2_3.lft"; // input file
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

