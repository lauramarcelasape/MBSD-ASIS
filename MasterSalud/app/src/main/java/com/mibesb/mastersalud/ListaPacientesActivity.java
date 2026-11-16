package com.mibesb.mastersalud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaPacientesActivity extends AppCompatActivity {

    ListView lvPacientes;
    ArrayList<String> listaStrings;
    ArrayList<Paciente> listaObjetos; // Lista paralela para recuperar el objeto al hacer clic
    ArrayAdapter<String> adapter;
    DatabaseReference refPacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pacientes);

        lvPacientes = findViewById(R.id.lvPacientes);
        listaStrings = new ArrayList<>();
        listaObjetos = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaStrings);
        lvPacientes.setAdapter(adapter);

        refPacientes = FirebaseDatabase.getInstance().getReference(Constantes.pacientes);

        refPacientes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaStrings.clear();
                listaObjetos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Paciente p = snapshot.getValue(Paciente.class);
                    if (p != null) {
                        listaObjetos.add(p);
                        listaStrings.add(p.getNuhsa() + " - " + p.getNombre() + " "
                                + p.getApellidos() + " (" + p.getGrupoSanguineo() + ")");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ListaPacientesActivity.this, "Error al leer lista", Toast.LENGTH_SHORT).show();
            }
        });

        // Al pulsar sobre cualquier fila de la lista, abrimos BuscaActivity con su NUHSA
        lvPacientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Paciente seleccionado = listaObjetos.get(position);
                Intent intent = new Intent(ListaPacientesActivity.this, BuscaActivity.class);
                intent.putExtra(Constantes.nuhsa, seleccionado.getNuhsa());
                startActivity(intent);
            }
        });
    }
}