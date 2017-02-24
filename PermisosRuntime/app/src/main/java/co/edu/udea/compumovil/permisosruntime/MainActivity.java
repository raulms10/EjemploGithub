package co.edu.udea.compumovil.permisosruntime;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {

    private TextView lblPermiso; //Referenciar el Texview del estado del permiso
    private TextView lblContacto; //Referenciar el Texview del contacto seleccionado
    private final int MY_PERMISSIONS = 100; /*Código que utilizaremos para solicitar los persimos,
                                              puede ser cualquier número*/
    private final int OPEN_CONTACT = 200; /*Código que utilizaremos para abrir la lista de conctatos
                                            puede ser cualquier número*/
    private final String str_permitido = "PERMITIDO";
    private final String str_denegado = "DENEGADO";
    private String estado; //Alamacenará "Estado del permiso:"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        estado = getResources().getString(R.string.lblName); /*Obtiene el String los recursos "Estado del permiso: "
                                                                para mostrarlo debajo del botón Abrir contactos*/
        lblPermiso = (TextView) findViewById(R.id.lblPermiso); //Inicializamos el componente del permiso
        lblContacto = (TextView) findViewById(R.id.lblContacto); //Inicializamos el componente del contacto

        if(verificarPermiso()) //verificamos los permisos y actualizamos el Textview del estado
            lblPermiso.setText(estado+" "+str_permitido);
        else
            lblPermiso.setText(estado+" "+str_denegado);
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void on_Click(View view){

        if(verificarPermiso()) { //verificar los permisos
            lblPermiso.setText(estado+" "+str_permitido); //Actualizamos el Textview del estado del permiso
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); //Creamos un Intent para abrir los contactos
            startActivityForResult(intent, OPEN_CONTACT); //Lanzamos el Intent y se espera a que se seleccione un contacto
        }else
            requestPermissions(new String[]{READ_CONTACTS}, MY_PERMISSIONS); //Solicitamos los permisos para abrir los contactos
    }

    public boolean verificarPermiso(){
        /* Comprobar que la versión del dispositivo si sea la que admite los permisos en tiempo de
        *  de ejecución, es decir, de la versión de Android 6.0 o superior porque para versiones
        *  anteriores basta con colocar el permiso el el archivo manifiesto
        * */
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        /* Aqui es donde comprobamos que los permisos ya hayan sido aceptdos por el usuario */
        if(checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            return true;

        return false; //Los permisos no han sido aceptados por el usuario
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){ //Comprobar que la operación se hay realizado con éxito
            switch (requestCode){ //Código de respuesta
                case OPEN_CONTACT: //Significa que ha regresado de contactos que se abrió en startActivityForResult(intent, OPEN_CONTACT);
                    Uri contactUri = data.getData(); //Obtenemos el URI del contacto
                    Cursor cursor = getContentResolver().query(contactUri, null, null, null, null); /*Con el URI consultamos la
                                                                                            información del contacto seleccionado*/
                    String nombre = "\n"+getResources().getString(R.string.lblContacto)+"\n"; //Obetenemos el String "Contacto seleccionado: "
                    if (cursor.moveToFirst()){ //Verificamos que que contacto existe, la consulta no está vacía
                        nombre = nombre + cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));/*Obtenemos el nombre
                                                                                                  del contacto contenido en la cunsulta(cursor)*/
                    }
                    lblContacto.setText(nombre); //Mostramos el nombre del contacto
                break;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){ //Este es el código que hemos ingresado en el método requestPermisions
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){ /*Verificar que si se haya aceptado el permiso*/
                lblPermiso.setText(estado+" "+str_permitido); //Modificamos el estado del permiso a PERMITIDO
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); //Creamos el Intent para abrir los contactos
                startActivityForResult(intent, OPEN_CONTACT); //Abrimos los contactos
            }else{
                lblPermiso.setText(estado+" "+str_denegado); //Si los permisos fueron rechazos actualizamos el textview del estado del permiso
            }
        }
    }

}
