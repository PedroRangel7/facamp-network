package com.pief.facampnetwork;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Utilities extends Application implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static Activity currentActivity;
    private static Fragment currentFragment;
    private static boolean usingFragment = false;
    private static String editItemURL = "http://192.168.0.79/scripts/editItem.php";

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    private static Context getContext() {
        return instance.getApplicationContext();
    }

    public static Bitmap getBitmap(String imageString){
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }

    public static Bitmap getBitmap(byte[] imageBytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return bitmap;
    }

    public static String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imagemBytes = byteArrayOutputStream.toByteArray();
        String imagemString = Base64.encodeToString(imagemBytes, Base64.DEFAULT);
        return imagemString;
    }

    public static String formatPrice(double preco){
        String precoString = "R$" + String.format("%.2f", preco);
        precoString = precoString.replace(".", ",");
        return precoString;
    }

    public static void adicionarTextChangeListener(EditText editText, String tabela, String campo, int id, Activity activity){
        currentActivity = activity;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                criarEditStringRequest(tabela, campo, editText.getText().toString(), id);
            }
        });
    }

    public static void adicionarImageViewChangeListener(ImageView imageView, Activity activity){
        currentActivity = activity;
        usingFragment = false;
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(currentActivity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        currentActivity.requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else
                        escolherImagem();
                }
                else
                    escolherImagem();
            }
        });
    }

    public static void adicionarImageViewChangeListener(ImageView imageView, Activity activity, Fragment fragment){
        currentActivity = activity;
        currentFragment = fragment;
        usingFragment = true;
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(currentActivity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        currentActivity.requestPermissions(permissions, PERMISSION_CODE);
                    }
                    else
                        escolherImagem();
                }
                else
                    escolherImagem();
            }
        });
    }

    public static void criarEditStringRequest(String tabela, String campo, String valor, int id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, editItemURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Profile", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(erro){
                        Toast.makeText(Utilities.getContext(), jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.e("Profile", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Profile", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("campo", campo);
                params.put("valor", valor);
                params.put("tabela", tabela);
                return params;
            }
        };

        Singleton.getInstance(currentActivity).addToRequestQueue(stringRequest);
    }

    private static void escolherImagem(){
        Intent escolherImg = new Intent(Intent.ACTION_PICK);
        escolherImg.setType("image/*");
        if(!usingFragment)
            currentActivity.startActivityForResult(escolherImg, IMAGE_PICK_CODE);
        else
            currentFragment.startActivityForResult(escolherImg, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                escolherImagem();
            }
        }
    }

    public static void onActivityResultImageChange(int requestCode, int resultCode, @Nullable Intent data, ImageView imageView, String tabela, String campo, int id) {
        if (resultCode == currentActivity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            try {
                InputStream inputStream = currentActivity.getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                Utilities.criarEditStringRequest(tabela, campo, bitmapToString(bitmap), id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
