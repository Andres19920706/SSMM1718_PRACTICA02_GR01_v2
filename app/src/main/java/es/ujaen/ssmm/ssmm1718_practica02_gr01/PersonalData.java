package es.ujaen.ssmm.ssmm1718_practica02_gr01;

/**
 * Created by Andres on 18/11/2017.
 */

public class PersonalData {
    //----------------------------------------------------------------------------------------------
                                    //Constantes y atributos
    protected String user; //Nombre del usuario
    protected String pass; //Calve del usuario

    //----------------------------------------------------------------------------------------------
                                    //Constructor

    /**
     * Constructor de la clase
     * Permite inicializar los atributos de la clase, cuando esta es instanciada
     * @param user : String
     * @param pass : String
     */
    public PersonalData(String user,String pass){
        this.user=user;
        this.pass=pass;
    }

    //----------------------------------------------------------------------------------------------
                                //Métodos de acceso Getters & Setters

    /**
     * Método getUser
     * Permite obtener el campo user del objeto o de la instancia
     * @return user : String
     */
    public String getUser() {
        return user;
    }

    /**
     * Métod setUser
     * Permite cambiar el valor o campo user del objeto o instancia
     * @param user :  String
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Métod getPass
     * Permite obtener el campo pass del objeto o de la instancia
     * @return user : String
     */
    public String getPass() {
        return pass;
    }

    /**
     * Métod setPass
     * Permite cambiar el valor o campo pass del objeto o instancia
     * @param pass :  String
     */
    public void setPass(String pass) {
        this.pass = pass;
    }
}
