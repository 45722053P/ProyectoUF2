package com.proyecto.projectmap;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.Firebase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A placeholder fragment containing a simple view.
 */
public class AnadirNotaFragment extends Fragment {

    EditText titulo, descripcion;
    ImageButton btncamara, btnvideo;
    boolean fototik = false;
    boolean videotik = false;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    double latitude=0;
    double longitude=0;
    Location loc = null;

    public AnadirNotaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);

    }

    //Recogemos el evento con la localizacion que nos da el LocationChangedEvent al que estamos suscritos
    @Subscribe
    public void onLocationChangedEvent(LocationChangedEvent event) {

        latitude = event.getLocation().getLatitude();
        longitude = event.getLocation().getLongitude();

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
                nota.setLatitud(latitude);
                nota.setLongitud(longitude);
                if (fototik) {
                    nota.setImagePath(imageFile());
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

        fototik = true;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }
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
}
