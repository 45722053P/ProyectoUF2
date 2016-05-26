package com.proyecto.projectmap;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.File;

/**
 * A placeholder fragment containing a simple view.
 */
public class AnadirNotaFragment extends Fragment implements LocationListener {
    String nom;
    EditText titulo, descripcion;
    ImageButton btncamara, btnvideo;
    boolean fototik = false;
    boolean videotik = false;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private double longitud;
    private double latitud;

    public AnadirNotaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_anadir_nota, container, false);

        titulo = (EditText) view.findViewById(R.id.tituloanadir);
        descripcion = (EditText) view.findViewById(R.id.descripcionanadir);

        //Imagebuttons. Pero tambien se podria hacer con el layout qque coge el imagebutton y el texto
        btnvideo = (ImageButton) view.findViewById(R.id.videoButton);
        btncamara = (ImageButton) view.findViewById(R.id.cameraButton);


        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideo();
            }
        });

        btncamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        //Le decimos a Firebase que este sera el contexto
        Firebase.setAndroidContext(getContext());

        //Creamos una referencia a nuestra bd de Firebase
        Firebase ref = new Firebase("https://proyectouf2.firebaseio.com/");

        //Y tendra un hijo que es donde guardara las notas y la localizacion de esta
        final Firebase notas = ref.child("notas");

        FloatingActionButton sendNote = (FloatingActionButton) view.findViewById(R.id.enviarNota);
        sendNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Firebase note = notas.push();

                Nota nota = new Nota();
                nota.setTitulo(titulo.getText().toString());
                nota.setNota(descripcion.getText().toString());
                nota.setLatitud(latitud);
                nota.setLongitud(longitud);
                if (fototik) {
                    nota.setImagePath(nom);
                    fototik = false;
                }
                if (videotik) {
                    nota.setVideoPath(videoFile());
                    videotik = false;
                }
                note.setValue(nota);

                titulo.setText("");
                descripcion.setText("");

            }
        });
        return view;
    }


    public void openVideo(){

        videotik = true;
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getActivity().getPackageManager()) != null){

            startActivityForResult(takeVideoIntent, REQUEST_IMAGE_CAPTURE);

        }
    }

    public void openCamera(){
        fototik =true;
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //Creamos una carpeta en la memeria del terminal
        File imagesFolder = new File(
                Environment.getExternalStorageDirectory(), "Map");
        imagesFolder.mkdirs();
        //añadimos el nombre de la imagen
        nom = NomFoto()+".jpg";
        File image = new File(imagesFolder, nom);
        Uri uriSavedImage = Uri.fromFile(image);
        //Le decimos al Intent que queremos grabar la imagen
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        //Lanzamos la aplicacion de la camara con retorno (forResult)
        startActivityForResult(cameraIntent, 1);
    }
    private String NomFoto() {
        long rand = (long) Math.floor(Math.random() * 5871);
        String photoCode = "pic_" + rand;
        return photoCode;
    }

    public String videoFile() {

        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getActivity().managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToLast();

        String path =  cursor.getString(column_index_data);

        return path;
    }

    public String imageFile() {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        String path =  cursor.getString(column_index_data);

        return path;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.longitud = location.getLongitude();
        this.latitud = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //no se utiliza
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getContext(), "El GPS esta encendido",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getContext(), "El Gps esta apagado",
                Toast.LENGTH_SHORT).show();
    }
}
