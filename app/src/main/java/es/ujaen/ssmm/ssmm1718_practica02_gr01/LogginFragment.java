package es.ujaen.ssmm.ssmm1718_practica02_gr01;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                String s_user = user.getText().toString();
                String s_pass = pass.getText().toString();
                String s_ip = ip.getText().toString();
                //Valido la dirección IP
                if(!PATTERN.matcher(s_ip).matches()){
                    s_ip = "192.168.0.19"; //Dirección por defecto.
                }
                Log.e("direccionIP", "AnDomus: Direccion IP- "+s_ip);

                //Estragio el puerto
                String s_port = port.getText().toString();
                short port2;

                try {
                    port2 = Short.parseShort(s_port);
                } catch (java.lang.NumberFormatException ex) {
                    port2 = 8080; //Puerto por defecto
                }
                ConnectionUserData data = new ConnectionUserData(
                        s_user, s_pass, s_ip, port2
                );


                TareaAutentica tarea = new TareaAutentica();
                tarea.execute(data);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

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
        private ConnectionUserData data; public String doInBackground(ConnectionUserData... param){
            String resul = "NO OK";
            if(param!=null) {
                if (param.length >= 1) {
                    data = param[0];
                    Log.e("o", "E" + data.getConnectionPort());
                    publishProgress("MENSAJE RECIBIDO CORRECTAMENTE");
                    //TODO proceso de autenticación
                    //Aqui me quedo, falta:
                        //Insertar clase de http.
                        //Crear servidor.
                        //Establcer conexión con servidor.
                        //Modificar diálogo.
                        //Crear cookie de sesión.
                        //Pasar actividad si esta correcto.

                    PeticionPost peti = new PeticionPost();

                    String re = peti.autentica();
                    Log.e("AnDomus","Se ha establecido conexión, respuesta "+re);
                    //Analizar respuesta


                    resul = "OK"; //Si se ha recibido correctamente
                }
            }

            return resul;
        }

        //
        protected void onProgressUpdate(String... progress) {
            onPostExecute(progress);
            Log.e("makemachine", "onPostExecute(): " +progress[0]);
            //onPostExecute(progress[0]);

        }
        //
        protected void onPostExecute(String... result) {


            Log.e("makemachine", "onPostExecute(): " + result[0]);
            Toast.makeText( getActivity(), ""+result[0], Toast.LENGTH_LONG).show();
            //Creo una instancia de dialogo
            FragmentManager fragmentManager = getFragmentManager();
            DialogoAlerta dialogo = new DialogoAlerta(result[0]);
            dialogo.show(fragmentManager, "tagAlerta");

        }
        /**
        *
        * @param result OK si la operación fue correcta y si no otor valor
        */
        public void onPostExecute(String result){
            if(result.compareToIgnoreCase("OK")==0) {
                //Intent nueva = new Intent(getActivity(), ServiceActivity.class);
                //nueva.putExtra(ServiceActivity.PARAM_USER, data.getUser());
                //nueva.putExtra("param_pass", data.getPass());
                //nueva.putExtra("param_ip", data.getConnectionIP());
                //nueva.putExtra("param_port", data.getConnectionPort());
                //startActivity(nueva);
                Log.e("o","ok");
                   // Toast.makeText(getContext(), "OK autenticando a " +data.getUser(), Toast.LENGTH_LONG).show();
                }else
                {
                    Log.e("o","no ok");
                    //Toast.makeText(getContext(), "Error autenticando a " +data.getUser(), Toast.LENGTH_LONG).show();
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
