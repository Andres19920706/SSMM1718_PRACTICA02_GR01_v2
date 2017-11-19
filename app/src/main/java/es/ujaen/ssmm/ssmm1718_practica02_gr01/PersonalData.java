package es.ujaen.ssmm.ssmm1718_practica02_gr01;

/**
 * Created by Andres on 18/11/2017.
 */

public class PersonalData {

    protected String user="";
    protected String pass="";

    public PersonalData(String user,String pass){
        this.user=user;
        this.pass=pass;
    }
    public PersonalData(){
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
