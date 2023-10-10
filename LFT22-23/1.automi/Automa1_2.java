/* Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
in un linguaggio in stile Java: un identificatore e una sequenza non vuota di lettere, numeri, ed il `
simbolo di “underscore” _ che non comincia con un numero e che non puo essere composto solo `
dal simbolo _. Compilare e testare il suo funzionamento su un insieme significativo di esempi.
Esempi di stringhe accettate: “x”, “flag1”, “x2y2”, “x 1”, “lft_lab”, “ temp”, “x 1 y 2”,“x ”, “ 5”
Esempi di stringhe non accettate: “5”, “221B”, “123”, “9 to 5”, “ ”  */
public class Automa1_2 {

    public static boolean scan(String s){

            int state = 0; // inizializzo stato a 0

            for (int i = 0; i < s.length(); i++){ //analizzo la stringa che prendo in input
                char ch = s.charAt(i); //mi sposto sul carattere corrente da analizzare
                switch (state) {
                    case 0: // stato == 0
                        if (ch >= '0' && ch <= '9'){ //se il carattere è maggiore-uguale di 0 e minore-uguale di 9 setto state a 1
                            state = 1;
                        }
                        else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){ //se il carattere è maggiore-uguale di 'a' o 'A' in ASCII
                            state = 3; // stato == 3
                        }
                        else if (ch == '_'){ //se il carattere è '_' setto state a 2
                            state = 2 ;
                        }
                        else{
                            state = -1;  //altrimento setto state a -1
                        }
                        break;

                    case 1: //caso in cui state è 1
                        if ((ch >= '0' && ch <= '9') || //se il carattere è maggiore-uguale di '0' e minore-uguale di '9'
                                (ch >= 'a' && ch <= 'z') ||//se il carattere è maggiore-uguale di 'a' in ASCII e minore-uguale di 'z' in ASCII o
                                (ch >= 'A' && ch <= 'Z') || //se il carattere è maggiore-uguale di 'A' in ASCII e minore-uguale di 'Z' in ASCII o
                                (ch >= 'A' && ch <= 'Z') ||
                                (ch == '_')){ //se il carattere corrisponde a '_'
                         state = 1 ; // stato == 1
                        }
                        else{
                            state = -1; //altrimento setto state a -1
                        }
                        break;

                    case 2: // stato == 2
                        if (ch == '_'){ // se leggo _ stato == 2
                            state = 2 ;
                        }
                        if ((ch >= '0' && ch <= '9') || // tra 0 e 9
                                (ch >= 'a' && ch <= 'z') || // a e z in ASCII
                                (ch >= 'A' && ch <= 'Z')){ // A e Z in ACII
                           state = 3  ; // stato == 3
                        }
                        else{
                            state = -1; // altrimenti -1
                        }
                        break;

                    case 3: //stato == 3
                        if ((ch >= '0' && ch <= '9') || // 0 e 9
                                (ch >= 'a' && ch <= 'z') || // a e z in ASCII
                                (ch >= 'A' && ch <= 'Z') || // A e Z in ASCII
                                (ch == '_')){ //   SE è _
                         state = 3 ; // stato == 3
                        }
                        else{
                            state = -1; } // altrimenti -1

                        break;
                }
            }

        return state == 3; // ritorna true se la stringa che leggo va in statp uguale a 3 , false altrimenti
    }
    public static void main (String [] args){
            System.out.println(scan("flag1") ? "OK" : "NOPE");
        }
}


