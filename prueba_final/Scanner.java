package scanner;

//LIBRERIAS
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//CLASE SCANNER MAIN
public class Scanner {
    
    //VARIABLES DE ENTRADA
    private final String source; 
    
    private final List<Token> tokens = new ArrayList<>();
    
    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("(",  TipoToken.LEFT_PAREN);
        palabrasReservadas.put(")",  TipoToken.RIGHT_PAREN);
        palabrasReservadas.put("{",  TipoToken.LEFT_BRACE);
        palabrasReservadas.put("}",  TipoToken.RIGHT_BRACE);
        palabrasReservadas.put(",",  TipoToken.COMMA);
        palabrasReservadas.put(".",  TipoToken.DOT);
        palabrasReservadas.put("-",  TipoToken.MINUS);
        palabrasReservadas.put("+",  TipoToken.PLUS);
        palabrasReservadas.put(";",  TipoToken.SEMICOLON);
        palabrasReservadas.put("/",  TipoToken.SLASH);
        palabrasReservadas.put("*",  TipoToken.STAR);
        palabrasReservadas.put("!",  TipoToken.BANG);
        palabrasReservadas.put("!=",  TipoToken.BANG_EQUAL);
        palabrasReservadas.put("=",  TipoToken.EQUAL);
        palabrasReservadas.put("==",  TipoToken.EQUAL_EQUAL);
        palabrasReservadas.put(">",  TipoToken.GREATER);
        palabrasReservadas.put(">=",  TipoToken.GREATER_EQUAL);
        palabrasReservadas.put("<",  TipoToken.LESS);
        palabrasReservadas.put("<=",  TipoToken.LESS_EQUAL);
        palabrasReservadas.put("null",  TipoToken.NULL);
        palabrasReservadas.put("true",  TipoToken.TRUE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("String",  TipoToken.STRING);
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);
    }

    Scanner(String source){
        this.source = source+" ";
    }
    
    public List<Token> scanTokens(){
        int estado = 0;
        String lexema = "";
        int inicioLexema = 0;
        char car;

        for(int i = 0; i<source.length();i++){
            car = source.charAt(i);
            switch(estado)
            {
                case 0: //OPERADORES
                    switch(car){
                        case '-':
                            tokens.add(new Token(TipoToken.MINUS, "-",  i+1));
                            break;
                        case '+':
                            tokens.add(new Token(TipoToken.PLUS, "+",  i+1));
                            break;
                        case '*':
                            tokens.add(new Token(TipoToken.STAR, "*",  i+1));
                            break;
                        case '/':
                            if (car == '/') {
                                // Verificar si hay otro '/' consecutivo
                                if (i + 1 < source.length() && source.charAt(i + 1) == '/') {
                                    // Generar un token para el '/'
                                    tokens.add(new Token(TipoToken.SLASH, "/", inicioLexema + 1));
                                    estado = 4; // Mover al caso 5 para reconocer comentarios
                                    i++; // Avanzar al siguiente carácter
                                } else {
                                    tokens.add(new Token(TipoToken.SLASH,"/",i+1));
                                }
                            }
                            //tokens.add(new Token(TipoToken.DIVISION,"/",i+1));
                            break;
                        case '=':
                            if (car == '=') {
                                if (i + 1 < source.length() && source.charAt(i + 1) == '=') {
                                    tokens.add(new Token(TipoToken.EQUAL_EQUAL,"==",i+1));
                                    estado = 0;
                                    i++;
                                } else {
                                    tokens.add(new Token(TipoToken.EQUAL,"=",i+1));
                                }
                            }
                            break;
                        case '<':
                            if (car == '<') {
                                if (i + 1 < source.length() && source.charAt(i + 1) == '=') {
                                    tokens.add(new Token(TipoToken.LESS_EQUAL,"<=",i+1));
                                    estado = 0;
                                    i++;
                                } else {
                                    tokens.add(new Token(TipoToken.LESS,"<",i+1));
                                }
                            }
                            break;
                        case '>':
                            if (car == '<') {
                                if (i + 1 < source.length() && source.charAt(i + 1) == '=') {
                                    tokens.add(new Token(TipoToken.GREATER_EQUAL,">=",i+1));
                                    estado = 0;
                                    i++;
                                } else {
                                    tokens.add(new Token(TipoToken.GREATER,">",i+1));
                                }
                            }
                            break;
                        case '!':
                            if (car == '!') {
                                if (i + 1 < source.length() && source.charAt(i + 1) == '=') {
                                    tokens.add(new Token(TipoToken.BANG_EQUAL,">=",i+1));
                                    estado = 0;
                                    i++;
                                } else {
                                    tokens.add(new Token(TipoToken.BANG,"!",i+1));
                                }
                            }

                            break;
                        //VARIANTES A OTROS ESTADOS
                        //Cadena
                        case '"':
                            estado = 1;
                            lexema = lexema+car;
                            inicioLexema=i;
                            break;
                        //SIMBOLOS DEL LENGUAJE
                        case '(':
                            tokens.add(new Token(TipoToken.LEFT_PAREN,"(",i+1));
                            break;
                        case '{':
                            tokens.add(new Token(TipoToken.LEFT_BRACE,"{",i+1));
                            break;
                        case ')':
                            tokens.add(new Token(TipoToken.RIGHT_PAREN,")",i+1));
                            break;
                        case '}':
                            tokens.add(new Token(TipoToken.RIGHT_BRACE,"}",i+1));
                            break;
                        case ',':
                            tokens.add(new Token(TipoToken.COMMA,",",i+1));
                            break;
                        case '.':
                            tokens.add(new Token(TipoToken.DOT,".",i+1));
                            break;
                        case ';':
                            tokens.add(new Token(TipoToken.SEMICOLON,";",i+1));
                            break;
                    }
                    //Reservadas
                    if(Character.isAlphabetic(car)){
                        estado = 2;
                        lexema=lexema+car;
                        inicioLexema = i;
                    } else if(Character.isDigit(car)) {
                        estado = 3;
                        lexema=lexema+car;
                        inicioLexema = i;
                    }
                    break;
                case 1://Cadenas
                    if (car != '"') {
                        lexema = lexema + car;
                    } else {
                        tokens.add(new Token(TipoToken.STRING, lexema, inicioLexema + 1));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;
                case 2 : //Reservadas
                    if(Character.isAlphabetic(car) || Character.isDigit(car)){
                        lexema=lexema+car;
                    } else {
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if(tt ==  null){ 
                            tokens.add(new Token(TipoToken.IDENTIFIER,lexema,inicioLexema+1));
                        } else {
                            tokens.add(new Token(tt,lexema,inicioLexema+1));
                        }
                        estado = 0;
                        lexema = "";
                        inicioLexema=0;
                        i--;
                    }
                          
                    break;
                case 3: //Numeros
                    if(Character.isDigit(car)){
                        lexema=lexema+car;
                    } else {
                        tokens.add(new Token(TipoToken.NUMBER,lexema,inicioLexema+1));
                        estado=0;
                        lexema="";
                        inicioLexema=0;
                        i--;
                    }
                    break;
                case 4:
                    if(car != '\n'){
                        lexema = lexema+car;
                    } else {
                        tokens.add(new Token(TipoToken.COMMENT,lexema,inicioLexema+1));
                        estado=0;
                        lexema="";
                        inicioLexema=0;
                    }
                    break;
            }
        }
        tokens.add(new Token(TipoToken.EOF, "",  source.length())); //Fin del archivo
        return tokens; 
    }
}

/*
Signos o símbolos del lenguaje:
(
)
{
}
,
.
;
-
+
*
/
!
!=
=
==
<
<=
>
>=
// -> comentarios (no se genera token)
/* ... * / -> comentarios (no se genera token)
Identificador,
Cadena
Numero
Cada palabra reservada tiene su nombre de token

 */