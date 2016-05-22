package com.proyecto.projectmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetallesNotaFragment extends Fragment {

    TextView tituloNota,descripcionNota;
    ImageView mediaNota;

    public DetallesNotaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detalles_nota, container, false);

        tituloNota = (TextView)view.findViewById(R.id.tituloNota);
        descripcionNota = (TextView)view.findViewById(R.id.descripcionNota);
        mediaNota = (ImageView)view.findViewById(R.id.MediaNota);


        //Hacemos una referencia para recorrer todas las notas y añadir cada una a un marcador
        final Firebase notas = new Firebase("https://proyectouf2.firebaseio.com/").child("notas");

        notas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //Recorremos todas las notas que haya en ese momento
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Nota nota = postSnapshot.getValue(Nota.class);
                    tituloNota.setText("Titulo: " + nota.getTitulo());
                    descripcionNota.setText("Descripción: " + nota.getNota());
                    File imagePath = new File(nota.getImagePath());
                    Picasso.with(getContext()).load(imagePath).fit().into(mediaNota);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        return view;
    }
}
