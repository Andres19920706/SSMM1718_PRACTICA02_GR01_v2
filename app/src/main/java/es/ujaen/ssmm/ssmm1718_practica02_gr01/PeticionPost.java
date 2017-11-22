package es.ujaen.ssmm.ssmm1718_practica02_gr01;

/**
 * Created by Andres on 20/11/2017.
 */

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * La clase PeticionPost, contiene métodos y atributos necesarios, para realizar
 * una petición POST en JAVA y poder así simular un método de HTTP.
 *
 * @author Andres Ruiz Peñuela
 * @author Luis Jesús Montes Pérez
 * @version 3.0.0
 */
public class PeticionPost extends MetodosConnect {
    //----------------------------------------------------------------------------------------------
                                        //Constantes y atributos
    //Constantes
        //Códigos de error del cliente.
    public final static String ERRORTYPE2[] = {"500 Servidor Desconectado.", "300 Error Interno."};
        //Protocolo de aplicación
    public static final String PROTOCOL = "http";
        //Nombre de la máquina servidora
    public static final String NAMEHOST = "ssmm1718_practica02_gr01_servidor";
        //Tiempo en ms, que espera el cliente a que el servidor acepte la petición
    public static final int TIMECONNECT = 7000; //ms
        //Método de petición
    public static final String METODO[] = {"POST","GET"};
        //Recurso solicitado
    public static final String RECURSO[] = {"/","/index"};
    //Atributos
        //Dirección del servidor
    private String request ;
        //Parámetros de pitición
            //Ejemplo, petición de autentica: usuario=manuelZ&clave=123
    private String parametros;

    //----------------------------------------------------------------------------------------------
                                        // Constructores
    /**
     * Constructor por defecto.
     */
    public PeticionPost(){
        //Recurso por defecto (loggin)
        //"http://192.168.0.19:8080/ssmm1718_practica02_gr01_servidor/"
        //"http://192.168.0.19:8080/ssmm1718_practica02_gr01_servidor/"
        //"http://www4.ujaen.es/~jccuevas/ssmm/login.php" //http://www4.ujaen.es/~jccuevas/ssmm/login.php?user=An&pass=13
        this.request ="http://10.82.125.108:8080/ssmm1718_practica02_gr01_servidor/";
        this.parametros="usuario=Andres&clave=123";
    }

    /**
     * Constructor con parámetros.
     * @param direccionIP de tipo String
     * @param port de tipo Short
     * @param datos de tipo String
     * @param idRecurso de tipo Short
     */
    public PeticionPost(String direccionIP,short port,String datos,short idRecurso){

        this.request =this.PROTOCOL+"://"+direccionIP+":"+port+"/"+this.NAMEHOST+this.RECURSO[idRecurso];
        this.parametros=datos;
    }

    //----------------------------------------------------------------------------------------------
                            // Metodos de acceso "Getters and Setters"

    /**
     * Metodo que devuelve la direccion del serivor (url)
     * @return request de tipo String
     */
    public String getDireccion() {
        return this.request ;
    }

    /**
     * Metodo que inserta la direcion del servidor
     * @param direccionIP
     * @param port
     * @param idRecurso
     */
    public void setDireccion(String direccionIP,String port, short idRecurso){
        this.request =this.PROTOCOL+"://"+direccionIP+":"+port+"/"+this.NAMEHOST+this.RECURSO[idRecurso];
    }

    /**
     * Metodo que devuelve los parametros de la peticion
     * @return parametros de tipo String
     */
    public String getParametros() {
        return this.parametros;
    }
    /**
     * Metodo para insertar los parametros de la peticion
     * @param datos de tipo String
     */
    public void setParametros(String datos){
        this.parametros =datos;
    }

