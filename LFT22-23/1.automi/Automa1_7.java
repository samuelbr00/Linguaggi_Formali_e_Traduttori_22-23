public class Automa1_7 {

    /* Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
contengono il tuo nome e tutte le stringhe ottenute dopo la sostituzione di un carattere del nome
con un altro qualsiasi. Ad esempio, nel caso di uno studente che si chiama Paolo, il DFA deve
accettare la stringa “Paolo” (cioe il nome scritto correttamente), ma anche le stringhe “ ` Pjolo”,
“caolo”, “Pa%lo”, “Paola” e “Parlo” (il nome dopo la sostituzione di un carattere), ma non
“Eva”, “Perro”, “Pietro” oppure “P*o*o”.  */

    public static boolean scan(String s) {
        int state = 0;

        for (int i = 0; i < s.length(); i++) {
           final char ch = s.charAt(i);
            switch (state) {
                case 0:
                    if(ch == 's' || ch == 'S'){
                        state = 1;
                    }
                    else{
                        state = 7;
                    }
                    break;

                case 1:
                    if(ch == 'a' || ch == 'A'){
                        state = 2;
                    }
                    else{
                        state = 8;
                    }
                    break;

                case 2:
                    if(ch == 'm' || ch == 'M'){
                        state = 3;
                    }

                    else{
                        state = 9;
                    }
                    break;

                case 3:
                    if(ch == 'u' || ch == 'U'){
                        state = 4;
                    }
                    else{
                        state = 10;
                    }
                    break;

                case 4 :
                if(ch == 'e' || ch == 'E'){
                    state = 5;
                }
                else{
                    state = 11;
                }
                break;


                case 5:
                    state = (6);
                    break;

                case 6 :
                    state = 12;
                    break;

                case 7:
                    if(ch == 'a' || ch == 'A'){
                        state = 8;
                    }
                    else{
                        state = 12;
                    }
                    break;

                case 8:
                    if(ch == 'm' || ch == 'M'){
                        state = 9;
                    }
                    else{ state = 12;
                    }
                    break;

                case 9:
                    if(ch == 'u' || ch == 'U'){
                        state = 10;
                    }
                    else{

                        state = 12;
                    }
                    break;



                case 10:
                    if(ch == 'e' || ch == 'E'){
                        state = 11;
                    }
                    else{
                        state = 12;
                    }
                    break;

                case 11 :
                    if(ch == 'l' || ch == 'L'){
                        state = 6;
                    }
                    else{
                        state = 12;
                    }
                    break;

                case 12 :
                    state = 12;
                    break;

            }
        }

        return state == 6;
    }

    public static void main(String[] args) {
        System.out.println(scan("Samuel") ? "OK" : "NOPE");

    }
}