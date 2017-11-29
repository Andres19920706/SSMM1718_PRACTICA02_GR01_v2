package es.ujaen.ssmm.ssmm1718_practica02_gr01;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.lang.NumberFormatException;
import java.net.SocketTimeoutException;
import java.util.regex.Pattern;

import static java.lang.String.*;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    private static final short PORTDEFUAL = 8080;
    public static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "PreferenciasAnDomus" ;
    public static final String MyPREFERENCES2 = "IntentosDeLoggin" ;
    public static final String SESSIONID = "sessionID";
    public static final String SESSIONEXPIRED = "sessionExpired";
    public static final String SHUSER = "usuario";
    public static final String SHPASS = "clave";
    public static final String SHIP = "direccionIP";
    public static final String SHPORT = "puerto";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DialogoAlerta DIALOGO = null;
    private FragmentManager FM = null;
    private TareaAutentica TAREACONN = null;
    private ConnectionUserData DATA = null;
    private OnFragmentInteractionListener mListener;

    public LogginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogginFragment newInstance(String param1, String param2) {
        LogginFragment fragment = new LogginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //Obtenemos el FragmentManager para interactuar con los fragmentos asociados con la actividad de este fragmento.
        FM = getFragmentManager();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_loggin, container, false);

        Button connect = (Button) fragment.findViewById(R.id.button_login);
        final EditText user = (EditText) fragment.findViewById(R.id.editText_login_user);
        final EditText pass = (EditText) fragment.findViewById(R.id.editText_login_pass);
        final EditText ip = (EditText) fragment.findViewById(R.id.editText_login_ip);
        final EditText port = (EditText) fragment.findViewById(R.id.editText_login_port);

        //Comprobamos si hay datos compartidos, para facilitar el inicio de sesion
        sharedpreferences =  getActivity().getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

        user.setText(sharedpreferences.getString(SHUSER,""));
        ip.setText(sharedpreferences.getString(SHIP,""));
        port.setText(sharedpreferences.getString(SHPORT,""));

        //Cuando se hace click sobre el boton de iniciar sesion
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                //Extracción de datos
                String s_user = user.getText().toString();
                String s_pass = pass.getText().toString();
                String s_ip = ip.getText().toString();
                String s_port = port.getText().toString();

                //Valido la dirección IP
                if(!PATTERN.matcher(s_ip).matches()){
                    //Si no tiene el formato de IPv4
                    s_ip = "";
                }
                //Adaptamos el puerto
                short port2;
                try {
                    port2 = Short.parseShort(s_port);
                } catch (java.lang.NumberFormatException ex) {
                    port2 = PORTDEFUAL; //Puerto por defecto
                }

                //Comprobamos que los campos estén bien insertados
                if(s_user.equalsIgnoreCase("")||s_pass.equalsIgnoreCase("")
                        || s_ip.equalsIgnoreCase("")){
                    //Informamos al cliente del error
                    if(s_ip.equalsIgnoreCase("")){
                        DIALOGO = new DialogoAlerta("IP errónea.");
                    }else{
                        Log.e("AnDomus", "Campos Vacios o incorrectos ");
                    }
                    DIALOGO.show(FM, "tagAlerta");

                }else {
                    //"Encapsulamos los datos"
                    DATA = new ConnectionUserData(
                            s_user, s_pass, s_ip, port2
                    );
                    //Preferencias compartidas
                    sharedpreferences =  getActivity().getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(SHUSER, DATA.getUser());
                    //editor.putString(SHPASS, DATA.getPass());
                    editor.putString(SHIP,DATA.getConnectionIP());
                    editor.putString(SHPORT, String.valueOf(DATA.getConnectionPort()));
                    editor.commit();
                    //Establcemos conexión
                    TAREACONN = new TareaAutentica();
                    TAREACONN.execute(DATA);

                }
            }
        });




        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //
    public class TareaAutentica extends AsyncTask<ConnectionUserData,String,String> {
        private ConnectionUserData data;
        public String doInBackground(ConnectionUserData... param) {
            String resul = "NO OK";
            if(param!=null) {
                if (param.length >= 1) {
                    data = param[0];
                    //Log.e("AnDomus", "Los datos son " + data.getConnectionPort());
                    publishProgress("Iniciando sesion");
                    //TODO proceso de autenticación
                    //Aqui me quedo, falta:
                    //Pasar actividad si esta correcto.

                    String datosLogin = "usuario="+data.getUser()+"&clave="+data.getPass();
                    PeticionPost peti = new PeticionPost(data.getConnectionIP(),data.getConnectionPort(),datosLogin);

                    Log.e("AnDomus","URL: "+peti.getDireccion());
                    String re = peti.autentica();
                    publishProgress(""+re);

                    Log.e("AnDomus","Se ha establecido conexión, respuesta  recibida: "+re);
                    //Analizar respuesta
                    //Cabmiar de activdad (guardata todos datos en preferencias compartidas)
                    if(re.split("&").length==2){
                        //Autenticacion con exito
                        String [] datosRespuesta = re.split("&");
                        String sessionID = datosRespuesta[0].split("=")[1];
                        String sessionExpired = datosRespuesta[1].split("=")[1];
                        Log.e("comprobamos A","sessionID: "+sessionID);
                        Log.e("comprobamos B","expired: "+sessionExpired);
                        //Preferencias precompartidas
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(SESSIONEXPIRED, sessionExpired);
                        editor.putString(SESSIONID, sessionID);
                        editor.commit();
//TODO respuesta ok

                        //resultado
                        resul = "OK"; //Si se ha recibido correctamente
                    }


                }
            }
            Log.e("AnDomus","Resultado: "+resul);
            sharedpreferences =  getActivity().getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE); //Para extraer el nombre de usuario
            //
            SharedPreferences sharedpreferences2 = getActivity().getSharedPreferences(MyPREFERENCES2, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences2.edit();
            editor = sharedpreferences2.edit();
            editor.putString(SHUSER, sharedpreferences.getString(SHUSER, ""));
            editor.putString(SESSIONID, resul);
            editor.commit();

            return resul;
        }

        //
        protected void onProgressUpdate(String... progress) {
            //onPostExecute(progress);
            //onPostExecute(progress[0]);
            if(!progress[0].equalsIgnoreCase("OK")) {
                //Creo una instancia de dialogo
                DIALOGO = new DialogoAlerta(progress[0]);
                DIALOGO.show(FM, "tagAlerta");
            }


        }
        /**
        *
        * @param result OK si la operación fue correcta y si no otor valor
        */
        public void onPostExecute(String result){

            if(result.compareToIgnoreCase("OK")==0) {
                Intent nueva = new Intent(getActivity(), ServiceActivity.class);
//                Paso de datos a la otra actividad.
//                nueva.putExtra(SHUSER, data.getUser());
//                nueva.putExtra(SHPASS, data.getPass());
//                nueva.putExtra(SHIP, data.getConnectionIP());
//                nueva.putExtra(SHPORT,data.getConnectionPort());
                startActivity(nueva);
                Log.e("AnDomus","onPpostExecute() ok");

            }
        }
    }

    //Cuadro de dialogo.
    @SuppressLint("ValidFragment")
    public class DialogoAlerta extends DialogFragment {
        public String res;
        public DialogoAlerta (String res){
            this.res = res;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setMessage(this.res)
                    .setTitle("Información")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            return builder.create();
        }
    }
}
