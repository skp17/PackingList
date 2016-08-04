package com.example.packinglist;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class  PackingListActivity extends Activity {

    EditText mEditText;
    Button mButton;
    ListView mListView;
    PackingListDb mDatabase;
    ArrayList<String> item_list;
    ArrayAdapter mAdapter;

    String item;
    Boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_list);

        // Cast
        mEditText = (EditText) findViewById(R.id.editText_item);
        mButton = (Button) findViewById(R.id.add_item_button);
        mListView = (ListView) findViewById(R.id.list_items);

        mDatabase = new PackingListDb(this);
        item_list = new ArrayList<String>();

        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, item_list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setAdapter(mAdapter);


        displayItems();
//        setChecked();
        AddItem();
        deleteItem();

    }


    public void displayItems() {
        Cursor res = mDatabase.getAllData();
        res.moveToNext();
        for (int i = 1; i <= res.getCount(); i++) {
            item_list.add(res.getString(1)); // Retrieve item string from database

            if(res.getInt(2) == 1)
                mListView.setItemChecked(i,true);
            else
                mListView.setItemChecked(i,false);

            res.moveToNext();
        }

    }


    public void AddItem() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = mEditText.getText().toString();
                item_list.add(item);

                if (item.isEmpty()) {
                    Toast.makeText(PackingListActivity.this, "Empty Field", Toast.LENGTH_LONG).show();
                    return;
                }

                mAdapter.notifyDataSetChanged();

                Boolean isInserted = mDatabase.insertData(item, 0); // 0 means unchecked
                if (isInserted)
                    Toast.makeText(PackingListActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PackingListActivity.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();

                mEditText.setText(null);
            }
        });
    }

    public void deleteItem() {
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                item_list.remove(position);
                mAdapter.notifyDataSetChanged();

                Integer deletedRows = mDatabase.deleteData(position);
                if (deletedRows > 0) {
                    Toast.makeText(PackingListActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(PackingListActivity.this, "Data not Deleted", Toast.LENGTH_LONG).show();

                return false;
            }
        });
    }


//    public void setChecked() {
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//
//                CheckedTextView mCTV = (CheckedTextView) view;
//                if (mCTV.isChecked()) {
//                    mDatabase.updateData(position, 1); // check
//                    Toast.makeText(PackingListActivity.this, "Item checked", Toast.LENGTH_SHORT).show();
//                } else {
//                    mDatabase.updateData(position, 0); // check
//                    Toast.makeText(PackingListActivity.this, "Item Unchecked", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
//    }

    public void deleteAllItems() {
        Boolean isDeleted = mDatabase.deleteAllData();
        if (isDeleted)
            Toast.makeText(PackingListActivity.this, "All Data Deleted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(PackingListActivity.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();

        item_list.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_items) {
            deleteAllItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
