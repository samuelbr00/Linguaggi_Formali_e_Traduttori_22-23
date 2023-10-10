import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {  // genera, se presenti, degli errori
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) { // verifica se il tag corrisponde al simbolo corrente
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }
   
    public void start() { // produzione di partenza
        expr();
        match(Tag.EOF);
    }

    private void expr() {
        term();
        exprp();
    }

    private void exprp() {  
        switch (look.tag) { // verifico tutti i possibili case (simboli) che posso avere
                            // per ogni case applico la procedura adatta
            case '+':
                match('+');
                term();
                exprp();
                break; 

            case'-':
                match('-');
                term();
                exprp();
                break; 

            default:    //caso eps
                break;
        }
    }

    private void term() {
        fact();
        termp();
    }

    private void termp() {
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;

            case '/':
                match('/');
                fact();
                termp();
                break;

            default:    //caso eps
                break;    
        }
    }

    private void fact() {
		switch (look.tag) {
			case Tag.NUM:       // riconosco un numero
				match(Tag.NUM);
				break;

			case Tag.ID:        // riconosco un identificatore
				match(Tag.ID);
				break;
			
			case'(':	        // richiamo expr fra parentesi
                match('(');
				expr();
				match(')');
                break;
		}
    }
}
    
