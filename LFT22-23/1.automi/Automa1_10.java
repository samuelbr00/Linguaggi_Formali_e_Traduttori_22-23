//Esercizio 1.10. Modificare l’automa dell’esercizio precedente in modo che riconosca
//il linguaggio di stringhe (sull’alfabeto {/, *, a}) che contengono “commenti” delimitati da /* e */,
// ma con la possibilita di avere stringhe prima e dopo come specificato qui di seguito.
//L’idea è che sia possibile avere eventualmente commenti (anche multipli) immersi in una sequenza di simboli
//dell’alfabeto. Quindi l’unico vincolo e che l’automa deve accettare le stringhe in cui un’occorrenza`
//della sequenza /* deve essere seguita (anche non immediatamente) da un’occorrenza della
//sequenza */. Le stringhe del linguaggio possono non avere nessuna occorrenza della sequenza
///* (caso della sequenza di simboli senza commenti). Implementare l’automa seguendo la costruzione vista in Listing 1.
//Esempi di stringhe accettate: “aaa/****/aa”, “aa/*a*a*/”, “aaaa”, “/****/”, “/*aa*/”,
//“*/a”, “a/**/***a”, “a/**/***/a”, “a/**/aa/***/a”
//Esempi di stringhe non accettate: “aaa/*/aa”, “a/**//***a”, “aa/*aa”
public class Automa1_10 {
    public static boolean scan(String s){
        int state = 0;
        for(int i = 0; i < s.length(); i++){
            char ch = s.charAt(i);
            switch(state){
                case 0:
                    if(ch == 'a' || ch == '*'){
                        state = 1;
                    }else if(ch == '/'){
                        state = 2;
                    }else{
                        state = -1;
                    }
                    break;

                case 1:
                    if(ch == 'a' || ch == '*'){
                        state = 1;
                    }else if(ch == '/'){
                        state = 2;
                    }else{
                        state = -1;
                    }
                    break;

                case 2:
                    if(ch == 'a'){
                        state = 1;
                    }else if(ch == '*'){
                        state = 3;
                    }else if(ch == '/'){
                        state = 2;
                    }else{
                        state = -1;
                    }
                    break;

                case 3:
                    if(ch == 'a' || ch == '/'){
                        state = 3;
                    }else if(ch == '*'){
                        state = 4;
                    }else{
                        state = -1;
                    }
                    break;

                case 4:
                    if(ch == 'a'){
                        state = 3;
                    }else if(ch == '/'){
                        state = 5;
                    }else if(ch == '*'){
                        state = 4;
                    }else{
                        state = -1;
                    }
                    break;

                case 5:
                    if(ch == 'a' || ch == '*'){
                        state = 5;
                    }else if(ch == '/'){
                        state = 6;
                    }else{
                        state = -1;
                    }
                    break;

                case 6:
                    if(ch == 'a'){
                        state = 5;
                    }else if(ch == '*'){
                        state = 3;
                    }else if(ch == '/'){
                        state = 6;
                    }else{
                        state = 1;
                    }
                    break;
            }
        }
        return state == 1 || state == 2 || state == 5 || state == 6;
    }
    public static void main(String [] args){System.out.println(scan("a/**/aa/***/a") ? "OK" : "NOPE");}
}
