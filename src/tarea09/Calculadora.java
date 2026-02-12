package tarea09;

import static java.lang.System.exit;
//Hemos importado listas paara facilitar ciertas tareas con los botones
import java.util.ArrayList;
import java.util.List;
//Librerías para poder utilizar JavaFX
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
//Librerías específicas para evaluar expresiones exp4j
//Nota, hemos entrado a propiedades del proyecto para añadir a la librería el paquete exp4j y que pueda importarse sus clases
import net.objecthunter.exp4j.*;

/**
 * La típica calculadora básica para realizar cálculos artitméticos como la suma, resta, multiplicación y
 * división, en la que se permite el cálculo de expresiones combinadas.
 *
 * @author Paula Serrano Torrecillas
 */
public class Calculadora extends Application {

    //----------------------------------------------
    //          Declaración de variables 
    //----------------------------------------------
    //BOTONES
    private final Button btC = new Button("C");
    private final Button btSupr = new Button("\u2190");
    private final Button btIgual = new Button("=");
    private final Button btSum = new Button("+");
    private final Button bt7 = new Button("7");
    private final Button bt8 = new Button("8");
    private final Button bt9 = new Button("9");
    private final Button btRest = new Button("-");
    private final Button bt4 = new Button("4");
    private final Button bt5 = new Button("5");
    private final Button bt6 = new Button("6");
    private final Button btMul = new Button("*");
    private final Button bt1 = new Button("1");
    private final Button bt2 = new Button("2");
    private final Button bt3 = new Button("3");
    private final Button btDiv = new Button("/");
    private final Button bt0 = new Button("0");
    private final Button btComa = new Button(".");
    private final Button btAperturaP = new Button("(");
    private final Button btCierreP = new Button(")");
    //VISOR
    private final TextField visor = new TextField();
    //LAYOUT
    // Creamos un panel de tipo GridPane porque nos facilitará mucho componer en rejilla
    private final GridPane rejilla = new GridPane();
    //ESCENA
    private Scene escena;
    // Error salida calculadora
    private final String errorVisor = "ERROR / pulse C";

    //----------------------------------------------
    //          Declaración de variables auxiliares 
    //----------------------------------------------  
    // Lista que guardará los botones para iterar operaciones repetitivas
    private List<Button> botonera;
    // Índices para la introducción de botones en la rejilla del layout
    private int iColumna = 0;
    private int iFila = 1;

