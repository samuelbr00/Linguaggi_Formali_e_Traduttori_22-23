package valutatore4_1;

import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) { 
        lex = l; 
        pbr = br;
        move(); 
    }
   
    void move() { 
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) { 
        throw new Error("near line " + lex.line + ": " + s);
    } // genera, se presenti, degli errori

    void match(int t) { // verifica se il tag corrisponde al simbolo corrente
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    public void start() {  // produzione di partenza
        int expr_val;
        expr_val = expr();
        match(Tag.EOF);
        System.out.println(expr_val);   // stampa valore finale (risultato)
    }

    private int expr() {        // non ha attributi ereditati, 
        int term_val, exprp_val;// ha un attributo sintetizzato(return int)
        term_val = term();      // invoca metodo term che ha un attributo sintetizzato
        exprp_val = exprp(term_val);// exprp ha un attributo sintetizzato, attributo ereditato == term.val
        return exprp_val;       // expr.val = exprp.val
    }

    private int exprp(int exprp_i) {    // switch sui vari case
        int term_val, exprp_val;
        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();  // attributo sintetizzato di term
                exprp_val = exprp(exprp_i + term_val);// attributo sintetizzato di expr
                break;  // attributo ereditato equivalente a (exprp.i + term.val)

            case '-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;

            default:  //caso eps
                exprp_val = exprp_i;
        }
        return exprp_val;   // attributo sintetizzato di exprp.val
    }

    private int term() { 
        int fact_val, termp_val;
        fact_val = fact();
        termp_val = termp(fact_val);
        return termp_val;
    }
    
    private int termp(int termp_i) { 
        int fact_val, termp_val;
        switch(look.tag){
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;

            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                break;
            default:
                termp_val = termp_i;
        }
        return termp_val;
    }
    
    private int fact() {
        int fact_val = 0; 
        switch(look.tag){
        case '(':
            match('(');
            fact_val = expr();
            match(')');
            break;

        case Tag.NUM:
            fact_val = ((NumberTok) look).n;
            match(Tag.NUM);
            break;

        default:
            error("ERROR FACT()");
        }
        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "4.valutatore/valutatore4_1/Input4_1.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
