package com.biometric.safingerprintscan.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.biometric.safingerprintscan.Notifications.AllConstants;
import com.biometric.safingerprintscan.R;
import com.biometric.safingerprintscan.Scanner.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AcquisitionForm_Screen extends AppCompatActivity {

    EditText Tracking_Id, F_name, L_name;
    RelativeLayout relative1, relative2, relative3, relative4, relative5, relative6, relative7, relative8, relative9, relative10;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    CircleImageView fv1, fv2, fv3, fv4, fv5, fv6, fv7, fv8, fv9, fv10;
    Button Preview_Submit;
    Bitmap b1 = null, b2 = null, b3 = null, b4 = null, b5 = null, b6 = null, b7 = null, b8 = null, b9 = null, b10 = null, bm;
    private static final int SCAN_FINGER1 = 1, SCAN_FINGER2 = 2, SCAN_FINGER3 = 3, SCAN_FINGER4 = 4, SCAN_FINGER5 = 5, SCAN_FINGER6 = 6, SCAN_FINGER7 = 7, SCAN_FINGER8 = 8, SCAN_FINGER9 = 9, SCAN_FINGER10 = 10;
    byte[] img;
    String R_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acquisition_form_screen);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseApp.initializeApp(AcquisitionForm_Screen.this);
        databaseReference = FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference();
        storageReference = FirebaseStorage.getInstance("gs://sa-fingerprint-scan.appspot.com").getReference();

        progressDialog = new ProgressDialog(AcquisitionForm_Screen.this);
        progressDialog.setCancelable(false);

        getR_ID();
        Widgets();

    }

    private void getR_ID() {

        FirebaseDatabase.getInstance("https://sa-fingerprint-scan-default-rtdb.firebaseio.com/").getReference().child("Admin_ID")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            R_ID = snapshot.child("id").getValue().toString();
                        } catch (Exception ex) {
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void Widgets() {

        Tracking_Id = findViewById(R.id.tracking_id);
        F_name = findViewById(R.id.f_name);
        L_name = findViewById(R.id.l_name);

        fv1 = findViewById(R.id.image1);
        fv2 = findViewById(R.id.image2);
        fv3 = findViewById(R.id.image3);
        fv4 = findViewById(R.id.image4);
        fv5 = findViewById(R.id.image5);
        fv6 = findViewById(R.id.image6);
        fv7 = findViewById(R.id.image7);
        fv8 = findViewById(R.id.image8);
        fv9 = findViewById(R.id.image9);
        fv10 = findViewById(R.id.image10);

        Preview_Submit = findViewById(R.id.preview_submit);

        relative1 = findViewById(R.id.r1);
        relative2 = findViewById(R.id.r2);
        relative3 = findViewById(R.id.r3);
        relative4 = findViewById(R.id.r4);
        relative5 = findViewById(R.id.r5);
        relative6 = findViewById(R.id.r6);
        relative7 = findViewById(R.id.r7);
        relative8 = findViewById(R.id.r8);
        relative9 = findViewById(R.id.r9);
        relative10 = findViewById(R.id.r10);

        relative1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen1.class);
                startActivityForResult(intent, SCAN_FINGER1);
            }
        });
        relative2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen2.class);
                startActivityForResult(intent, SCAN_FINGER2);
            }
        });
        relative3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen3.class);
                startActivityForResult(intent, SCAN_FINGER3);
            }
        });
        relative4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen4.class);
                startActivityForResult(intent, SCAN_FINGER4);
            }
        });
        relative5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen5.class);
                startActivityForResult(intent, SCAN_FINGER5);
            }
        });
        relative6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen6.class);
                startActivityForResult(intent, SCAN_FINGER6);
            }
        });
        relative7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen7.class);
                startActivityForResult(intent, SCAN_FINGER7);
            }
        });
        relative8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen8.class);
                startActivityForResult(intent, SCAN_FINGER8);
            }
        });
        relative9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen9.class);
                startActivityForResult(intent, SCAN_FINGER9);
            }
        });
        relative10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AcquisitionForm_Screen.this, ScanScreen10.class);
                startActivityForResult(intent, SCAN_FINGER10);
            }
        });
    }

    public void Submit(View view) {

        if (!Tracking_Id.getText().toString().isEmpty() && !F_name.getText().toString().isEmpty() && !L_name.getText().toString().isEmpty()) {

            progressDialog.setMessage("Submitting Form...");
            progressDialog.show();

            StorageReference storageReference1 = storageReference.child(firebaseAuth.getUid() + "." + "pdf");
            File file;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1){
                file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM)+"/SA_Form.pdf");

            }
            else{
                file = new File(Environment.getExternalStorageDirectory().toString()+"/SA_Form.pdf");

            }

            Uri upload = Uri.fromFile(file);

            storageReference1.putFile(upload).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    Uri uri = uriTask.getResult();

                    if (uriTask.isSuccessful()) {
                        String timestamp = String.valueOf(System.currentTimeMillis());

                        HashMap<String, String> map = new HashMap<>();
                        map.put("first_name", F_name.getText().toString());
                        map.put("uid", firebaseAuth.getUid());
                        map.put("form_url", uri.toString());
                        map.put("imageURL", "");
                        map.put("timeStamp", timestamp);

                        databaseReference.child("Users_Form").child(firebaseAuth.getUid()).child(timestamp).setValue(map).
                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            HashMap<String, String> map2 = new HashMap<>();
                                            map2.put("first_name", F_name.getText().toString());
                                            map2.put("uid", firebaseAuth.getUid());

                                            databaseReference.child("Notifications").child(timestamp).setValue(map2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        progressDialog.dismiss();
                                                        File myFile;
                                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1){
                                                            myFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM)+"/SA_Form.pdf");

                                                        }
                                                        else{
                                                            myFile = new File(Environment.getExternalStorageDirectory().toString()+"/SA_Form.pdf");

                                                        }

                                                        myFile.delete();
                                                        getToken(F_name.getText().toString() + " has submitted the form", R_ID, "");
                                                        Toast.makeText(AcquisitionForm_Screen.this, "Form Saved Successfully", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(AcquisitionForm_Screen.this, User_MainScreen.class));
                                                        finish();

                                                    } else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AcquisitionForm_Screen.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(AcquisitionForm_Screen.this, "Form not saved -- " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                }
            });
        } else {
            Toast.makeText(AcquisitionForm_Screen.this, "Missing data", Toast.LENGTH_SHORT).show();
        }

    }

    public void Generate_PDF() {

        if (Tracking_Id.getText().length() > 0 && F_name.getText().length() > 0 && L_name.getText().length() > 0) {
            progressDialog.setMessage("Generating Form...");
            progressDialog.show();

            PdfDocument pdfDocument = new PdfDocument();
            Paint paint = new Paint();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2480, 3509, 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            int total_width = pageInfo.getPageWidth(), total_height = pageInfo.getPageHeight();
            Bitmap bitmap, bitmap2;
            bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.nadra_form_1), 2480, 3509, false);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            bitmap2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.nadralogo), 350, 463, false);
