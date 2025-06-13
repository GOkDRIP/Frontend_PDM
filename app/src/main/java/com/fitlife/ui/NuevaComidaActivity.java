// File: NuevaComidaActivity.java
package com.fitlife.ui;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.fitlife.R;
import com.fitlife.conexionServer.RetrofitClient;
import com.fitlife.conexionServer.FitLifeService;
import com.fitlife.model.GenericResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class NuevaComidaActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_IMAGE_PICK = 101;

    private Uri imageUri;
    private ImageView ivPreview;
    private EditText etNombre, etCalorias, etCarbohidratos, etProteinas, etGrasas, etDescripcion;
    private Button btnTomarFoto, btnElegirGaleria, btnPublicar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_comida);

        ivPreview        = findViewById(R.id.ivPreview);
        btnTomarFoto     = findViewById(R.id.btnTomarFoto);
        btnElegirGaleria = findViewById(R.id.btnElegirGaleria);
        etNombre         = findViewById(R.id.etNombre);
        etCalorias       = findViewById(R.id.etCalorias);
        etCarbohidratos  = findViewById(R.id.etCarbohidratos);
        etProteinas      = findViewById(R.id.etProteinas);
        etGrasas         = findViewById(R.id.etGrasas);
        etDescripcion    = findViewById(R.id.etDescripcion);
        btnPublicar      = findViewById(R.id.btnPublicar);

        btnTomarFoto.setOnClickListener(v -> dispatchTakePictureIntent());
        btnElegirGaleria.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pick, REQUEST_IMAGE_PICK);
        });

        btnPublicar.setOnClickListener(v -> uploadFood());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null && data.getData() != null) {
                imageUri = data.getData();
            }
            ivPreview.setImageURI(imageUri);
        }
    }

    private void dispatchTakePictureIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nueva_comida");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Foto desde cámara");
        imageUri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void uploadFood() {
        if (imageUri == null) {
            Toast.makeText(this, "Debes seleccionar una foto", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] bytes = readBytesFromUri(imageUri);
            RequestBody reqFile = RequestBody.create(
                    MediaType.parse(getContentResolver().getType(imageUri)), bytes);
            MultipartBody.Part imagenPart = MultipartBody.Part.createFormData(
                    "imagen", "comida.jpg", reqFile);

            RequestBody nombre = RequestBody.create(
                    MediaType.parse("text/plain"), etNombre.getText().toString().trim());
            RequestBody calorias = RequestBody.create(
                    MediaType.parse("text/plain"), etCalorias.getText().toString().trim());
            RequestBody carbohidratos = RequestBody.create(
                    MediaType.parse("text/plain"), etCarbohidratos.getText().toString().trim());
            RequestBody proteinas = RequestBody.create(
                    MediaType.parse("text/plain"), etProteinas.getText().toString().trim());
            RequestBody grasas = RequestBody.create(
                    MediaType.parse("text/plain"), etGrasas.getText().toString().trim());
            RequestBody descripcion = RequestBody.create(
                    MediaType.parse("text/plain"), etDescripcion.getText().toString().trim());

            FitLifeService svc = RetrofitClient.getService();
            svc.publicarComida(imagenPart, nombre, calorias, carbohidratos, proteinas, grasas, descripcion)
                    .enqueue(new Callback<GenericResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<GenericResponse> call,
                                               @NonNull Response<GenericResponse> resp) {
                            if (resp.isSuccessful() && resp.body() != null) {
                                GenericResponse gr = resp.body();
                                if (gr.success) {
                                    Toast.makeText(NuevaComidaActivity.this,
                                            gr.message != null ? gr.message : "Publicado",
                                            Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(NuevaComidaActivity.this,
                                            gr.message != null ? gr.message : "Error al publicar",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(NuevaComidaActivity.this,
                                        "Error HTTP: " + resp.code(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {
                            Toast.makeText(NuevaComidaActivity.this,
                                    "Fallo de red: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error leyendo la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] readBytesFromUri(Uri uri) throws IOException {
        try (InputStream is = getContentResolver().openInputStream(uri);
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        }
    }
}
