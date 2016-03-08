package com.chatapp.fovi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {

    private EditText editUser;
    private EditText editPass;
    private EditText editMail;
    private TextView titulo;
    private ProgressBar pb;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editUser = (EditText) findViewById(R.id.usernameField);
        editPass = (EditText) findViewById(R.id.passwordField);
        editMail = (EditText) findViewById(R.id.emailField);
        titulo = (TextView) findViewById(R.id.textViewTitle);
        pb = (ProgressBar) findViewById(R.id.pbSignup);
        mCancelButton = (Button) findViewById(R.id.cancel_Button);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/sensei-medium.otf");
        titulo.setTypeface(myFont);


        getSupportActionBar().hide();
    }

    public void onClickCancel(View view){
        finish();
    }

    public void onClickRegistro(View view){
        if(editUser.getText().toString().equals("") || editPass.getText().toString().equals("") ||
                editMail.getText().toString().equals("")){
            dialogoCamposVacios().show();
        }else{
            ParseUser parseUser = new ParseUser();
            parseUser.setUsername(editUser.getText().toString());
            parseUser.setPassword(editPass.getText().toString());
            parseUser.setEmail(editMail.getText().toString());
            pb.setVisibility(View.VISIBLE);
            parseUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    pb.setVisibility(View.INVISIBLE);
                    if(e == null){
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle(R.string.titulo_dialog);
                        builder.setMessage(e.getMessage());
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }
            });
        }
    }

    public Dialog dialogoCamposVacios(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.titulo_dialog);
        builder.setMessage(R.string.dialog_camposvacios);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
