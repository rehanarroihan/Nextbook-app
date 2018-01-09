package id.sch.smktelkom_mlg.nextbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoconActivity extends AppCompatActivity {
    @BindView(R.id.textViewMessage)
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nocon);
        ButterKnife.bind(this);
        setTitle("Nextbook");

        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoconActivity.this, MainActivity.class));
            }
        });
    }
}
