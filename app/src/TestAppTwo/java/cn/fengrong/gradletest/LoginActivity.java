package cn.fengrong.gradletest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mPhoneEt;
    private EditText mCodeEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView version = findViewById(R.id.la_version);
        version.setText(BuildConfig.VERSION_NAME);
        mPhoneEt = findViewById(R.id.la_phone);
        mCodeEt = findViewById(R.id.la_code);


        Button loginBtn = findViewById(R.id.la_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this, "我这里  随便登录", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
