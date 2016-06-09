package letsdecode.com.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;


public class EditActivity extends AppCompatActivity {
    public static final String EDITED_ITEM_VALUE = "EDITED_ITEM_VALUE";
    public static int REQUEST_CODE_EDIT = 0x1;

    private EditText editText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        String textToBeEdited = getIntent().getStringExtra(ItemListActivity.ITEM);
        //reference of editText
        editText = (EditText) findViewById(R.id.editText_editItem);
        editText.setText(textToBeEdited);
        editText.setSelection(textToBeEdited.length());


        editText.addTextChangedListener(editTextWatcher);

        //reference of button
        saveButton = (Button) findViewById(R.id.button_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textFromEditText = editText.getText().toString();
                Intent intentForItemListActivity = new Intent();

                // put the message in Intent
                intentForItemListActivity.putExtra(EDITED_ITEM_VALUE, textFromEditText);
                // Set The Result in Intent
                setResult(RESULT_OK, intentForItemListActivity);
                // finish The activity
                finish();
            }
        });

        //disabling save button
        saveButton.setEnabled(false);


        //Set Action bar icon and show that
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.check);

        //for keyboard focus
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


    //text watch listener for editText in order to make button enable and disable
    private TextWatcher editTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String textFromEditText = editText.getText().toString();
            //check if user entered some data, enable the button
            if (textFromEditText.trim().length() > 0) {
                saveButton.setEnabled(true);
            } else {
                saveButton.setEnabled(false);
            }
        }
    };
}
