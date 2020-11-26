package com.pief.facampnetwork;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities extends Application implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static Activity currentActivity;
    private static Fragment currentFragment;
    private static boolean usingFragment = false;
    private static String editItemURL = "http://192.168.0.79/scripts/editItem.php";
    private static String deleteItemURL = "http://192.168.0.79/scripts/deleteItem.php";

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
    
    public static void blockViewInputs(View[] views){
        for(View view : views){
            if(view instanceof EditText){
                ((EditText) view).setCursorVisible(false);
            }
            view.setFocusable(false);
        }
    }

    public static boolean checarCamposAleatorios(EditText[] editTexts){
        boolean validado = true;
        for(EditText editText : editTexts){
            if(editText.getText().length() == 0){
                editText.setError("Campo obrigatório.");
                editText.requestFocus();
                validado = false;
            }
        }
        return validado;
    }

    public static String formatarDataString(String data){
        String ano = data.substring(0, 4);
        String mes = data.substring(5, 7);
        String dia = data.substring(8, 10);
        String horario = data.substring(11, 16);
        String dataFormatada = dia + "/" + mes + "/" + ano + " - " + horario;
        return dataFormatada;
    }

    public static void showConfirmacaoDelete(String tabela, int id, Activity activity){
        currentActivity = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Deletar " + tabela + "?");
        builder.setTitle("Confirmação de exclusão de " + tabela);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int option) {
                criarDeleteStringRequest(tabela, id);
                Intent telaPrincipal = new Intent(activity.getApplicationContext(), MainActivity.class);
                activity.startActivity(telaPrincipal);
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int option) {
            }
        });
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    public static void criarDateTimePicker(EditText editText, Activity activity){
        editText.setInputType(InputType.TYPE_NULL);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker(editText, activity);
            }
        });
    }

    private static void showDateTimePicker(EditText editText, Activity activity){
        int theme = 0;
        int nightModeFlags = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(nightModeFlags == Configuration.UI_MODE_NIGHT_YES){
            theme = android.R.style.Theme_Holo_Dialog_MinWidth;
        }
        else{
            theme = android.R.style.Theme_Holo_Light_Dialog_MinWidth;
        }

        final Calendar calendar =  Calendar.getInstance();
        int finalTheme = theme;
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, finalTheme, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog timePickerDialog = new TimePickerDialog(activity, finalTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");

                        MainActivity.lastSelectedDateTime = sqlDateFormat.format(calendar.getTime());
                        editText.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, 12, 0, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        }, 2020, 10, 27);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    public static void criarDeleteStringRequest(String tabela, int id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, deleteItemURL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Log.i("Delete", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(currentActivity, jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                }catch(Exception e){
                    Log.e("Delete", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Delete", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("tabela", tabela);
                return params;
            }
        };

        Singleton.getInstance(currentActivity).addToRequestQueue(stringRequest);
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

    public static void adicionarDateChangeListener(EditText editText, String tabela, String campo, int id, Activity activity){
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
                criarEditStringRequest(tabela, campo, MainActivity.lastSelectedDateTime, id);
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
                Log.i("Edit", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean erro = jsonObject.getBoolean("erro");
                    if(erro){
                        Toast.makeText(currentActivity, jsonObject.getString("mensagem"), Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.e("Edit", e.getMessage());
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Edit", error.getMessage());
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