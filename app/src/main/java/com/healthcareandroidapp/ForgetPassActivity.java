package com.healthcareandroidapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.speech.tts.Voice;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

public class ForgetPassActivity extends AppCompatActivity {

    private EditText emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        emailView = (EditText) findViewById(R.id.email_forget_pass);

        Button btnSubmit = (Button) findViewById(R.id.submit_forget_password_button);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder;
//                builder = new AlertDialog.Builder(getBaseContext(), R.style.Theme_AppCompat);
//                builder.setTitle("Xác nhận lấy lại mật khẩu")
//                        .setMessage("Bạn có chác muốn lấy lại mật khẩu ?")
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                submit();
//                            }
//                        })
//                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        })
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .create();
                submit();
            }
        });
        Button btnReturn = (Button) findViewById(R.id.btnReturnLoginForgetPass);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnLogin();
            }
        });
    }

    private void submit() {

        //Kiem tra email
        String email = emailView.getText().toString();
        View focus = null;
        boolean cancel = false;
        if (TextUtils.isEmpty(email)) {
            this.emailView.setError("Hãy điền thông tin email");
            focus = emailView;
            cancel = true;
        } else if (!Validation.isEmailValid(email)) {
            this.emailView.setError("Email không hợp lệ");
            focus = emailView;
            cancel = true;
        }

        if (cancel) {
            focus.requestFocus();
        } else {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.forget_pass_progress);
            progressBar.setVisibility(View.VISIBLE);
            ScrollView scrollView = (ScrollView) findViewById(R.id.forget_form);
            scrollView.setVisibility(View.GONE);
            UserForgetPassTask userForgetPassTask = new UserForgetPassTask(emailView.getText().toString());
            userForgetPassTask.execute((Void) null);
        }
    }

    private void returnLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public class UserForgetPassTask extends AsyncTask<Void, Void, Boolean> {
        private String email;

        public UserForgetPassTask(String email) {
            this.email = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return Connection.resetPass(email);
        }
    }

    public static class ForgetPassDialog extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Bạn có chắc muốn lấy lại mật khẩu ?")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ok();
                        }
                    })
                    .setNegativeButton("Huỷ bỏ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }

        public boolean ok() {
            return true;
        }
    }
}
