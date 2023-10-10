/*  Modificare l’automa dell’esercizio precedente in modo che riconosca le combinazioni di matricola e
cognome di studenti del turno 2 o del turno 3 del laboratorio, dove il numero
di matricola e il cognome possono essere separati da una sequenza di spazi, e possono essere
precedute e/o seguite da sequenze eventualmente vuote di spazi. Per esempio, l’automa deve
accettare la stringa “654321 Rossi” e “ 123456 Bianchi ” (dove, nel secondo esempio, ci
sono spazi prima del primo carattere e dopo l’ultimo carattere), ma non “1234 56Bianchi” e
“123456Bia nchi”. Per questo esercizio, i cognomi composti (con un numero arbitrario di parti)
possono essere accettati: per esempio, la stringa “123456 De Gasperi” deve essere accettato.
Modificare l’implementazione Java dell’automa di conseguenza. */
public class Automa1_4 {

    public static boolean scan(String s){

            int state = 0;

            for(int i = 0; i < s.length(); i++){
                char ch = s.charAt(i);

                switch (state) {

                    //Init state, id regognising
                    case 0:
                        if ((int) ch % 2 == 0){
                            state= 1;
                        }
                        else if ((int) ch % 2 == 1){
                            state=2;
                        }
                        else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == ' '){
                            state= 7 ;
                        }
                        else{ state= -1;
                        }
                        break;

                    //even id regognised
                    case 1:
                        if(ch == ' '){
                            state=3;
                        }
                        else if (ch >= 'A' && ch <= 'K'){
                            state=5;
                        }
                        else if ((ch >= 'L' && ch <= 'Z') || (ch >= 'a' && ch <= 'z')){
                            state=7;
                        }
                        else if ((int) ch % 2 == 0){
                            state=1;
                        }
                        else if ((int) ch % 2 == 1){
                            state=2;
                        }
                        else{
                            state=-1;
                        }
                        break;

                    //odd id regognised
                    case 2:
                        if(ch == ' '){
                            state=4;
                        }
                        else if (ch >= 'L' && ch <= 'Z'){
                            state=5;
                        }
                        else if ((ch >= 'A' && ch <= 'K') || (ch >= 'a' && ch <= 'z')){
                            state=7;
                        }
                        else if ((int) ch % 2 == 0){
                            state=1;
                        }
                        else if ((int) ch % 2 == 1){
                            state=2;
                        }
                        else{
                            state=-1;
                        }
                        break;

                    //White space after even id
                    case 3:
                        if(ch == ' '){
                            state=3;
                        }
                        else if (ch >= 'A' && ch <= 'K'){
                            state=5;
                        }
                        else if ((ch >= 'L' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')){
                            state=7;
                        }
                        else{ state=-1; }
                        break;

                    //White space after odd id
                    case 4:
                        if(ch == ' '){
                            state=4;
                        }
                        else if (ch >= 'L' && ch <= 'Z'){
                            state=5;
                        }
                        else if ((ch >= 'A' && ch <= 'K') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')){
                            state=7;
                        }
                        else{ state=-1; }
                        break;

                    //Successful state
                    case 5:
                        if ((ch >= 'a' && ch <= 'z')){
                            state=5;
                        }
                        else if(ch == ' '){
                            state=6;
                        }
                        else if ((ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')){
                            state=7;
                        }
                        else{ state=-1; }
                        break;

                    //Successful state with white space
                    case 6:
                        if (ch >= 'A' && ch <= 'Z'){
                            state=5;
                        }
                        else if(ch == ' '){
                            state=6;
                        }
                        else if ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')){
                            state=7;
                        }
                        else{
                            state=-1;
                        }
                        break;

                    //First letter regognised
                    case 7:
                        if ((ch >= 'a' && ch <= 'z') ||
                                (ch >= 'A' && ch <= 'Z') ||
                                (ch >= '0' && ch <= '9') ||
                                ch == ' '){
                            state=7;
                        }
                        else{ state=-1; }
                        break;

                }
            }

                return state == 5 || state == 6;
    }
    public static void main(String [] args){
        System.out.println(scan("123456Bianchi") ? "OK" : "NOPE");
    }

}



