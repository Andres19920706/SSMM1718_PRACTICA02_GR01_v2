package es.ujaen.ssmm.ssmm1718_practica02_gr01;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ServiceActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "PreferenciasAnDomus" ;
    public static final String SHUSER = "usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        TextView saludo = (TextView) this.findViewById(R.id.SaludoService);
        Log.e("AnDomus","Queando las preferencias compartidas");
        SharedPreferences sharedpreferences = getSharedPreferences(this.MyPREFERENCES, Context.MODE_PRIVATE);
        Log.e("AnDomus","Preferncias precopartidas ok");
        saludo.setText("Hola, "+sharedpreferences.getString(SHUSER,"")+", inicio del servicio");
        Log.e("AnDomus","Datos mostrados");

    }
}
