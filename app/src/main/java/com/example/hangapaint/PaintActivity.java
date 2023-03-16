package com.example.hangapaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PaintActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);

        findViewById(R.id.onClear).setOnClickListener(this);
        findViewById(R.id.onSave).setOnClickListener(this);
        findViewById(R.id.undo).setOnClickListener(this);
        findViewById(R.id.redo).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.onClear) {
            onClear();
        } else if (view.getId() == R.id.onSave) {
            onSave();
        } else if (view.getId() == R.id.undo) {
            undo();
        } else if (view.getId() == R.id.redo) {
            redo();
        }
    }

    private void onSave() {
        PaintView paintView = findViewById(R.id.paintView);
        Bitmap bitmap = Bitmap.createBitmap(paintView.getWidth(), paintView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bitmap);
        paintView.draw(cv);

        String fileName = "res.png";
        FileOutputStream fileOutputStream;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        try {
            fileOutputStream = paintView.getContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void redo() {
        PaintView paintView = findViewById(R.id.paintView);

        if (paintView.stack.isEmpty()){
            Snackbar
                    .make(paintView, "The limits of redo.", Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }
        Path lastRedoPath = paintView.stack.removeLast();
        paintView.paths.addLast(lastRedoPath);
    }

    private void undo() {
        PaintView paintView = findViewById(R.id.paintView);

        if (paintView.paths.isEmpty()) {
            Snackbar
                    .make(paintView, "The limits of undo.", Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }
        Path lastPath = paintView.paths.removeLast();
        paintView.stack.addLast(lastPath);
    }

    private void onClear() {
        PaintView paintView = findViewById(R.id.paintView);
        paintView.paths.clear();
    }
}