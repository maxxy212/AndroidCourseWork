package com.greenwich.mexpense.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.greenwich.mexpense.R;
import com.greenwich.mexpense.databinding.ActivityShowBinding;
import com.greenwich.mexpense.model.Trip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.realm.Realm;

public class ShowActivity extends AppCompatActivity {

    private ActivityShowBinding binding;
    private static final String SHOW = "show_report";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_show);
        Realm realm = Realm.getDefaultInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        int id = getIntent().getIntExtra(SHOW, 0);
        Trip trip = realm.where(Trip.class).equalTo("id", id).findFirst();
        for (int i = 0; i < (trip != null ? trip.expense.size() : 0); i++) {
            String originalText = binding.expenseType.getText().toString();
            binding.expenseType.setText(originalText + " " + trip.expense.get(i));
        }
        binding.setTrip(trip);
    }

    public static void start(Context context, int tripID){
        Intent intent = new Intent(context, ShowActivity.class);
        intent.putExtra(SHOW, tripID);
        context.startActivity(intent);
    }

    private void sharePDF() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(binding.viewWrapper.getWidth(), binding.viewWrapper.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        binding.viewWrapper.draw(page.getCanvas());
        binding.viewWrapper.invalidate();
        document.finishPage(page);
        String filepath = null;
        try {
            filepath = savePDF();
            document.writeTo(new FileOutputStream((filepath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        document.close();
        shareReceipt(filepath);
    }

    private String savePDF() throws IOException {
        UUID uuid = UUID.randomUUID();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File doc = File.createTempFile(
                uuid.toString(),  /* key */
                ".pdf",         /* suffix */
                storageDir      /* directory */
        );

        return doc.getAbsolutePath();
    }

    private void shareReceipt(String filePath) {
        File file = new File(filePath);
        Uri uri = FileProvider.getUriForFile(this, "com.greenwich.mexpense.fileprovider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Receipt"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            sharePDF();
        }
        return super.onOptionsItemSelected(item);
    }
}

//    private void testPDF() {
//        bitmap = LoadBitmap(binding.viewWrapper, binding.viewWrapper.getWidth(), binding.viewWrapper.getHeight());
//        createPdf();
//    }
//
//    private Bitmap LoadBitmap(View v, int width, int height) {
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        v.draw(canvas);
//        return bitmap;
//    }
//
//    private void createPdf() {
//        //WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        //  Display display = wm.getDefaultDisplay();
//        DisplayMetrics displaymetrics = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        float hight = displaymetrics.heightPixels;
//        float width = displaymetrics.widthPixels;
//
//        int convertHighet = (int) hight, convertWidth = (int) width;
//
//        PdfDocument document = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
//        PdfDocument.Page page = document.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//
//        Paint paint = new Paint();
//        canvas.drawPaint(paint);
//
//        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);
//
//        paint.setColor(Color.BLUE);
//        canvas.drawBitmap(bitmap, 0, 0, null);
//        document.finishPage(page);
//        String filepath = null;
//        try {
//            filepath = savePDF();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        shareReceipt(filepath);
//
////        // write the document content
////        String targetPdf = "/sdcard/page.pdf";
////        File filePath;
////        filePath = new File(targetPdf);
////        try {
////            document.writeTo(new FileOutputStream(filePath));
////
////        } catch (IOException e) {
////            e.printStackTrace();
////            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
////        }////////////////////
////
////        // close the document
////        document.close();
////        Toast.makeText(this, "successfully pdf created", Toast.LENGTH_SHORT).show();
//
//    }