package com.example.aluno.ichurch;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.aluno.ichurch.Model.Opening_hours;
import com.example.aluno.ichurch.Model.Photos;
import com.example.aluno.ichurch.Model.PlaceDetail;
import com.example.aluno.ichurch.Remote.IGoogleAPIService;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPlace extends AppCompatActivity {
        ImageView photo;
        RatingBar ratingBar;
        TextView churchName, churchAddress, churchOpenHour,churchPhoneNumber;
        Button btnShowInMap;
        IGoogleAPIService mService;

        PlaceDetail mChurch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);



        mService= Common.getGoogleAPIService();
        ratingBar=(RatingBar) findViewById(R.id.ratingBar);
        btnShowInMap= (Button) findViewById(R.id.btn_show_map);
        churchName= (TextView) findViewById(R.id.church_name);
        churchAddress= (TextView) findViewById(R.id.church_address);
        churchOpenHour= (TextView) findViewById(R.id.church_open_hour);
        churchPhoneNumber= (TextView) findViewById(R.id.church_phone_number);
        photo= (ImageView) findViewById(R.id.photo);

        //Limpar a View
        churchName.setText("");
        churchAddress.setText("");
        churchOpenHour.setText("");


        //Abrir no mapa


        btnShowInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent= new Intent(Intent.ACTION_VIEW, Uri.parse(mChurch.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });

        //Foto
        if(Common.currentResult.getPhotos() != null && Common.currentResult.getPhotos().length>0){
            Picasso.with(this)
                    .load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000))
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(photo);


        }

        //Rating Bar
        /*
        if(Common.currentResult.getRating() != null && !TextUtils.isEmpty(Common.currentResult.getRating())){
            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
        }
        else{
            ratingBar.setVisibility(View.GONE);
        }
        */

        // Não será utlizado no momento o rating bar
        ratingBar.setVisibility(View.GONE);

        //Não será utilizado no momento se a igreja está aberta
        churchOpenHour.setVisibility(View.GONE);

//        //Hora de abertura
//        if(Common.currentResult.getOpening_hours()!= null ){
//            churchOpenHour.setText("Aberto agora:"+Common.currentResult.getOpening_hours().getOpen_now());
//        }
//        else{
//           churchOpenHour.setVisibility(View.GONE);
//        }
         // Usando place Detail para receber nome endereço e telefone da igreja
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                        .enqueue(new Callback<PlaceDetail>() {
                            @Override
                            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                                mChurch= response.body();

                                churchAddress.setText(" "+mChurch.getResult().getFormatted_address());
                                churchName.setText(" "+mChurch.getResult().getName());

                                getSupportActionBar().setDisplayHomeAsUpEnabled(true); //Mostrar o botão
                                getSupportActionBar().setHomeButtonEnabled(true);      //Ativar o botão
                                getSupportActionBar().setTitle(mChurch.getResult().getName());

                                if(mChurch.getResult().getFormatted_phone_number()!=null){
                                    churchPhoneNumber.setText(" "+mChurch.getResult().getFormatted_phone_number());

                                    }else{
                                    churchPhoneNumber.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onFailure(Call<PlaceDetail> call, Throwable t) {

                            }
                        });

    }

    private String getPhotoOfPlace(String photo_reference, int maxWidth) {
        StringBuilder url= new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photo_reference);
        url.append("&key="+getResources().getString(R.string.browser_key));

        return url.toString();
    }

    private String getPlaceDetailUrl(String placeId){
        StringBuilder url= new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+placeId);
        url.append("&language=pt-BR");
        url.append("&key="+getResources().getString(R.string.browser_key));

        return url.toString();




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //Botão adicional na ToolBar
        switch (item.getItemId()) {
            case android.R.id.home:  //ID do seu botão (gerado automaticamente pelo android, usando como está, deve funcionar
                //startActivity(new Intent(this, MainActivity.class));  //O efeito ao ser pressionado do botão (no caso abre a activity)
                finishAffinity();  //Método para matar a activity e não deixa-lá indexada na pilhagem
                break;
            default:break;
        }
        return true;
    }

}
