package com.example.aluno.ichurch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertPlaceHours extends AppCompatActivity {
    Button btnInsertSchedules;
    EditText editTextMass, editTextConfession, editTextOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_place_hours);
        btnInsertSchedules = findViewById(R.id.btnRegisterSchedules);
        editTextMass = findViewById(R.id.editTextMass);
        editTextConfession = findViewById(R.id.editTextConfession);
        editTextOther = findViewById(R.id.editTextOther);
        btnInsertSchedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextMass.getText().toString().isEmpty() && editTextConfession.getText().toString().isEmpty() && editTextOther.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Escreva pelo menos um horário", Toast.LENGTH_LONG).show();
                } else {
                    // Costruindo referência da database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference church = database.getReference(Common.currentResult.getId());
                    church.child("user").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    if (!editTextMass.getText().toString().isEmpty()) {
                        church.child("schedules-mass").setValue("" + editTextMass.getText());

                    }
                    if (!editTextConfession.getText().toString().isEmpty()) {
                        church.child("schedules-confession").setValue("" + editTextConfession.getText());

                    }
                    if (!editTextOther.getText().toString().isEmpty()) {
                        church.child("schedules-other").setValue("" + editTextOther.getText());

                    }
                   Toast.makeText(getApplicationContext(),"Cadastrado com sucesso",Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

    }


}
