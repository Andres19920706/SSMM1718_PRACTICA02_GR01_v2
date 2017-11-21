package es.ujaen.ssmm.ssmm1718_practica02_gr01;

/**
 * Created by Andres on 18/11/2017.
 */

public class ConnectionUserData extends PersonalData {
    //----------------------------------------------------------------------------------------------
                                        //Constantes y atributos
        //Nombre del servidor (DNS) o dirección IP
    protected String connectionIP;
        //Puerto (diferencia el servico dentro del servidor)
    protected short connectionPort;

    //----------------------------------------------------------------------------------------------
                                    //Constructor

    /**
     * Constructor con parámetros
     * Permite inicailizar los atributos de la clase cuando esta es instanciada o ejemplarizada
     * @param user tipo String
     * @param pass tipo String
     * @param ip tipo String
     * @param port tipo Short
     */
    public ConnectionUserData(String user,String pass,String ip,short port){
        super(user,pass); //Llamada al constructor del padre
        this.connectionIP=ip;
        this.connectionPort=port;
    }

    //----------------------------------------------------------------------------------------------
                                    //Métodos de acceso Getters & Setters

    /**
     * Método get de ConnectionIP
     * Devuelve el contenido de ConnectionIP, del objeto instanciado
     * @return String
     */
    public String getConnectionIP() {
        return connectionIP;
    }

    /**
     * Método set de ConnecciónIP
     * Permite cambiar el valor de la direccciónIP del objeto instancia.
     * @param connectionIP : String
     */
    public void setConnectionIP(String connectionIP) {
        this.connectionIP = connectionIP;
    }

    /**
     * Método get de ConeectionPort
     * Devuelve el contenido de ConnectionPort, del objeto instanciado
     * @return short
     */
    public short getConnectionPort() {
        return connectionPort;
    }

    /**
     * Método set de ConnecciónPort
     * Permite cambiar el valor de la direccciónPort del objeto instancia.
     * @param connectionPort : Short
     */
    public void setConnectionPort(short connectionPort) {
        this.connectionPort = connectionPort;
    }
}