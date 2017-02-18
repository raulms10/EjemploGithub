package co.edu.udea.compumovil.permisosruntime;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button btnCamara = (Button) findViewById(R.id.btnCamara);
        lblPermiso = (TextView) findViewById(R.id.lbnPermiso);
        layout = (LinearLayout) findViewById(R.id.activity_main);


        if(verificarPermisos())
            lblPermiso.setText(lblPermiso.getText().toString()+"PERMITIDO");
        else
            lblPermiso.setText(lblPermiso.getText().toString()+"DENEGADO");


    }

    public void on_Click(View view){
        //Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //intent.setType("image/*");
        //startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), SELECT_PICTURE);


    }

    public boolean verificarPermisos(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if(checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            return true;

        if(shouldShowRequestPermissionRationale(READ_CONTACTS)){
            Toast.makeText(this, "Los permisos son necesarios para poder usar la aplicación", Toast.LENGTH_SHORT).show();


            Snackbar.make(layout, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{READ_CONTACTS}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{READ_CONTACTS}, MY_PERMISSIONS);
        }

        return false;
    }





}
