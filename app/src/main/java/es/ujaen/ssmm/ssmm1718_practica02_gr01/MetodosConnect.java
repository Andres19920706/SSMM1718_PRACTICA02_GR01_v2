package es.ujaen.ssmm.ssmm1718_practica02_gr01;

import java.net.SocketTimeoutException;

/**
 * Created by Andres on 21/11/2017.
 */

public abstract class MetodosConnect {
    /**
     * Método autentica
     * Método abstracto para autenticar al cliente frente al servidor
     * @return String
     */
    public abstract String autentica ();

    /*
     * El resto de métodos  aún no están contemplados:
     *      - Cierre de sesión
     *     - Petición básica del cliente.
     */
}
