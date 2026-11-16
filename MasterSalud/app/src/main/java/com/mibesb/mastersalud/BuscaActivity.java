package com.mibesb.mastersalud;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuscaActivity extends AppCompatActivity {

    String nuhsa;
    EditText etNombre, etApellidos;
    TextView tvGrupoSanguineo;
    FirebaseDatabase database;
    DatabaseReference puntoAcceso;
    Paciente pacienteActual;
    ValueEventListener listenerPaciente; // Guardamos el listener para poder detenerlo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        nuhsa = getIntent().getStringExtra(Constantes.nuhsa);

        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        tvGrupoSanguineo = findViewById(R.id.tvGrupoSanguineo);

        database = FirebaseDatabase.getInstance();
        puntoAcceso = database.getReference(Constantes.pacientes + "/" + nuhsa);

        listenerPaciente = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (nuhsa != null && !nuhsa.isEmpty() && dataSnapshot.getValue() != null) {
                    pacienteActual = dataSnapshot.getValue(Paciente.class);
                    if (pacienteActual != null) {
                        etNombre.setText(pacienteActual.getNombre());
                        etApellidos.setText(pacienteActual.getApellidos());
                        tvGrupoSanguineo.setText(pacienteActual.getGrupoSanguineo());
                    }
                } else {
                    Toast.makeText(BuscaActivity.this, "Paciente no encontrado",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BuscaActivity.this, "Error de lectura: " + databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        };

        // Usamos addListenerForSingleValueEvent para leer una sola vez al abrir la pantalla
        puntoAcceso.addListenerForSingleValueEvent(listenerPaciente);
    }

    public void actualizarPaciente(View view) {
        if (pacienteActual != null) {
            String nuevoNombre = etNombre.getText().toString().trim();
            String nuevosApellidos = etApellidos.getText().toString().trim();
            String grupo = pacienteActual.getGrupoSanguineo();

            if (nuevoNombre.isEmpty() || nuevosApellidos.isEmpty()) {
                Toast.makeText(this, "Rellena nombre y apellidos", Toast.LENGTH_SHORT).show();
                return;
            }

            Paciente actualizado = new Paciente(nuevoNombre, nuevosApellidos, grupo, nuhsa);
            DatabaseReference root = database.getReference();

            root.child(Constantes.pacientes).child(nuhsa).setValue(actualizado);
            root.child(grupo).child(nuhsa).setValue(actualizado);

            Toast.makeText(this, "Paciente actualizado", Toast.LENGTH_SHORT).show();
            finish(); // Volvemos a la lista o pantalla anterior tras actualizar
        }
    }

    public void eliminarPaciente(View view) {
        if (pacienteActual != null) {
            // Desconectamos el listener antes de borrar para que no salte "Paciente no encontrado"
            if (listenerPaciente != null) {
                puntoAcceso.removeEventListener(listenerPaciente);
            }

            DatabaseReference root = database.getReference();
            root.child(Constantes.pacientes).child(nuhsa).removeValue();
            root.child(pacienteActual.getGrupoSanguineo()).child(nuhsa).removeValue();

            Toast.makeText(this, "Paciente eliminado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiamos el listener al cerrar la actividad para evitar notificaciones fantasma
        if (puntoAcceso != null && listenerPaciente != null) {
            puntoAcceso.removeEventListener(listenerPaciente);
        }
    }
}