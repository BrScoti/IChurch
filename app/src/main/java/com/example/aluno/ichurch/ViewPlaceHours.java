package com.example.aluno.ichurch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewPlaceHours extends AppCompatActivity {
    Button btnCadastrarHorários;
    ListView listaHours;
    View viewplaceHours;
    Button btnInsertSchedules;
    EditText editTextMass, editTextConfession, editTextOther;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place_hours);
        btnCadastrarHorários = findViewById(R.id.buttonCadastrarHorários);
        listaHours = findViewById(R.id.listaHorarios);
        mAuth = FirebaseAuth.getInstance();
        viewplaceHours=findViewById(R.id.layoutCadastrarHorarios);
        btnCadastrarHorários.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), InsertPlaceHours.class));

                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .build(),
                            123);
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                //Toast.makeText(this,"Login bem sucedido!",Toast.LENGTH_LONG).show();
                mAuth = FirebaseAuth.getInstance();
                viewplaceHours.setVisibility(View.VISIBLE);
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
                            viewplaceHours.setVisibility(View.GONE);

                            church.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    String value = dataSnapshot.getValue(String.class);
                                    Log.d("OPA", "Value is: " + value);
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w("OPA", "Failed to read value.", error.toException());
                                }
                            });
                        }

                    }
                });
                //Toast.makeText(getApplicationContext(),mAuth+"",Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this,
                        "Erro de login.",
                        Toast.LENGTH_SHORT)
                        .show();


            }
        }
    }


}