    /*El método start es el punto de entrada para una aplicación JavaFX.
    Su función principal es inicializar y mostrar la interfaz 
    gráfica de usuario (GUI) de la aplicación. Se crea un diseño (layout), 
    se añaden controles (botones, etiquetas, campos, ...) y se crea la escena
    con un estilo, y se muestra el escenario.*/
    @Override
    public void start(Stage escenario) {

        //IMPORTACIÓN DE RECURSOS INTERNOS___________________________________________________________________
        //Importamos una fuente personalizada que uasaremos en el CSS
        Font fuente = Font.loadFont(getClass().getResourceAsStream("/tarea09/recursos/pokemon-emerald.otf"), 40);

        //TRATAMIENTO DE ELEMENTOS EN ESCENA____________________________________________________________________
        // Hacemos que el campo de texto sea no editable
        this.visor.setEditable(false);

        //Terminamos de instanciar el ArrayList
        this.botonera = new ArrayList<>();
        // Introducimos los botones en el ArrayList
        this.botonera.add(this.btC);
        this.botonera.add(this.btSupr);
        this.botonera.add(this.btIgual);
        this.botonera.add(this.btSum);
        this.botonera.add(this.bt7);
        this.botonera.add(this.bt8);
        this.botonera.add(this.bt9);
        this.botonera.add(this.btRest);
        this.botonera.add(this.bt4);
        this.botonera.add(this.bt5);
        this.botonera.add(this.bt6);
        this.botonera.add(this.btMul);
        this.botonera.add(this.bt1);
        this.botonera.add(this.bt2);
        this.botonera.add(this.bt3);
        this.botonera.add(this.btDiv);
        this.botonera.add(this.bt0);
        this.botonera.add(this.btComa);
        this.botonera.add(this.btAperturaP);
        this.botonera.add(this.btCierreP);

        //APLICACIÓN DE ESTILOS ESPECÍFICOS__________________________________________________________________
        // Aplicamos estilo a los operando
        for (Button i : this.botonera) {
            if (i.getText().matches("[-+/*()]")) {
                i.getStyleClass().add("button-operador");
            }
        }
        // Aplicamos estilo al igual
        this.btIgual.getStyleClass().add("button-igual");
        // Aplicamos estilo a los botones de limpieza
        this.btC.getStyleClass().add("button-limpiar");
        this.btSupr.getStyleClass().add("button-limpiar");

        //EVENTOS____________________________________________________________________________________________
        //Iteramos sobre los botones creando un evento que dirija al método procesoDeEntrada
        for (Button i : this.botonera) {
            // Desactivamos el foco de los botones para evitar qhe el visor haga enfoque extraños de la cadena al pulsar botones
            i.setFocusTraversable(false);
            // Llamamos al método procesoDeEntrada e introducimos el texto del botón como el String de entrada 
            i.setOnAction((ActionEvent event) -> procesoDeEntrada(i.getText()));
        }

        //CONFIGURACIÓN DE LAYOUT____________________________________________________________________________
        // Introducimos los elementos en la rejilla indicando el índice de iColumna y iFila, siendo (0,0) la primera celda
        // Primero el visor, que ocupa todas las columnas de la primera iFila
        this.rejilla.add(visor, 0, 0, 4, 1);
        // Además daremos espacio para separar el visor de la botonera, pero sin dar espacio entre las filas de la botonera
        GridPane.setMargin(visor, new Insets(20, 0, 30, 0));
        //Ahora introducimos los botones en las celdas restantes según hemos decidido nosotros
        //Iteraremos el ArrayList botonera, que hemos creado en un orden específico que facilita esta tarea
        for (Button i : this.botonera) {
            this.rejilla.add(i, this.iColumna, this.iFila);
            if (this.iColumna >= 3) {
                this.iColumna = 0;
                this.iFila++;
            } else {
                this.iColumna++;
            }
        }

        //ESCENA_____________________________________________________________________________________________
        //Inicializamos la escena con el layout
        this.escena = new Scene(rejilla);
        //Aplicamos la hoja de estilos CSS a la escena
        this.escena.getStylesheets().add("tarea09/calculadora.css");

        //CONFIGURACIÓN ESCENARIO____________________________________________________________________________
        // Damos título al escenario/ventana
        escenario.setTitle("Mi calculadora");
        //Vamos a crear un bloque try-catch porque hay un recurso que entra o se importa pudiendo generar error
        //Pues, si el archivo se borra, cambia de carpeta o paquete o bien recibe un nuevo nombre, generaría una excepción o error
        try {
            // Añadimos la imagen del icono de aplicación al escenario
            Image icono = new Image(getClass().getResourceAsStream("logoCalcu.png"));
            escenario.getIcons().add(icono);
        } catch (Exception e) {
            System.err.print("Icono de aplicación no encontrado\n");
            exit(-1);
        }
        //Establecemos un tamaño en base a la escena
        escenario.sizeToScene();
        //Establecemos que la ventana no sea redimensionable
        escenario.resizableProperty().setValue(false);
        // Añadimos la escena al escenario
        escenario.setScene(escena);
        // Mostramos el escenario
        escenario.show();
    }

