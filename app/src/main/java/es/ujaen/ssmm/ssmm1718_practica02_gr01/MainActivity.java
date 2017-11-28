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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "PreferenciasAnDomus" ;
    public static final String SESSIONID = "sessionID";
    public static final String SESSIONEXPIRED = "sessionExpired";
    public static final String SHUSER = "usuario";
    public static final String SHPASS = "clave";
    public static final String SHIP = "direccionIP";
    public static final String SHPORT = "puerto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation rotate;
        ImageView logo = (ImageView)findViewById(R.id.logo_welcome);
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        logo.setAnimation(rotate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cerrarSesion) {
            //Obtenemos los datos precompartidos
            SharedPreferences sharedpreferences = getSharedPreferences(this.MyPREFERENCES, Context.MODE_PRIVATE);
            //Obtenmos el identficiador de sesion
            String sessionID = sharedpreferences.getString(SESSIONID,"");

            //Comprobmoas si hay sesión
            if(!sessionID.equalsIgnoreCase("")) {
                //Hay sesion.
                //Cerrar session (Limpiamos la lista de preferencias compartidas)
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                //Volvemos al inicio de la app.
                WelcomenFragment fragment = new WelcomenFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.Contenedor, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                //Mostramos un cuadro de diálogo
                DialogoAlerta alerta = new DialogoAlerta("Sesion cerrada");
                alerta.show(fm, "aviso");
            }else{
                //No hay sesion
                //Mostramos un cuadro de diálogo
                DialogoAlerta alerta = new DialogoAlerta("No hay sesion");
                FragmentManager fm = getFragmentManager();
                alerta.show(fm, "aviso");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_loggin) {
            // Handle the camera action
            Log.e("AnDomus","Cargando fragmento de bienvenida");
            LogginFragment fragment = new LogginFragment();
            //Comprobamos si ya ha iniciado sesion
            Boolean autenticar = false;
            //Preferencias compartidas
            sharedpreferences =  getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            //Obtenmos el identficiador de sesion
            String sessionID = sharedpreferences.getString(SESSIONID,"");

            //Comprobmoas si hay sexión
            if(!sessionID.equalsIgnoreCase("")){
                //Hay sesion.
                String sessionExpired = sharedpreferences.getString(SESSIONEXPIRED,"");

                //Extraemos la hora actual
                Date fechaActual = new Date();
                SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss");
                String horaActual = dt1.format(fechaActual);

                //Realizamos la operacion para obtener la direncia de fechas
                Date fechaA = null;
                Date fechaB = null;
                try {
                    fechaA = dt1.parse(sessionExpired);
                    fechaB = dt1.parse(horaActual);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Calculo
                Log.e("TIEMPOS","FechaActual = "+horaActual+" sessionExpired="+sessionExpired);
                if(fechaA!=null && fechaB!=null) {
                    Long time = fechaA.getTime() - fechaB.getTime();
                    Log.e("TIEMPOS", "FechaActual-sessionExpired= " + time);
                    //Accion
                    if(time < 0){
                        //Sesion Expirada
                        autenticar = true;
                    }
                }
            }else{
                autenticar = true;
            }

            if(autenticar) {
                //Cambimos de fragemnto para que inicie sesion.
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.Contenedor, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }else{
                //Cambiamos de actidad
                FragmentManager fm = getFragmentManager();
                //Mostramos un cuadro de diálogo
                Toast.makeText(this, "Sesion ya iniciada", Toast.LENGTH_LONG).show();
                //Cambiamos a la actividad 2
                Intent nueva = new Intent(this, ServiceActivity.class);
                startActivity(nueva);
            }

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
