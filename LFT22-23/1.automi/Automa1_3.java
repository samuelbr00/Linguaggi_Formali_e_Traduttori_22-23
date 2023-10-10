/*  Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
contengono un numero di matricola seguito (subito) da un cognome, dove la combinazione di
matricola e cognome corrisponde a studenti del turno 2 o del turno 3 del laboratorio di Linguaggi
Formali e Traduttori. Si ricorda le regole per suddivisione di studenti in turni:
• Turno T1: cognomi la cui iniziale e compresa tra A e K, e il numero di matricola ` e dispari; `
• Turno T2: cognomi la cui iniziale e compresa tra A e K, e il numero di matricola ` e pari; `
• Turno T3: cognomi la cui iniziale e compresa tra L e Z, e il numero di matricola ` e dispari; `
• Turno T4: cognomi la cui iniziale e compresa tra L e Z, e il numero di matricola ` e pari. `
Per esempio, “123456Bianchi” e “654321Rossi” sono stringhe del linguaggio, mentre
“654321Bianchi” e “123456Rossi” no. Nel contesto di questo esercizio, un numero di matricola non
ha un numero prestabilito di cifre (ma deve essere composto di almeno una cifra). Un
cognome corrisponde a una sequenza di lettere, e deve essere composto di almeno una lettera.
Quindi l’automa deve accettare le stringhe “2Bianchi” e “122B” ma non “654322” e “Rossi”.
 */
public class Automa1_3 {

    public static boolean scan(String s){

            int state = 0;

            for(int i = 0; i < s.length(); i++){

                char ch = s.charAt(i);

                switch (state) {
                    case 0:
                        if ((int) ch % 2 == 0){

                            state = 1 ;
                        }
                        else if ((int) ch % 2 == 1){

                            state = 2 ;
                        }
                        else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){

                            state = 4 ;
                        }
                        else{

                            state = -1;
                        }
                        break;

                    case 1:
                        if ((ch >= 'A' && ch <= 'K') || (ch >= 'a' && ch <= 'k')){
                            state = 3 ;
                        }
                        else if ((ch >= 'L' && ch <= 'Z') || (ch >= 'l' && ch <= 'z')){

                            state = 4 ;
                        }
                        else if ((int) ch % 2 == 0){

                            state = 1 ;
                        }
                        else if ((int) ch % 2 == 1){

                            state = 2 ;
                        }
                        else{

                            state = -1;
                        }
                        break;

                    case 2:
                        if ((ch >= 'A' && ch <= 'K') || (ch >= 'a' && ch <= 'k')){

                            state = 4 ;
                        }
                        else if ((ch >= 'L' && ch <= 'Z') || (ch >= 'l' && ch <= 'z')){

                            state = 3 ;
                        }
                        else if ((int) ch % 2 == 0){

                            state = 1 ;
                        }
                        else if ((int) ch % 2 == 1){

                            state = 2;
                        }
                        else{

                            state = -1;
                        }
                        break;

                    case 3:
                        if (ch >= '0' && ch <= '9'){

                          state = 4 ;
                        }
                        else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){

                            state = 3 ;
                        }
                        else{

                            state = -1;
                        }
                        break;

                    case 4:
                        if ((ch >= '0' && ch <= '9') ||
                                (ch >= 'a' && ch <= 'z') ||
                                (ch >= 'A' && ch <= 'Z')){

                            state = 4 ;
                        }
                        else{

                            state = -1;
                        }
                        break;
                }
            }

                return state == 3;
    }
    public static void main(String [] args){
            System.out.println(scan("123456Bianchi") ? "OK" : "NOPE");
        }
}