//            b1 =  b2 = b3 = b4 = b5 = b6 = b7 = b8 = b9 = b9 = b10 = bitmap2;

            if (b1 != null) {
                canvas.drawBitmap(b1, 170, 1200, paint);
            }
            if (b2 != null) {
                canvas.drawBitmap(b2, 610, 1200, paint);
            }
            if (b3 != null) {
                canvas.drawBitmap(b3, 1050, 1200, paint);
            }
            if (b4 != null) {
                canvas.drawBitmap(b4, 1490, 1200, paint);
            }
            if (b5 != null) {
                canvas.drawBitmap(b5, 1930, 1200, paint);
            }
            if (b6 != null) {
                canvas.drawBitmap(b6, 170, 1795, paint);
            }
            if (b7 != null) {
                canvas.drawBitmap(b7, 610, 1795, paint);
            }
            if (b8 != null) {
                canvas.drawBitmap(b8, 1050, 1795, paint);
            }
            if (b9 != null) {
                canvas.drawBitmap(b9, 1490, 1795, paint);
            }
            if (b10 != null) {
                canvas.drawBitmap(b10, 1930, 1795, paint);
            }

            paint.setColor(Color.BLACK);
            paint.setTextSize(35.0f);
            canvas.drawText(Tracking_Id.getText().toString(), total_width - 700, 360, paint);

            paint.setTextSize(45.0f);
            canvas.drawText(F_name.getText().toString(), 280, 920, paint);
            canvas.drawText(L_name.getText().toString(), total_width - 1050, 920, paint);

            pdfDocument.finishPage(page);
            File file;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1){
               file = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM)+"/SA_Form.pdf");

            }
            else{
                file = new File(Environment.getExternalStorageDirectory().toString()+"/SA_Form.pdf");

            }

            try {
                pdfDocument.writeTo(new FileOutputStream(file));
                Toast.makeText(AcquisitionForm_Screen.this, "PDF Generated", Toast.LENGTH_SHORT).show();

            } catch (Exception ex) {
                Toast.makeText(AcquisitionForm_Screen.this, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            pdfDocument.close();

            progressDialog.dismiss();

            Uri pdfPath = FileProvider.getUriForFile(AcquisitionForm_Screen.this, getApplicationContext().getPackageName() + ".provider", file);

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(pdfPath, "application/pdf");
            pdfIntent.setClipData(ClipData.newRawUri("", pdfPath));
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            try {
                startActivity(pdfIntent);

            } catch (Exception ex) {
                Toast.makeText(AcquisitionForm_Screen.this, "No App can open this file", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Incomplete fields", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int status;
        String errorMesssage;
        switch (requestCode) {
            case (SCAN_FINGER1): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b1 = bm;
                        fv1.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case (SCAN_FINGER2): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b2 = bm;
                        fv2.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case (SCAN_FINGER3): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b3 = bm;
                        fv3.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case (SCAN_FINGER4): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b4 = bm;
                        fv4.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case (SCAN_FINGER5): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b5 = bm;
                        fv5.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case (SCAN_FINGER6): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b6 = bm;
                        fv6.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case (SCAN_FINGER7): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b7 = bm;
                        fv7.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case (SCAN_FINGER8): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b8 = bm;
                        fv8.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case (SCAN_FINGER9): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b9 = bm;
                        fv9.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case (SCAN_FINGER10): {
                if (resultCode == RESULT_OK) {
                    status = data.getIntExtra("status", Status.ERROR);
                    if (status == Status.SUCCESS) {
                        Toast.makeText(AcquisitionForm_Screen.this, "Fingerprint captured", Toast.LENGTH_SHORT).show();
                        img = data.getByteArrayExtra("img");
                        bm = BitmapFactory.decodeByteArray(img, 0, img.length);
                        b10 = bm;
                        fv10.setImageBitmap(bm);
                    } else {
                        errorMesssage = data.getStringExtra("errorMessage");
                        Toast.makeText(AcquisitionForm_Screen.this, errorMesssage + " -- Error: " + errorMesssage + " --", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

    public void Preview(View view) {
        if (ActivityCompat.checkSelfPermission(AcquisitionForm_Screen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AcquisitionForm_Screen.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
        } else {
            Generate_PDF();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 10 && grantResults.length > 0) {
            boolean StorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (StorageAccepted) {
                Generate_PDF();
            } else {
                Toast.makeText(AcquisitionForm_Screen.this, "Please Enable Storage Permission!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getToken(String Message, String hisID, String myImage) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users").child(hisID);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String token = snapshot.child("token_id").getValue().toString();

                JSONObject to = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    data.put("title", "New Message");
                    data.put("message", Message);
                    data.put("hisID", hisID);
//                    data.put("hisImage", myImage);
//                    data.put("chatID", chatID);

                    to.put("to", token);
                    to.put("data", data);

                    sendNotification(to);

                } catch (Exception e) {
                    Toast.makeText(AcquisitionForm_Screen.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(JSONObject to) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AllConstants.NOTIFICATION_URL, to, response -> {
            Log.d("notification", "sendNotification: " + response);
        }, error -> {
            Toast.makeText(AcquisitionForm_Screen.this, "sendNotification: " + error, Toast.LENGTH_SHORT).show();

            Log.d("notification", "sendNotification: " + error);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "key=" + AllConstants.SERVER_KEY);
                map.put("Content-Type", "application/json");
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}