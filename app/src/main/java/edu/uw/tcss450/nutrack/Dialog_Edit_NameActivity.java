package edu.uw.tcss450.nutrack;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Dialog_Edit_NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog__edit__name);
    }

    private void initializeRegistrationDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder((Dialog_Edit_NameActivity.this));
        View mView = getLayoutInflater().inflate(R.layout.dialog_registration, null);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

        Button btnRegister = (Button) dialog.findViewById(R.id.registration_button_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tempName = (EditText) dialog.findViewById(R.id.edit_name);

                String name = tempName.getText().toString();

//                if (insertNewMemberData(name)) {
                dialog.dismiss();
                verifyName();
//                }
            }
        });
    }

    public void verifyName() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}
