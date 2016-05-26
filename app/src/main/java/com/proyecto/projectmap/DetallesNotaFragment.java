package com.proyecto.projectmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class DetallesNotaFragment extends Fragment {

    TextView tituloNota,descripcionNota;
    ImageView mediaNota;

    public DetallesNotaFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detalles_nota, container, false);
        Intent i = getActivity().getIntent();

        String key = i.getStringExtra("notaref");
        System.out.println(key+"--------------------------------------------------");
        tituloNota = (TextView)view.findViewById(R.id.tituloNota);
        descripcionNota = (TextView)view.findViewById(R.id.descripcionNota);
        mediaNota = (ImageView)view.findViewById(R.id.MediaNota);


        final Firebase notas = new Firebase("https://proyectouf2.firebaseio.com/").child("notas");

        notas.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot snapshot){

                for (DataSnapshot postSnapshot : snapshot.getChildren()){

                    Nota nota = postSnapshot.getValue(Nota.class);
                    tituloNota.setText("Titulo: " + nota.getTitulo());
                    descripcionNota.setText("Descripción: " + nota.getNota());
                    Picasso.with(getContext()).load( Environment.getExternalStorageDirectory() +
                            "/Map/" + nota.getImagePath()).fit().into(mediaNota);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError){
            }
        });

        return view;
    }
}