    /**
     * El método procesoDeEntrada maneja la entrada de datos en una calculadora. Toma una cadena de texto
     * (entrada) y realiza diferentes acciones según el contenido de esa cadena, agregando al campo de texto,
     * estableciendo el estado, controlando la adición de puntos decimales para evitar múltiples decimales en
     * un número o evaluando la expresión mátemática para mostrar el resultado haciendo uso de la librería
     * Exp4J.
     *
     * @param entrada Texto recogido de los diferentes textoBotones de la calculadora.
     */
    public void procesoDeEntrada(String entrada) {
        //DÍGITOS____________________________________________________________________________________________
        //Si la tecla pulsada es un dígito y el visor no contiene el texto de error
        if (entrada.matches("\\d") && !this.visor.getText().equals(this.errorVisor)) {
            // Se concatena al texto actual del visor
            this.visor.setText(this.visor.getText() + entrada);
            // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
            // Para ello usamos positionCaret y posicionamos al final del String del visor
            this.visor.positionCaret(this.visor.getText().length());
        }
        
        //OPERADORES_________________________________________________________________________________________
        //Si la tecla pulsada es un operador y el último caracter es un dígito o un cierre de paréntesis
        if (entrada.matches("[-+/*]") && this.visor.getText().matches(".*(\\d|\\))$")) {
            // Se concatena al texto actual del visor
            this.visor.setText(this.visor.getText() + entrada);
            // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
            // Para ello usamos positionCaret y posicionamos al final del String del visor
            this.visor.positionCaret(this.visor.getText().length());
        }
        
        //LIMPIEZA___________________________________________________________________________________________
        //Si la tecla pulsada es C se borra todo el contenido
        if (entrada.equals(this.btC.getText())) {
            // El visor es una cadena vacía
            this.visor.setText("");
        }
        //Si la tecla pulsada es la flecha de retroceso se borra el último caracter si la cadena no está vacía y no es de error
        if (entrada.equals(this.btSupr.getText()) 
                && !this.visor.getText().isEmpty()
                && !this.visor.getText().equals(this.errorVisor)) {
            // El visor es la cadena actual menos la última posición
            this.visor.setText(this.visor.getText().substring(0, this.visor.getText().length()-1));
        }
        
        //IGUAL/RESULTADO____________________________________________________________________________________________
        //Si la tecla pulsada es =
        if (entrada.equals(this.btIgual.getText())) {
            // Puede haber error aritmético que recogeremos en este try-catch
            try {
                Expression operacion = new ExpressionBuilder(this.visor.getText()).build();
                double resultado = operacion.evaluate();
                // Vamos a condicionar que si los decimales no añade valor al número no los muestre en la salida
                // Para ello compararaemos con el resultado redondeado y si el valor es el mismo, implica que los decimales no añaden valor
                if (resultado == Math.floor(resultado)) {
                    this.visor.setText(String.valueOf((int) resultado));
                    // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
                    // Para ello usamos positionCaret y posicionamos al final del String del visor
                    this.visor.positionCaret(this.visor.getText().length());
                } else {
                    this.visor.setText(String.valueOf(resultado));
                    // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
                    // Para ello usamos positionCaret y posicionamos al final del String del visor
                    this.visor.positionCaret(this.visor.getText().length());
                }
            } catch (Exception ArithmeticException) {
                this.visor.setText(this.errorVisor);
            }
        }
        
        //COMA DECIMAL_______________________________________________________________________________________
        //Si la tecla pulsada es la coma y el último caracter es un número
        if (entrada.equals(this.btComa.getText())
                && this.visor.getText().matches(".*\\d$")) {
            // Comprobaremos en la cadena si se encuentra algún operador o paréntesis y en qué posición
            // Para ello usaremos lastIndexOf por cada operador, dentro de varios Math.max anidados (ya que sólo permiten comparaciones por pares) 
            // Con esto buscaremos qué operador es el último y en qué posición. De no haber ningún operador la posición sería -1
            int indice
                    = Math.max(
                            Math.max(
                                    Math.max(
                                            this.visor.getText().lastIndexOf(this.btSum.getText()),
                                            this.visor.getText().lastIndexOf(this.btRest.getText())),
                                    Math.max(
                                            this.visor.getText().lastIndexOf(this.btMul.getText()),
                                            this.visor.getText().lastIndexOf(this.btDiv.getText()))),
                            Math.max(
                                    this.visor.getText().lastIndexOf(this.btAperturaP.getText()),
                                    this.visor.getText().lastIndexOf(this.btCierreP.getText())));
            // Si no hay ningún operador, el índice es -1 y no hay comas introducidas
            if (indice == -1 && !this.visor.getText().contains(this.btComa.getText())) {
                // Se concatena al texto actual del visor
                this.visor.setText(this.visor.getText() + entrada);
                // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
                // Para ello usamos positionCaret y posicionamos al final del String del visor
                this.visor.positionCaret(this.visor.getText().length());
            } else {
                //Hacemos substring desde el operador hasta el final y comprobamos que no haya sido introducida ninguna coma todavía
                //Hacemos esto para evitar escribir 5.6.7
                if (indice != -1 && !this.visor.getText().substring(indice).contains(this.btComa.getText())) {
                    this.visor.setText(this.visor.getText() + entrada);
                    // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
                    // Para ello usamos positionCaret y posicionamos al final del String del visor
                    this.visor.positionCaret(this.visor.getText().length());
                }
            }
        }
        
        //PARÉNTESIS____________________________________________________________________________________________
        //Apertura de paréntesis
        if (entrada.equals(this.btAperturaP.getText()) && !this.visor.getText().equals(this.errorVisor)) {
            //Si el último caracter es un dígito
            if (this.visor.getText().matches(".*\\d$")) {
                // Se concatena al texto actual del visor pero se añade un signo de mltiplicar
                this.visor.setText(this.visor.getText() + this.btMul.getText() + entrada);
                // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
                // Para ello usamos positionCaret y posicionamos al final del String del visor
                this.visor.positionCaret(this.visor.getText().length());
            //Si el último caracter es una coma  
            }else if (this.visor.getText().matches(".*\\.$")) {
                // Eliminamos la coma, usando retroceso, creando una entrada por teclado del programa (no el usuario)
                this.btSupr.fire();
                // Se concatena al texto actual del visor pero se añade un signo de mltiplicar
                this.visor.setText(this.visor.getText() + this.btMul.getText() + entrada);
                // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
                // Para ello usamos positionCaret y posicionamos al final del String del visor
                this.visor.positionCaret(this.visor.getText().length());
            //Para el resto de casos    
            } else {
                // Se concatena al texto actual del visor sin añadir o suprimir nada
                this.visor.setText(this.visor.getText() + entrada);
                // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
                // Para ello usamos positionCaret y posicionamos al final del String del visor
                this.visor.positionCaret(this.visor.getText().length());
            }
        }
        //Cierre de paréntesis
        if (entrada.equals(this.btCierreP.getText())) {
            // Vamos a contar los parentesis abiertos y cerrados
            int pAb = 0;
            int pCr = 0;
            
            for (int i = 0; i < this.visor.getText().length(); i++) {
                if (this.visor.getText().charAt(i) == '(') {
                    pAb++;
                }
                if (this.visor.getText().charAt(i) == ')') {
                    pCr++;
                }
            }
            // Si hay más paréntesis abiertos que cerrados y los últimos caracteres son gígitos o un paréntesis cerrado
            if (pAb > pCr && this.visor.getText().matches(".*(\\d|\\))$")) {
                // Se concatena al texto actual del visor sin añadir o suprimir nada
                this.visor.setText(this.visor.getText() + entrada);
                // Nos aseguramos de que el visor muestre los últimos caracteres introducidos
                // Para ello usamos positionCaret y posicionamos al final del String del visor
                this.visor.positionCaret(this.visor.getText().length());
            }
        }
    }

    /**
     * Programa principal, lanza la aplicación.
     *
     * @param args Argumentos o parámetros (no hay en este caso)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
