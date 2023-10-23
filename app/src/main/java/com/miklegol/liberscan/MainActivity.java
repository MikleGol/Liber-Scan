package com.miklegol.liberscan;

import static android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

import androidx.annotation.FloatRange;
import androidx.annotation.FractionRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_CODE = 1313;
    LinearLayout linear_dialog;
    RelativeLayout relativeLayout;
    ScrollView scrollView;
    FloatingActionButton fab;
    RadioGroup radioGroup;
    RadioButton rad_sans, rad_serif, rad_monospace;
    ImageButton btn_beige, btn_red, btn_yellow;
    Button button_capture, button_copy;
    TextView textview_data;
    Bitmap bitmap;
    Dialog dialog;
    ActionBar actionBar;
    Window w;
    Typeface type_sans = Typeface.SANS_SERIF;
    Typeface type_serif = Typeface.SERIF;
    Typeface type_monospace = Typeface.MONOSPACE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayout = findViewById(R.id.relative_layout);
        scrollView = findViewById(R.id.scroll_view);

        button_capture = findViewById(R.id.button_capture);
        button_copy = findViewById(R.id.button_copy);
        textview_data = findViewById(R.id.text_data);
        fab = findViewById(R.id.fab);

        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_view);
        dialog.setTitle("Theme");
        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        radioGroup = dialog.findViewById(R.id.rad_group);

        btn_beige = dialog.findViewById(R.id.btn_beige);
        btn_red = dialog.findViewById(R.id.btn_red);
        btn_yellow = dialog.findViewById(R.id.btn_yellow);
        linear_dialog = dialog.findViewById(R.id.linear_dialog);
        rad_sans = dialog.findViewById(R.id.rad_sans);
        rad_serif = dialog.findViewById(R.id.rad_serif);
        rad_monospace = dialog.findViewById(R.id.rad_monospace);
        actionBar = getSupportActionBar();

        w = getWindow();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    android.Manifest.permission.CAMERA
            }, REQUEST_CAMERA_CODE);
        }

        button_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(MainActivity.this);
            }
        });


        button_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String scanned_text = textview_data.getText().toString();
                copyToClipBoard(scanned_text);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        });




        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(i);
                String font = (String) checkedRadioButton.getText();
                 switch(font){
                    case("Arial"): changeFont(font);
                    case("Times New Roman"): changeFont(font);
                    case("Calibri"): changeFont(font);
                }
            }
        });

        btn_beige.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.beige_dark));
                scrollView.setBackgroundResource(R.drawable.button_beige);
                textview_data.setTextColor(getResources().getColor(R.color.beige_dark));
                fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.beige_dark)));
                fab.setImageResource(R.drawable.ic_brush_beige);
                button_capture.setBackgroundResource(R.drawable.button_beige);
                button_copy.setBackgroundResource(R.drawable.button_beige);
                linear_dialog.setBackgroundColor(getResources().getColor(R.color.beige_dark));

                rad_sans.setTextColor(getResources().getColor(R.color.beige));
                rad_sans.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.beige)));

                rad_serif.setTextColor(getResources().getColor(R.color.beige));
                rad_serif.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.beige)));

                rad_monospace.setTextColor(getResources().getColor(R.color.beige));
                rad_monospace.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.beige)));
            }
        });

        btn_red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.red_dark));
                scrollView.setBackgroundResource(R.drawable.button_red);
                textview_data.setTextColor(getResources().getColor(R.color.red_dark));
                fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.red_dark)));
                fab.setImageResource(R.drawable.ic_brush_red);
                button_capture.setBackgroundResource(R.drawable.button_red);
                button_copy.setBackgroundResource(R.drawable.button_red);
                linear_dialog.setBackgroundColor(getResources().getColor(R.color.red_dark));

                rad_sans.setTextColor(getResources().getColor(R.color.red));
                rad_sans.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.red)));

                rad_serif.setTextColor(getResources().getColor(R.color.red));
                rad_serif.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.red)));

                rad_monospace.setTextColor(getResources().getColor(R.color.red));
                rad_monospace.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.red)));
            }
        });

        btn_yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setBackgroundColor(getResources().getColor(R.color.yellow_dark));
                scrollView.setBackgroundResource(R.drawable.button_yellow);
                textview_data.setTextColor(getResources().getColor(R.color.yellow_dark));
                fab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.yellow_dark)));
                fab.setImageResource(R.drawable.ic_brush_yellow);
                button_capture.setBackgroundResource(R.drawable.button_yellow);
                button_copy.setBackgroundResource(R.drawable.button_yellow);
                linear_dialog.setBackgroundColor(getResources().getColor(R.color.yellow_dark));

                rad_sans.setTextColor(getResources().getColor(R.color.yellow));
                rad_sans.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.yellow)));

                rad_serif.setTextColor(getResources().getColor(R.color.yellow));
                rad_serif.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.yellow)));

                rad_monospace.setTextColor(getResources().getColor(R.color.yellow));
                rad_monospace.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(MainActivity.this,R.color.yellow)));
            }
        });



    }

    @Override
    protected void onResume(){
        super.onResume();
        w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        actionBar.hide();

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    getTextFromImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getTextFromImage(Bitmap bitmap){
        TextRecognizer recognizer = new TextRecognizer.Builder(this).build();
        if(!recognizer.isOperational()){
            Toast.makeText(MainActivity.this, "Error Occurred!", Toast.LENGTH_SHORT).show();
        } else{
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < textBlockSparseArray.size(); i++){
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            textview_data.setText(stringBuilder.toString());
            button_capture.setText("Retake");
            button_copy.setVisibility(View.VISIBLE);
        }
    }

    private void copyToClipBoard(String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied data", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(MainActivity.this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

    private void changeFont(String font){

        if(font.equals("Arial")) {
            textview_data.setTypeface(type_sans);
        } else if (font.equals("Times New Roman")) {
            textview_data.setTypeface(type_serif);
        } else if (font.equals("Calibri")){
            textview_data.setTypeface(type_monospace);
        }

    }
}