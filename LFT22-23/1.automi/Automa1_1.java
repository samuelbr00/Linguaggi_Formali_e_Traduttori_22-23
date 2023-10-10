/* Esercizio 1.1. Copiare il codice in Listing 1, compilarlo e testarlo su un insieme significativo di
stringhe, per es. “010101”, “1100011001”, “10214”, ecc.
Come deve essere modificato il DFA in Figure 1 per riconoscere il linguaggio complementare,
ovvero il linguaggio delle stringhe di 0 e 1 che non contengono 3 zeri consecutivi? Progettare e
implementare il DFA modificato, e testare il suo funzionamento. */
public class Automa1_1{

    public static boolean scan(String s){
        int state = 0 ;


        //Analizzo la stringa che prendo in input
        for (int i = 0; i < s.length(); i++){
            //mi sposto sul carattere corrente da analizzare
            final char ch = s.charAt(i++);


            switch (state) {
                case 0: // caso in cui carattere == 0 , setto stato in 1
                    if (ch == '0'){
                        state = 1; }
                    else if (ch == '1'){// caso in cui carattere == 1 , setto stato in 0
                        state = 0 ;
                    }
                    else{
                        state = -1; } //altrimenti setto state a -1
                    break;

                case 1: //caso in cui state è 1
                    if (ch == '0') { //se il carattere è '0' setto state a 2

                        state = 2;
                    }
                    else if (ch == '1'){ ;//se il carattere è '1' setto state a 0

                        state = 0 ;
                    }
                    else
                    {
                        state = -1; // altrimenti -1
                    }
                    break;

                case 2: // stato  == 2
                    if (ch == '0'){ // se leggo 0 vado in stato 3

                        state = 3;
                    }
                    else if (ch == '1'){ // se leggo 1 vado in stato 0

                        state = 0;
                    }
                    else{

                        state = -1; // altrimenti -1
                    }
                    break;

                case 3: // stato 3
                    if (ch == '0' || ch == '1'){ // se leggo 0 oppure 1 stato == 3
                        state = 3;
                    }
                    else{

                        state = -1; // altrimenti -1
                    }
                    break;
            }
        }

        return state == 0; // true se la striga che leggo arriva in stato = 0 altrimenti false
    }

    public static void main (String[] args) {
        System.out.println(scan("010101") ? "OK" : "NOPE");
    }

}