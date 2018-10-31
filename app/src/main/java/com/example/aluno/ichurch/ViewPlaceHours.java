package com.example.aluno.ichurch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class ViewPlaceHours extends AppCompatActivity {
        Button btnCadastrarHorários;
        ListView listaHours;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place_hours);
        btnCadastrarHorários= findViewById(R.id.buttonCadastrarHorários);
        listaHours= findViewById(R.id.listaHoráios);
        mAuth=FirebaseAuth.getInstance();
        btnCadastrarHorários.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser()!=null){
                    startActivity(new Intent(getApplicationContext(),InsertPlaceHours.class));

                }else{
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
                mAuth=FirebaseAuth.getInstance();
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