    //----------------------------------------------------------------------------------------------
                                            // Métodos
    /**
     * Método: Autentica (heredado de la clase abstracta "MétodosConnect")
     * Objetivo: Enviar petición post, para autenticar.
     * @return objeto de tipo String
     * @exception UnsupportedEncodingException
     * @exception MalformedURLException
     * @exception ConnectException
     * @exception ProtocolException
     * @exception IOException
     */
    public  String autentica( ) {
        //Variables
        String response = ""; //Almacena la respuesta
        byte[] msg = new byte[0]; //Contine los datos a enviar.
        DataOutputStream out = null; //Para capturar datos
        Reader in = null; //Para leer datos
        URL url = null; // Instancia de tipo URL, Nota:  Genera la excección java.et.MalformedURLExeption: Protocol not found
        HttpURLConnection conect = null; // Instancia  de la clase HttpURLConnection
        /*
        * https://borrowbits.com/2013/06/cliente-http-para-android/
        Integer codigoRespuesta = Integer.toString(urlConnection.getResponseCode());
         if(codigoRespuesta.equals("200")){//Vemos si es 200 OK y leemos el cuerpo del mensaje.
            body = readStream(urlConnection.getInputStream());
         }
        * */

        //Procedimiento
        try {
            msg = this.parametros.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("AnDomus","Eroor al convertir Sring a Bytes");
        }

        if(msg!=null) {
            //Si no hay error al converitr los paramétos a enviar.
            try {
                url = new URL(request);

            } catch (MalformedURLException e) {
                Log.e("AnDomus", "La URL no es valida, tipo de excepcion " + e);
                response = "URL no valida";
            }

            if (url != null) {
                //Si no hay error al instanciar la URL
                try{

                    conect= (HttpURLConnection) url.openConnection(); /** Instnaica de tipo HttpURLConnection */
                    // java.net.ConnectException: failed to connect to /192.168.0.19 (port 8080) after 90000ms: isConnected failed: EHOSTUNREACH (No route to host)
                }catch(ConnectException e ){
                    Log.e("AnDomus","Conexión errónea "+e);
                    response = "Error al establecer la conexión 1";
                    conect = null;
                }catch(IOException e){
                    Log.e("AnDomus","Conexión errónea "+e);
                    response = "Error al establecer la conexión 2";
                    conect = null;
                }

                while(conect != null){//Si se puede establcer una conexion
                    //Establecemos el tiempo de conexion
                    conect.setConnectTimeout(TIMECONNECT);
                    conect.setDoOutput(true);//Especificamos que vamos a escribir.
                    conect.setInstanceFollowRedirects(false);//En caso de recibir una respuesta con el código 3xx, no redireccionamos. (Por defecto es true)
                    try {
                        //Indiamos el método HTTP de la peticion (Por defecto es Gest).
                        conect.setRequestMethod(METODO[0]);
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                        Log.e("AnDomus","Error al establcer el protocolo"+e);
                        response = "Error al establecer el protocolo";
                        conect = null;
                    }

//                    if(conect!=null){
//
//                    }
                    //Definimos las propiedades de la petción

                    conect.setRequestProperty("Content-Type","application/x-www-form-urlencoded");//Tipo de contenido.
                    conect.setRequestProperty("charset","UTF-8");//Codificación.
                    conect.setRequestProperty("Conent-length", Integer.toString(msg.length));//Longitud del contenido.
                    conect.setUseCaches(false);//Indicamos que ignores las caches.

                    //Preparamos el flujo de datos.
                    try {
                        out = new  DataOutputStream(conect.getOutputStream());
                        out.write(msg);
                        out.flush();

                        //Recibimos la respuesta.
                        in = new BufferedReader(new InputStreamReader(conect.getInputStream(), "UTF-8"));

                        //Guradmos la respuesta, para devolverla.
                        int j=in.read(); //leemos el primer caracter
                        do{
                            response=response+(char)j; //La concatemos.
                            j=in.read(); //leemos el siguiente caracter.
                        }
                        while(j>=0);//Salimos cuando no haya mas caracteres.

                    }catch (IOException e) {
                        e.printStackTrace();
                        Log.e("AnDomus","Obteniendo o Enviando los parametros "+e);
                        response = "Error al enviar o recibir datos";
                        conect=null;
                    }finally{
                        //Cerramos
                        try {
                            out.close();
                            in.close();
                            conect.disconnect();
                            conect=null;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("AnDomus","Cerrando los objetos de write y read "+e);
                            response = "Error al cerrar el connector";
                            conect=null;
                        }
                    }
                }//Fin de connect!=null
            }//Fin de url!=null
        }//Fin de msg!=null

        return response; //Devolvemos la respuesta.
    }

}