package com.fitlife.ui;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.fitlife.R;
import com.fitlife.model.AnalisisResponse;
import com.fitlife.conexionServer.RetrofitClient;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.core.text.HtmlCompat;


public class AnalisisComidaActivity extends AppCompatActivity {
    private static final int REQ_PHOTO = 1;
    private static final int REQ_PICK  = 2;

    private Uri imageUri;
    private ImageView ivPreview;
    private Button btnTomarFoto, btnElegirGaleria, btnAnalizar;
    private TextView tvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analisis_comida);

        ivPreview      = findViewById(R.id.ivAnalisisPreview);
        btnTomarFoto   = findViewById(R.id.btnTomarFoto);
        btnElegirGaleria = findViewById(R.id.btnElegirGaleria);
        btnAnalizar    = findViewById(R.id.btnAnalizar);
        tvResultado    = findViewById(R.id.tvResultado);

        btnTomarFoto.setOnClickListener(v -> {
            Intent cam = new Intent(ACTION_IMAGE_CAPTURE);
            ContentValues cv = new ContentValues();
            cv.put(MediaStore.Images.Media.TITLE, "AnalisisFoto");
            imageUri = getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
            cam.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cam, REQ_PHOTO);
        });

        btnElegirGaleria.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pick, REQ_PICK);
        });

        btnAnalizar.setOnClickListener(v -> {
            if (imageUri == null) {
                Toast.makeText(this,
                        "Primero selecciona una imagen", Toast.LENGTH_SHORT).show();
            } else {
                analyzeOnDevice();
            }
        });
    }

    @Override
    protected void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        if (res == RESULT_OK) {
            if (req == REQ_PICK && data != null && data.getData() != null) {
                imageUri = data.getData();
            }
            ivPreview.setImageURI(imageUri);
            tvResultado.setText("Imagen lista. Pulsa \"Analizar comida\".");
        }
    }

    private void analyzeOnDevice() {
        try {
            InputImage image = InputImage.fromFilePath(this, imageUri);
            ImageLabeler labeler = ImageLabeling.getClient(
                    ImageLabelerOptions.DEFAULT_OPTIONS
            );
            labeler.process(image)
                    .addOnSuccessListener(labels -> {
                        if (labels.isEmpty()) {
                            tvResultado.setText("No detecté comida.");
                            return;
                        }
                        String foodLabel = labels.get(0).getText();
                        tvResultado.setText("Detectado: " + foodLabel + "\nConsultando USDA…");
                        callAnalyzeApi(foodLabel);
                    })
                    .addOnFailureListener(e ->
                            tvResultado.setText("Error ML Kit: " + e.getMessage())
                    );
        } catch (Exception e) {
            tvResultado.setText("Error leyendo imagen.");
        }
    }

    private void callAnalyzeApi(String etiqueta) {
        Map<String,String> body = new HashMap<>();
        body.put("etiqueta", etiqueta);
        RetrofitClient.getService()
                .analizaEtiqueta(body)
                .enqueue(new Callback<AnalisisResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AnalisisResponse> c,
                                           @NonNull Response<AnalisisResponse> r) {
                        if (r.isSuccessful() && r.body()!=null && r.body().success) {
                            AnalisisResponse ar = r.body();
                            String html =  "<b>Etiqueta:</b> "    + ar.etiqueta     + "<br/>"
                                    + "<b>Calorías:</b> "    + ar.calorias     + " kcal<br/>"
                                    + "<b>Proteínas:</b> "   + ar.proteinas    + " g<br/>"
                                    + "<b>Grasas:</b> "      + ar.grasas       + " g<br/>"
                                    + "<b>Carbohidratos:</b>"+ ar.carbohidratos + " g";

                            tvResultado.setText(
                                    HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
                            );
                        } else {
                            tvResultado.setText("Error al analizar: " +
                                    (r.body()!=null ? r.body().message : r.code()));
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<AnalisisResponse> c,
                                          @NonNull Throwable t) {
                        tvResultado.setText("Fallo de red: " + t.getMessage());
                    }
                });
    }
}
