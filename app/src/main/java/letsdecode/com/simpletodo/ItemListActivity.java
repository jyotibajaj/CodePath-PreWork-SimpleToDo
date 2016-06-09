package letsdecode.com.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ItemListActivity extends AppCompatActivity {
    private static final String TAG = ItemListActivity.class.getSimpleName();
    public static final String ITEM = "SELECTED_ITEM_VALUE";


    private List<String> items = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> itemsAdapter;
    private EditText editText;
    private int savedPositionOfEditedItem;
    private Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        //reference of list view
        listView = (ListView) findViewById(R.id.listView_items);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "setupListViewLongListener. onItemLongClick: @@@@@@@" + position);
                Log.d(TAG, "onItemLongClick: removed" + position);
                //remove the selected item from the linked list.
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                listView.setSelection(itemsAdapter.getCount() - 1);
                writeItemsToFile(items);
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemFromArrayList = items.get(position);
                Log.d(TAG, "setupListViewShortListener. onItemLongClick: @@@@@@@" + position);
                Intent editActivityIntent = new Intent(ItemListActivity.this, EditActivity.class);
                savedPositionOfEditedItem = position;
                editActivityIntent.putExtra(ITEM, selectedItemFromArrayList);
                startActivityForResult(editActivityIntent, EditActivity.REQUEST_CODE_EDIT);
            }
        });

        //edit text reference
        editText = (EditText) findViewById(R.id.editText_addItem);
        editText.requestFocus();
        editText.addTextChangedListener(editTextWatcher);

        //reference to button
        addButton = (Button) findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItemText = editText.getText().toString();
                if (newItemText != null && newItemText.trim().isEmpty() == false) {
                    //following code updates 'items' as well
                    itemsAdapter.add(newItemText);
                    Log.d(TAG, "onAddItem: iTemTxt " + newItemText);
                    editText.setText("");
                    //new entry added in the file
                    //By this time 'items' should already be updated with recent entry
                    writeItemsToFile(items);
                }
            }
        });
        //button disabled initially
        addButton.setEnabled(false);

        //read items from file
        items = readItemsFromFile();
        //initializing arrayadapter
        itemsAdapter = new ArrayAdapter<>(this,R.layout.list_layout, items);
        listView.setAdapter(itemsAdapter);

        Log.d(TAG, "onCreate: Items list" + items);

        //Set Action bar icon and show that
        getSupportActionBar().setIcon(R.drawable.check);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //for keyboard focus
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private List<String> readItemsFromFile() {
        File fileDir = getFilesDir();
        File toDoFile = new File(fileDir, "todo.txt");
        List<String> res = new ArrayList<>();
        try {
            res = FileUtils.readLines(toDoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void writeItemsToFile(List<String> listToSave) {
        File fileDir = getFilesDir();
        File toDoFile = new File(fileDir, "todo.txt");
        try {
            if (listToSave != null) {
                FileUtils.writeLines(toDoFile, listToSave);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Call Back method  to get the edited text  form other Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 1
        if (resultCode == RESULT_OK) {
            if (requestCode == EditActivity.REQUEST_CODE_EDIT) {
                if (null != data) {
                    // fetch the data String
                    String textForList = data.getStringExtra(EditActivity.EDITED_ITEM_VALUE);
                    if (textForList != null && textForList.trim().isEmpty() == false) {
                        items.set(savedPositionOfEditedItem, textForList);
                        itemsAdapter.notifyDataSetChanged();
                        writeItemsToFile(items);
                    }
                }
            }
        }
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
                addButton.setEnabled(true);

            } else {
                addButton.setEnabled(false);
            }
        }
    };
}




