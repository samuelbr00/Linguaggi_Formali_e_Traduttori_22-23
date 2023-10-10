/* Progettare e implementare un DFA che, come in Esercizio 1.3, riconosca il linguaggio di stringhe
che contengono matricola e cognome di studenti del turno 2 o del turno 3 del
laboratorio, ma in cui il cognome precede il numero di matricola (in altre parole, le posizioni del
cognome e matricola sono scambiate rispetto all’Esercizio 1.3). */
public class Automa1_5 {

    public static boolean scan(String s){
            int state = 0;
            for(int i = 0; i < s.length(); i++){
                char ch = s.charAt(i);

                switch (state) {
                    case 0:
                        if ((ch >= 'A' && ch <= 'K') || (ch >= 'a' && ch <= 'k')){
                            state = 1 ;
                        }
                        else if ((ch >= 'L' && ch <= 'Z') || (ch >= 'l' && ch <= 'z')){
                            state = 2;
                        }
                        else if (ch >= '0' && ch <= '9'){
                            state = 0;
                        }
                        else{
                            state= -1;
                        }
                        break;

                    case 1:
                        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
                            state = 1;
                        }
                        else if ((int) ch % 2 == 0){
                            state=3;
                        }
                        else if ((int) ch % 2 == 1){
                            state=5;
                        }
                        else{ state=-1;
                        }
                        break;

                    case 2:
                        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
                            state = 2;
                        }
                        else if ((int) ch % 2 == 0){
                            state=6;
                        }
                        else if ((int) ch % 2 == 1){
                            state=4;
                        }
                        else{
                            state= -1;
                        }
                        break;

                    case 3:
                        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
                            state=7;
                        }
                        else if ((int) ch % 2 == 0){
                            state=3;
                        }
                        else if ((int) ch % 2 == 1){
                            state=5;
                        }
                        else{
                            state=-1;
                        }
                        break;

                    case 4:
                        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
                            state=7;
                        }
                        else if ((int) ch % 2 == 0){
                            state=5;
                        }
                        else if ((int) ch % 2 == 1){
                            state=4;
                        }
                        else{
                            state=-1;
                        }
                        break;

                    case 5:
                        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
                            state=7;
                        }
                        else if ((int) ch % 2 == 0){
                            state=3;
                        }
                        else if ((int) ch % 2 == 1){
                            state=5;
                        }
                        else{
                            state=-1;
                        }
                        break;

                    case 6:
                        if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
                            state=7;
                        }
                        else if ((int) ch % 2 == 0){
                            state=6;
                        }
                        else if ((int) ch % 2 == 1){
                            state=4;
                        }
                        else{
                            state=-1;
                        }
                        break;

                    case 7:
                        if ((ch >= 'a' && ch <= 'z') ||
                                (ch >= 'A' && ch <= 'Z') ||
                                (ch >= '0' && ch <= '9')){
                            state=7;
                        }
                        else{
                            state=-1;
                        }
                        break;

                }
            }

            return state == 3 || state == 4;
    }
        public static void main (String [] args){
            System.out.println(scan("Bianco23") ? "OK" : "NOPE");
        }
}