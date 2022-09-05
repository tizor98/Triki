import java.util.Arrays;
import java.util.Scanner;
public class Triki {

    public static byte turno = 1;

    public static void main(String[] args) {

        byte cantidadJugadores = iniciarJuego();

        juegoTriki(cantidadJugadores);

    }

    public static void juegoTriki(byte cantJugadores) {
        // Se crea el tablero de juego
        byte filas = 3;
        byte columnas = 3;
        byte posicion = 1;
        String[][] tablero = new String[filas][columnas];
        for(int i= 0; i< filas; i++) {
            for(int j=0; j< columnas; j++) {
                tablero[i][j] = Integer.toString(posicion);
                posicion++;
            }
        }
        // Se crean las condiciones que se editarán para actualizar el juego
        byte player = 1;
        String movimiento1 = "X";
        String movimiento2 = "O";
        // Se inicia el juego
        if(cantJugadores==2) System.out.println("El juego dará inicio. El jugador 1 será X y el 2 será O");
        if(cantJugadores==1) System.out.println("Hola Jugador 1, el juego dará inicio. Tú serás X y la maquina será O");

        boolean finJuego = false;
        while(!finJuego) {
            if(cantJugadores==1 && player==2) {
                byte movimientoMaquina = juegoMaquina(tablero, movimiento1, movimiento2);
                tablero = actualizarTablero(tablero, player, movimientoMaquina, movimiento1, movimiento2);

                finJuego = endGame(tablero, player, movimiento1, movimiento2);
                if(finJuego) finalizarJuego(tablero, player);

                player = 1;
                turno += 1;
            } else {
                boolean movimientoOk = false;
                byte movimiento = 0;
                while(!movimientoOk) {
                    Scanner escanearMovimiento = new Scanner(System.in);
                    System.out.println("Jugador "+player+", mira el número que corresponde a cada posición disponible y elije una:");
                    System.out.println(Arrays.deepToString(tablero).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));

                    if(escanearMovimiento.hasNextByte()) {
                        movimiento = escanearMovimiento.nextByte();
                        if(movimiento <= 0 || !movimientoCorrecto(tablero, movimiento, movimiento1, movimiento2)) {
                            System.out.println("No es un número valido. Elige un valor entre 1 y 9 disponible.");
                            continue;
                        }
                        movimientoOk = true;
                    } else {
                        System.out.println("No es un valor valido. Por favor intenta nuevamente.");
                    }
                }

                tablero = actualizarTablero(tablero, player, movimiento, movimiento1, movimiento2);

                finJuego = endGame(tablero, player, movimiento1, movimiento2);
                if(finJuego) finalizarJuego(tablero, player);

                turno+= 1;
                player = (byte) ((player == 1) ? 2 : 1);
            }
        }

    }

    public static void finalizarJuego(String[][] tabla, byte player) {
        System.out.println(Arrays.deepToString(tabla).replace("], ", "]\n").replace("[[", "[").replace("]]", "]"));
        System.out.println("Fin del juego");
        if (turno == -1) {
            System.out.println("Se ha llegado al máximo de turnos posibles. Intentar nuevamente.");
        } else {
            System.out.println("Felicitaciones jugador "+ player +", ganaste el juego :)");
        }

        boolean respuestaOk = false;
        String respuesta = "";

        while(!respuestaOk) {
            Scanner escanearRespuesta = new Scanner(System.in);
            System.out.println("¿Quieres jugar nuevamente? Responde Sí o No.");

            if(escanearRespuesta.hasNextLine()){
                respuesta = escanearRespuesta.nextLine();
                if (respuesta.equals("Sí") || respuesta.equals("Si") || respuesta.equals("si") || respuesta.equals("sí")) {
                    respuestaOk = true;
                    respuesta = "ok";
                } else if(respuesta.equals("No") || respuesta.equals("no") || respuesta.equals("NO")){
                    respuestaOk = true;
                }
            } else{
                System.out.println("No es un valor valido. Por favor intenta nuevamente.");
            }
        }
        if (respuesta.equals("ok")) {
            turno = 1;
            byte cantidadJugadores = iniciarJuego();
            juegoTriki(cantidadJugadores);
        }
    }

    public static byte juegoMaquina(String[][] tabla, String mov1, String mov2) {
        byte movimiento = 0;
        boolean movOk = false;
        while (!movOk) {
            movimiento = (byte)(Math.random()*(9)+1);
            movOk = movimientoCorrecto(tabla, movimiento, mov1, mov2);
        }
        return movimiento;
    }

    public static String[][] actualizarTablero(String[][] tabla, byte jugador, byte movimiento, String mov1, String mov2) {
        int[] indices = encontrarIndice(movimiento);
        if(jugador == 1) {
            tabla[indices[0]][indices[1]] = mov1;
        } else if (jugador == 2) {
            tabla[indices[0]][indices[1]] = mov2;
        }
        return tabla;
    }

    public static int[] encontrarIndice(byte numero) {
        if(numero <=3) {
            return new int[]{0, (numero-1) % 3};
        } else if (numero <= 6) {
            return new int[]{1, (numero-1) % 3};
        } else {
            return new int[]{2, (numero-1) % 3};
        }
    }

    public static boolean movimientoCorrecto(String[][] tabla, byte movimiento, String mov1, String mov2) {
        int[] indices = encontrarIndice(movimiento);
        return !mov1.equals(tabla[indices[0]][indices[1]]) && !mov2.equals(tabla[indices[0]][indices[1]]) && movimiento <= 9 && movimiento >= 1;
    }

    public static boolean endGame(String[][] tabla, byte player, String mov1, String mov2) {
        boolean resultado = false;
        String texto = (player == 1) ? mov1 : mov2;
        String[] tabComparar = new String[]{texto, texto, texto};
        String[] tabDiagonal = new String[]{"","",""};
        String[] tabDiagonalInv = new String[]{"","",""};
        String[] tabFilas = new String[]{"","",""};
        String[] tabColumnas = new String[]{"","",""};

        for(int i=0; i<3; i++) {
            Arrays.fill(tabColumnas, "");
            Arrays.fill(tabFilas, "");
            if(tabla[i][i].equals(texto)) {
                tabDiagonal[i] = texto;
            }
            if(tabla[i][2 - i].equals(texto)) {
                tabDiagonalInv[i] = texto;
            }
            for(int j=0; j<3; j++) {
                if(tabla[i][j].equals(texto)) {
                    tabFilas[j] = texto;
                }
                if(tabla[j][i].equals(texto)) {
                    tabColumnas[j] = texto;
                }
            }
            resultado = Arrays.equals(tabComparar, tabFilas) || Arrays.equals(tabComparar, tabColumnas);
            if(resultado) return resultado;
        }
        resultado = Arrays.equals(tabComparar, tabDiagonal) || Arrays.equals(tabComparar, tabDiagonalInv);
        if(!resultado) {
            if(turno >= 9) {
                resultado = true;
                turno = -1;
            }
        }
        return resultado;
    }

    public static byte iniciarJuego() {
        boolean cantidadOk = false;
        byte cantidadJugadores = 0;

        while(!cantidadOk) {
            Scanner escanearRespuesta = new Scanner(System.in);
            System.out.println("¿Cuántos jugadores van a jugar? Puedes seleccionar 1 o 2.");

            if(escanearRespuesta.hasNextByte()){
                cantidadJugadores = escanearRespuesta.nextByte();
                if (cantidadJugadores == 1 || cantidadJugadores == 2) {
                    cantidadOk = true;
                } else {
                    System.out.println("No es un número valido. Por favor ingresa un 1 o un 2.");
                }
            } else{
                System.out.println("No es un valor valido. Por favor intenta nuevamente.");
            }
        }
        return cantidadJugadores;
    }
}