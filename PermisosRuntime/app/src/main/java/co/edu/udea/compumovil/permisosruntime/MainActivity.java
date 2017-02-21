package co.edu.udea.compumovil.permisosruntime;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
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
    private TextView lblContacto;
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
        lblContacto = (TextView) findViewById(R.id.lblContacto);

        if(verificarPermisos()) //verificar los permisos
            lblPermiso.setText(estado+" "+str_permitido);
        else
            lblPermiso.setText(estado+" "+str_denegado);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void on_Click(View view){

        if(verificarPermisos()) { //verificar los permisos
            lblPermiso.setText(estado+" "+str_permitido);
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); //Creamos un Intent para abrir los contactos
            startActivityForResult(intent, OPEN_CONTACT); //Lanzamos el Intent y se espera a que se seleccione uno
        }else
            requestPermissions(new String[]{READ_CONTACTS}, MY_PERMISSIONS); //Solicitamos los permisos para abrir los contactos
    }

    public boolean verificarPermisos(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if(checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            return true;
        /*if(shouldShowRequestPermissionRationale(READ_CONTACTS)){//Solicitar permisos cuando los permisos son negados por el usuario en ajustes.
            //Toast.makeText(this, "Los permisos son necesarios para poder usar la aplicación", Toast.LENGTH_SHORT).show();
            //requestPermissions(new String[]{READ_CONTACTS}, MY_PERMISSIONS); //Solicitar permisos
        }else{//Solicitar permisos la primera vez que se instalaló l aplicación
            //requestPermissions(new String[]{READ_CONTACTS}, MY_PERMISSIONS); //Solicitar permisos
        }*/
        return false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case OPEN_CONTACT:
                    Uri contactUri = data.getData(); //Obtenemos la información del contacto seleccionado
                    Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
                    String nombre = "\n"+getResources().getString(R.string.lblContacto)+"\n";
                    if (cursor.moveToFirst()){
                        nombre = nombre + cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    }
                    lblContacto.setText(nombre);
                    break;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                lblPermiso.setText(estado+" "+str_permitido);
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, OPEN_CONTACT);
            }
        }else{
            lblPermiso.setText(estado+" "+str_denegado);
        }

    }

}
