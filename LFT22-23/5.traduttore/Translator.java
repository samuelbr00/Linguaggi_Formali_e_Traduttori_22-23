// javac Traduttore.java
// java Traduttore
// java -jar jasmin.jar Output.j
// java Output
import java.io.*;
public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);//stampo token
    }

    void error(String s) {  // genera, se presenti, degli errori
        throw new Error("near line " + lex.line + ": " + s);
    } //stampo gli errori

    void match(int t) { // verifica se il tag corrisponde al simbolo corrente
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();//se il tag non è uguale a 'EOF' mi sposto
        } else error("syntax error");//altrimenti errore
    }

    public void prog() {
        statlist();
        match(Tag.EOF);
        try {
            code.toJasmin();
        } catch (java.io.IOException e) {
            System.out.println("IO error\n");
        }
    }

    private void statlist() {//mi permette di raggruppare un blocco di istruzioni
        stat();//vado in stat per vedere se c'è istruzione
        statlistp();//vado in statlistp per vedere se termino istruzione
        // o se il codice è completo
    }

    private void statlistp() {
        switch (look.tag) {//valuto il tag
            case ';'://se il tag è ';'
                match(';');//faccio il match del tag ';'
                stat();//vado in stat per vedere se c'è istruzione
                statlist();//vado in statlist per eseguire blocco istruzioni
                break;

            case Tag.EOF://se il tag è 'eof' mi fermo
                break;
        }
    }
    private void stat() {
        switch (look.tag) {//valuto il valore che può avere il tag
            case Tag.ASSIGN://se ho il tag Assign (assegnamento)
                match(Tag.ASSIGN);//faccio il match del tag Assign
                expr();//valore dell'espressione da assegnare a elementi di idlist
                match(Tag.TO);//tag To (indica a chi assegnare il valore di expr)
                idlist(Tag.ASSIGN);//identificatore/i a cui viene assegnato il valore in expr
                code.emit(OpCode.pop);  // per come abbiamo implementato assign,
                // duplichiamo una volta in piu' la cima, quindi in fine facciamo una pop (cancellazione)
                break;

            case Tag.PRINT://se ho il tag Print
                match(Tag.PRINT);//faccio il match del tag Print
                match('[');//faccio il match di '['
                exprlist(OpCode.invokestatic);  // invoca metodo print (definito nello header)
                match(']');//faccio il match di ']'
                break;

            case Tag.READ://se ho il tag Read
                match(Tag.READ);//faccio il match del tag
                match('[');//faccio il match di '['
                idlist(Tag.READ);//identificatore/i di cui vogliamo leggere il valore
                match(']');//faccio il match di ']'
                break;

            case Tag.WHILE: {//se ho il tag While
                // crea le label
                int wtrue = code.newLabel();
                int wfalse = code.newLabel();
                int wstart = code.newLabel();

                match(Tag.WHILE);//faccio il match del tag
                match('(');//faccio il match di '('
                code.emitLabel(wstart); // label viene emessa nel codice Output.j
                bexpr(wtrue, wfalse);//espressione booleana che permette l'esecuzione del while
                code.emitLabel(wtrue);//label
                match(')');//faccio il match di ')'
                stat();//istruzione o sequenza di istruzioni da eseguire nel while
                code.emit(OpCode.GOto, wstart);//tramite emit aggiungo una nuova istruzione con opCode di GOto e operando di wstart
                code.emitLabel(wfalse); // label uscita dal ciclo
                break;
            }

            case Tag.COND://se ho il tag Cond
                int iTrue = code.newLabel();
                int False = code.newLabel();
                int iEnd = code.newLabel(); //dopo l'else

                match(Tag.COND);//faccio il match del tag

                match('[');//faccio il match di '['
                optlist(iEnd);//gestisco le option
                match(']');//faccio il match di ']'

                doubleCond();//gestisco l'else
                code.emitLabel(iEnd);//tramite emitLabel richiamo emit che aggiunge una nuova istruzione
                //con un opcode e l'operando di 'iEnd'
                match(Tag.END);//faccio il match del tag 'End'
                break;

            case '{':
                match('{');//faccio il match del tag '{'
                statlist();//eseguo sequenza di istruzioni
                match('}');//faccio il match del tag ']'
                break;
        }
    }
    private void doubleCond() {// produzione che rende la grammatica LL(1) non fattorizzabile
        switch (look.tag) {//valuto il tag
            case Tag.ELSE://se il tag è 'else'
                match(Tag.ELSE);//faccio il match del tag 'else'
                stat();//eseguo l'istruzione dopo l'else
                break;

            case Tag.END://se il tag è 'end' mi fermo
                break;
        }
    }
    private void idlist(int i) {// usiamo i per distinguere read da assign
        if (look.tag == Tag.ID) {// controlla se una variabile esiste nella Symbol Table
            int id_addr = st.lookupAddress(((Word) look).lexeme);// indirizzo di Tag.ID
            if (id_addr == -1) {//se id_addr non esiste (==-1)
                id_addr = count;
                st.insert(((Word) look).lexeme, count++);       // altrimenti la inserisce
            }
            match(Tag.ID);

            if (Tag.READ == i) {
                code.emit(OpCode.invokestatic, 0);
                code.emit(OpCode.istore, id_addr);
            }

            if (Tag.ASSIGN == i) {//assign multiplo
                code.emit(OpCode.dup);// caso in cui ci sono piu variabili a cui assegnare un valore
                // nel caso di assegnazione singola viene fatto il pop dallo stack del duplicato
                code.emit(OpCode.istore, id_addr);
            }
            idlistp(i);
        }
    }
    private void idlistp(int i) {
        switch (look.tag) {//valuto il tag
            case ','://se il tag è ','
                match(',');//faccio il match del tag ','
                idlist(i);
                break;
            default:
                break;
        }

    }
    private void optlist(int lafter_else){
        switch(look.tag){
            case Tag.OPTION:
                int next_optitem=code.newLabel();
                optitem(next_optitem, lafter_else);
                code.emitLabel(next_optitem);
                optlistp(lafter_else);
                break;
            default:
                break;
        }
    }
    private void optlistp(int lafter_else){
        switch(look.tag){
            case Tag.OPTION:
                int next_optitem=code.newLabel();
                optitem(next_optitem, lafter_else);
                code.emitLabel(next_optitem);
                optlistp(lafter_else);
                break;

            default:
                break;
        }
    }
    private void optitem(int next, int lafter_else){
        switch(look.tag){
            case Tag.OPTION:
                match(Tag.OPTION);
                match('(');
                int lcontinue=code.newLabel();
                bexpr(lcontinue, next);
                code.emitLabel(lcontinue);
                match(')');

                match(Tag.DO);
                stat();
                code.emit(OpCode.GOto, lafter_else);
                break;
            default:
                break;
        }
    }
    private void bexpr(int bexprtrue, int bexprnext) {  // boolean expression
        switch (look.tag) {
            case Tag.RELOP: {
                String rel = ((Word) look).lexeme; //sfruttiamo la struttura Word che ha la lexeme di tipo String
                match(Tag.RELOP);
                expr();
                expr();
                switch (rel) {  // switch sugli operatori relazionali
                    case ">":
                        code.emit(OpCode.if_icmpgt, bexprtrue); // per ogni rel viene emesso opcode
                        break;
                    case "<":
                        code.emit(OpCode.if_icmplt, bexprtrue);
                        break;
                    case "==":
                        code.emit(OpCode.if_icmpeq, bexprtrue);
                        break;
                    case ">=":
                        code.emit(OpCode.if_icmpge, bexprtrue);
                        break;
                    case "<=":
                        code.emit(OpCode.if_icmple, bexprtrue);
                        break;
                    case "<>":
                        code.emit(OpCode.if_icmpne, bexprtrue);
                        break;
                }
            }
            code.emit(OpCode.GOto, bexprnext);
            break;
        }
    }
    private void expr() {
        switch (look.tag) {//valuto il valore che può avere il tag
            case '+'://se sono nel caso '+'
                match('+');//faccio il match di '+'
                match('(');//faccio il match di '('
                exprlist(OpCode.iadd);
                match(')');//faccio il match di ')'
                break;

            case '-'://se sono nel caso '-'
                match('-');//faccio il match di '-'
                expr();
                expr();
                code.emit(OpCode.isub);
                break;

            case '/'://se sono nel caso '/'
                match('/');//faccio il match di '/'
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;

            case '*'://se sono nel caso '*'
                match('*');//faccio il match di '*'
                match('(');
                exprlist(OpCode.imul);
                match(')');
                break;

            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok) look).n);
                match(Tag.NUM);
                break;

            case Tag.ID://valuto tag ID

                if(st.lookupAddress(((Word) look).lexeme)==-1)//se non esiste (==-1)
                {
                    error("Identificatore non inizializzato");//errore:non inizializzato
                }

                code.emit(OpCode.iload, st.lookupAddress(((Word) look).lexeme));//altrimenti
                match(Tag.ID);
                break;
        }
    }
    private void exprlist(OpCode opcode) {  // print risultati espressioni
        switch (look.tag) {
            case '+'://se sono nel caso '+'
                expr();
                if (opcode == OpCode.invokestatic) {    // opcode == print-read
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(opcode);
                break;

            case '-'://se sono nel caso '-'
                expr();
                if (opcode == OpCode.invokestatic) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(opcode);
                break;

            case '*'://se sono nel caso '*'
                expr();
                if (opcode == OpCode.invokestatic) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(opcode);
                break;

            case '/'://se sono nel caso '/'
                expr();
                if (opcode == OpCode.invokestatic) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(opcode);
                break;

            case Tag.NUM:
                expr();
                if (opcode == OpCode.invokestatic) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(opcode);
                break;

            case Tag.ID:
                expr();
                if (opcode == OpCode.invokestatic) {
                    code.emit(OpCode.invokestatic, 1);
                }
                exprlistp(opcode);
                break;

            default:
                error("exprlist error");
                break;
        }
    }
    public void exprlistp(OpCode opcode) {
        switch (look.tag) {//valuto il valore che può avere il tag
            case ','://se sono nel caso ','
                match(',');//faccio il match di ','
                expr();//valuto una nuova espressione chiamando expr
                if (opcode == OpCode.invokestatic) {
                    code.emit(OpCode.invokestatic, 1);
                } else {
                    code.emit(opcode);
                }
                exprlistp(opcode);
                break;

            default:
                break;
        }
    }


    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "5.traduttore/esempi/input2.lft"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
