package esercizio3_2;

import java.io.*;
public class Parser {
    // analizzatore sintattico che controlla se la successione di token, 
    // generata dal lexer, rispetta la grammatica implementata.
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    public static String s;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {   // legge prossimo token
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {// genera, se presenti, degli errori
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {// verifica se il tag corrisponde al simbolo corrente
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }
   
    public void prog(){ // produzione di partenza
        statlist(); //corpo della produzione formato da statlist
        match(Tag.EOF); // e dal tag EOF
    }

    private void statlist(){ //produzione con testa statlist
        stat(); //e corpo formato da da stat
        statlistp(); //e da statlistp
    }


    private void statlistp(){ //produzione con testa statlistp
        switch (look.tag) {   //guardo il tag che ha nel corpo della produzione
            case ';': // il tag in questione è ';'
                match(';'); // faccio il match di ';'
                stat(); //continuo a guardare il corpo della produzione formato da stat
                statlist(); //e da statlist
                break;

            default:
                //caso eps   //caso eps perché la produzione può essere anche nulla
                break;
        }
    }
    //caso degli identificatori della sintassi simil-java
    private void stat(){//testa della produzione
        switch (look.tag){  // ogni case (tag) ci suggerisce come implementare la produzione
                            // per ogni tag richiamiamo una procedura adatta, dettata dalla produzione
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist();
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist();
                match(']');
                break;

            case Tag.READ:
                match(Tag.READ);
                match('[');
                idlist();
                match(']');
                break;

            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat();
                break;

            case Tag.COND:
                match(Tag.COND);
                match('[');
                optlist();
                match(']');
                stat();
                statCond();
                break;

            case Tag.OPTION:
                match(Tag.OPTION);
                match('(');
                bexpr();
                match(')');
                match(Tag.DO);
                stat();
                break;

            case '{':
                match('{');
                statlist(); 
                match('}');
            break; 
        } 
    }

    private void statCond(){  // metodo aggiuntivo per rimozione fattorizzazione
        switch(look.tag){
            case Tag.END:
                match(Tag.END);    
                break;        
            case Tag.ELSE:
                match(Tag.ELSE);
                stat();
                match(Tag.END);
                break;
        }
    }

    private void idlist(){
        match(Tag.ID);
        idlistp();
    }

    private void idlistp(){
        switch(look.tag){
            case ',': 
                match(',');
                match(Tag.ID);
                idlistp();
                break;

            default://caso eps
                break;
           }
    }

    private void bexpr(){
        match(Tag.RELOP);
        expr();
        expr();
    }

    private void expr(){
        switch (look.tag) { 
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;

             case '-':
                match('-');
                expr();
                expr();
                break;

             case '/':
                match('/');
                expr();
                expr();
                break;

             case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            
             case Tag.NUM:     //caso numero
                match(Tag.NUM);
                break;
            
             case Tag.ID:      //caso identificaore
                match(Tag.ID);
                break;
        }
    }

    private void exprlist(){
        expr();
        exprlistp();
    }

    public void exprlistp(){
        switch (look.tag) {
            case ',':
                match(',');
                expr();
                exprlist();
                break;
        
            default://caso eps
                break;
        }
    }

    public void optlist(){
        optitem();
        optlistp();
    }

    public void optlistp() {
        optitem();
        optlist();
    }

    public void optitem() {
        switch (look.tag) {  // ogni case (tag) ci suggerisce come implementare la produzione
            // per ogni tag richiamiamo una procedura adatta, dettata dalla produzione
            case Tag.OPTION:
                match(Tag.OPTION);
                bexpr();
                match(Tag.DO);
                stat();
                break;
        }
    }



    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "3.parser/esercizio3_2/Input3_2.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
    
