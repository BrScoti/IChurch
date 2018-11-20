package com.example.aluno.ichurch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aluno.ichurch.Model.Church_Hours;
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
    View viewplaceHours,viewListarHorários;
    Button btnInsertSchedules;
    EditText editTextMass, editTextConfession, editTextOther;
    FirebaseDatabase database;
    DatabaseReference church;
    TextView listViewListMass,listViewListConfession,listViewListOther,listViewListUser;
    Boolean isCadastrado;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place_hours);
        //User
        mAuth = FirebaseAuth.getInstance();
        //Database
        database = FirebaseDatabase.getInstance();

        church = database.getReference(Common.currentResult.getId());

        //Botão que abre o viewPlace Hours para cadastrar horários
        btnCadastrarHorários = findViewById(R.id.buttonCadastrarHorários);
        viewplaceHours = findViewById(R.id.layoutCadastrarHorarios);

        //View para listar os horários
        viewListarHorários= findViewById(R.id.layoutListarHorários);

            if (mAuth.getCurrentUser() != null) {

                listarHorarios();

            } else {
                Toast.makeText(getApplicationContext(),"Se logue primeiro",Toast.LENGTH_SHORT).show();
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .build(),
                        123);
            }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                //Toast.makeText(this,"Login bem sucedido!",Toast.LENGTH_LONG).show();

                listarHorarios();

            }


        } else {
            Toast.makeText(this,
                    "Erro de login.",
                    Toast.LENGTH_SHORT)
                    .show();
        }

    }


    public void cadastrarHorarios() {

        viewListarHorários.setVisibility(View.GONE);
        btnCadastrarHorários.setVisibility(View.GONE);
        viewplaceHours.setVisibility(View.VISIBLE);

        btnInsertSchedules = findViewById(R.id.btnRegisterSchedules);

        //Textos que serão enviados
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
                    church.child("user").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    church.child("user_name").setValue(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    if (!editTextMass.getText().toString().isEmpty()) {
                        church.child("schedules_mass").setValue("" + editTextMass.getText());

                    }
                    if (!editTextConfession.getText().toString().isEmpty()) {
                        church.child("schedules_confession").setValue("" + editTextConfession.getText());

                    }
                    if (!editTextOther.getText().toString().isEmpty()) {
                        church.child("schedules_other").setValue("" + editTextOther.getText());

                    }
                    Toast.makeText(getApplicationContext(), "Cadastrado com sucesso", Toast.LENGTH_LONG).show();
                    viewplaceHours.setVisibility(View.GONE);

                    listarHorarios();
                }
            }
        });


    }

    public void listarHorarios(){
        church.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 Church_Hours o= dataSnapshot.getValue(Church_Hours.class);
               // System.out.println(o);
                btnCadastrarHorários.setVisibility(View.GONE);
                viewplaceHours.setVisibility(View.GONE);
                viewListarHorários.setVisibility(View.VISIBLE);
                listViewListMass=findViewById(R.id.textViewListMass);
                listViewListConfession=findViewById(R.id.textViewListConfession);
                listViewListOther=findViewById(R.id.textViewListOther);
                listViewListUser= findViewById(R.id.textViewListUser);
                if(o==null){
                    btnCadastrarHorários.setVisibility(View.VISIBLE);
                    viewListarHorários.setVisibility(View.GONE);
                    isCadastrado=false;
                    cadastrarHorarios();
                    return;
                } else{
                    isCadastrado=true;
                }
                if(o.getUser_name() !=null){
                    listViewListUser.setText("Cadastrado por: "+o.getUser_name());
                }


                if(o.getSchedules_mass() != null){
                    listViewListMass.setText(o.getSchedules_mass());
                }
                else{
                    listViewListMass.setText("Nenhum horário cadastrado");
                }
                if( o.getSchedules_confession() != null){
                    listViewListConfession.setText(o.getSchedules_confession());
                }else{
                    listViewListConfession.setText("Nenhum horário cadastrado");
                }
                if( o.getSchedules_other() != null){
                    listViewListOther.setText(o.getSchedules_other());
                }else{
                    listViewListOther.setText("Nenhum horário cadastrado");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println(error);
                //Toast.makeText(getApplicationContext(), "Nenhum horário cadastrado", Toast.LENGTH_LONG).show();
                btnCadastrarHorários.setVisibility(View.VISIBLE);
                viewListarHorários.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public void setViewCadastrarHorarios(View v){
        System.out.println(church);
        System.out.println(database.getReference());
        if (mAuth.getCurrentUser() != null) {


            if(isCadastrado){
                listarHorarios();

            }
            else {
                cadastrarHorarios();
            }
        } else {
            Toast.makeText(getApplicationContext(),"Se logue primeiro",Toast.LENGTH_SHORT).show();
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    123);
        }
    }

}

