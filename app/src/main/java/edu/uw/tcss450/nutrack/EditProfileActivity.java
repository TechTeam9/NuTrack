package edu.uw.tcss450.nutrack;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.password;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //obtain acess to the ListView object defined in XML
        final ListView listview = (ListView) findViewById(R.id.listView);

        //Obtain access to the String array defined in xml
        String[] labels = getResources().getStringArray(R.array.list_view_labels);

        //create and array of intents
        Intent[] intents = new Intent[]{
                // TODO have to put dialog for each edit
//                new Intent(this, ButtonActivity.class),
//                new Intent(this, TextViewActivity.class),
//                new Intent(this, EditTextActivity.class),
//                new Intent(this, AutoCompleteTextActivity.class),
//                new Intent(this, SpinnerActivity.class),
//                new Intent(this, CheckBoxRadioButtonActivity.class)
                new Intent(this, Dialog_Edit_NameActivity.class),
        };

        //create and populate a map linking the labels to the intents
        final Map<String, Intent> map = new HashMap<>();
        for (int i = 0; i < labels.length; ++i) {
            map.put(labels[i], intents[i]);
        }

        //add an Adapter to the ListView
        listview.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                labels
        ));

        //define what happens when an element in the ListView is clicked
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Log.d("MAIN_ACTIVITY", item);
                startActivity(map.get(item));
            }
        });
    }

    // TODO create 5 edit method.
    // Codes for building a dialog.
    private void initializeRegistrationDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder((EditProfileActivity.this));
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
