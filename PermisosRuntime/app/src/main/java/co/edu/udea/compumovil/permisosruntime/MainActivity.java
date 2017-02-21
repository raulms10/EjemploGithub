package co.edu.udea.compumovil.permisosruntime;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {

    private TextView lblPermiso;
    private LinearLayout layout;
    private final int MY_PERMISSIONS = 100;
    private final int OPEN_CONTACT = 200;
    private final String str_permitido = "PERMITIDO";
    private final String str_denegado = "DENEGADO";
    private String estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        estado = getResources().getString(R.string.lblName); //Obtiene el String del Label debajo del boton
        lblPermiso = (TextView) findViewById(R.id.lbnPermiso);
        layout = (LinearLayout) findViewById(R.id.activity_main);

        if(verificarPermisos())
            lblPermiso.setText(estado+" "+str_permitido);
        else
            lblPermiso.setText(estado+" "+str_denegado);



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void on_Click(View view){

        if(verificarPermisos()) {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, OPEN_CONTACT);
        }else
            requestPermissions(new String[]{READ_CONTACTS}, MY_PERMISSIONS); //Solicitar permisos
    }

    public boolean verificarPermisos(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if(checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            return true;
        if(shouldShowRequestPermissionRationale(READ_CONTACTS)){//Solicitar permisos cuando los permisos son negados por el usuario en ajustes.
            //Toast.makeText(this, "Los permisos son necesarios para poder usar la aplicación", Toast.LENGTH_SHORT).show();
            //requestPermissions(new String[]{READ_CONTACTS}, MY_PERMISSIONS); //Solicitar permisos
        }else{//Solicitar permisos la primera vez que se instalaló l aplicación
            //requestPermissions(new String[]{READ_CONTACTS}, MY_PERMISSIONS); //Solicitar permisos
        }
        return false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case OPEN_CONTACT:
                    Toast.makeText(this, "Volvio de contactos", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permisos aceptados", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, OPEN_CONTACT);
            }
        }else{
            Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
        }

    }

}
