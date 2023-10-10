package esercizio3_1;

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
    void move() { // legge prossimo token
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {  // genera, se presenti, degli errori

        throw new Error("near line " + lex.line + ": " + s);
    } // genera, se presenti, degli errori

    void match(int t) { // verifica se il tag corrisponde al simbolo corrente
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }
   
    public void start() { // produzione di partenza
        // vedo il corpo della produzione
        expr(); // corpo della produzione formato da expr
        match(Tag.EOF); //e EOF
    }

    private void expr() { //produzione formata da testa expr e corpo term e exprp
        term(); //corpo produzione formato da term
        exprp(); //e da exprp
    }

    private void exprp() {  
        switch (look.tag) { // verifico tutti i possibili case (simboli) che posso avere
                            // per ogni case applico la procedura adatta
            case '+': //se sono nel caso '+'
                match('+'); //faccio il match di '+'
                term(); //produzione formata da term
                exprp();//produzione formata da exprp
                break; 

            case'-': //oppure se sono nel caso '-'
                match('-'); //faccio il match di '-'
                term(); //produzione formata da term
                exprp(); // e da exprp
                break; 

            default:  //caso eps per produzione vuota
                break;
        }
    }

    private void term() { //produzione con testa term e corpo formato da fact e termp
        fact(); //corpo produzione formato da fact
        termp();//e da termp
    }

    private void termp() {
        switch (look.tag) {
            case '*': //se sono nel caso '*'
                match('*'); //faccio il match di '*'
                fact(); //corpo della produzione formato da fact
                termp(); //e da termp
                break;

            case '/': //se sono nel caso '/'
                match('/'); //faccio il match di '/'
                fact(); //corpo della produzione formato da fact
                termp(); // e da termp
                break;

            default:    //caso epsilon
                break;    
        }
    }

    private void fact() {
		switch (look.tag) {
			case Tag.NUM:       // riconosco un numero
				match(Tag.NUM); // faccio match del  numero
				break;

			case Tag.ID:        // riconosco un identificatore
				match(Tag.ID); // faccio match dell'identif
				break;
			
			case'(':	        // richiamo expr fra parentesi
                match('(');
				expr(); //richiamo l'espressione expr tra le parentesi
				match(')');
                break;
		}
    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "3.parser/esercizio3_1/Input3_1.lft"; // input file
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
    
