package id.sch.smktelkom_mlg.nextbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class NpostActivity extends AppCompatActivity {
    @BindView(R.id.imageViewNewPostUser)
    CircleImageView ivNPostUser;
    @BindView(R.id.textViewNewPostUser)
    TextView tvNPostUser;
    @BindView(R.id.textViewNPostLesson)
    TextView tvNPostLesson;
    @BindView(R.id.imageButtonPict)
    ImageButton ibPict;
    //    @BindView(R.id.imageButtonFile)
//    ImageButton ibFile;
    @BindView(R.id.editTextPost)
    EditText etNPost;
    @BindView(R.id.buttonPost)
    Button btPost;

    //post parameter
    private Uri uriFoto = null;
    //private Uri uriFile = null;
    private String content = "";

    private int PICK_IMAGE_REQUEST = 1;
    //private int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npost);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tulis Baru");
        ButterKnife.bind(this);

        tvNPostUser.setText(Prefs.getString("fullname", null));

        ibPict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
//        ibFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letsDo();
            }
        });
    }

    private void letsDo() {
        //AppController.getInstance().addToRequestQueue(multipartRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            uriFoto = data.getData();
            Log.d("Activity result", "onActivityResult: " + uriFoto);
            ibPict.setBackgroundColor(getResources().getColor(R.color.blue));
            ibPict.setEnabled(false);
        }
    }
}
