package hu.ait.android.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;
import java.util.UUID;

import hu.ait.android.shoppinglist.adapter.ShoppingListAdapter;
import hu.ait.android.shoppinglist.data.Item;
import io.realm.Realm;
import io.realm.RealmResults;

public class CreateItemActivity extends AppCompatActivity {

    public static final String KEY_ITEM = "KEY_ITEM";

    private Spinner spinnerItemType;
    private EditText etItem;
    private EditText etEstimatedPrice;
    private EditText etItemDesc;
    private CheckBox cbAlreadyPurchased;
    private boolean alreadyPurchased;
    private Item itemToEdit = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        setupUI();

        if (getIntent().getSerializableExtra(MainActivity.KEY_EDIT) != null) {
            initEdit();
        } else {
            initCreate();
        }
    }

    private void initCreate() {
        getRealm().beginTransaction();
        itemToEdit = getRealm().createObject(Item.class, UUID.randomUUID().toString());
        getRealm().commitTransaction();
    }

    private void initEdit() {
        String itemID = getIntent().getStringExtra(MainActivity.KEY_EDIT);
        itemToEdit = getRealm().where(Item.class)
                .equalTo("itemID", itemID)
                .findFirst();

        etItem.setText(itemToEdit.getName());
        etEstimatedPrice.setText(itemToEdit.getPrice());
        etItemDesc.setText(itemToEdit.getDescription());
        spinnerItemType.setSelection(itemToEdit.getItemType().getValue());
        cbAlreadyPurchased.setChecked(itemToEdit.isAlreadyPurchased());

        cbAlreadyPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(alreadyPurchased) {
                    alreadyPurchased = false;
                }else{
                    alreadyPurchased=true;
                }
            }
        });

    }

    private void setupUI() {
        spinnerItemType = (Spinner) findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.itemtypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemType.setAdapter(adapter);

        etItem = (EditText) findViewById(R.id.etItemName);
        etEstimatedPrice = (EditText) findViewById(R.id.etEstimatedPrice);
        etItemDesc = (EditText) findViewById(R.id.etItemDescription);
        cbAlreadyPurchased = (CheckBox) findViewById(R.id.cbItem);

        cbAlreadyPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alreadyPurchased) {
                    alreadyPurchased = false;
                } else {
                    alreadyPurchased = true;
                }
            }
        });

        Button btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }

    public Realm getRealm() {
        return ((ShoppingListApplication) getApplication()).getRealmShoppingList();
    }

    private void saveItem() {
        Intent intentResult = new Intent();

        getRealm().beginTransaction();
        itemToEdit.setName(etItem.getText().toString());
        itemToEdit.setDescription(etItemDesc.getText().toString());
        itemToEdit.setPrice(etEstimatedPrice.getText().toString());
        itemToEdit.setItemType(spinnerItemType.getSelectedItemPosition());
        itemToEdit.setAlreadyPurchased(alreadyPurchased);
        getRealm().commitTransaction();

        intentResult.putExtra(KEY_ITEM, itemToEdit.getItemID());
        setResult(RESULT_OK, intentResult);
        finish();
    }


}
