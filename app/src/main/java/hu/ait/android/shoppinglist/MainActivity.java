package hu.ait.android.shoppinglist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.ait.android.shoppinglist.adapter.ShoppingListAdapter;
import hu.ait.android.shoppinglist.data.Item;
import io.realm.RealmResults;
import io.realm.Realm;

import static hu.ait.android.shoppinglist.R.string.item;


public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_NEW_ITEM = 101;
    public static final int REQUEST_EDIT_ITEM = 102;
    public static final String KEY_EDIT = "KEY_EDIT";
    private int itemToEditPosition = -1;
    private int total;
    private LinearLayout layoutContent;

    private ShoppingListAdapter shoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        total=0;

        layoutContent = (LinearLayout) findViewById(
                R.id.layoutContent);

        ((ShoppingListApplication)getApplication()).openRealm();

        RealmResults<Item> allItems = getRealm().where(Item.class).findAll();
        Item itemsArray[] = new Item[allItems.size()];
        List<Item> shoppingListResult = new ArrayList<Item>(Arrays.asList(allItems.toArray(itemsArray)));

        for (Item item: shoppingListResult) {
            total+=Integer.parseInt(item.getPrice());
        }

        shoppingListAdapter = new ShoppingListAdapter(shoppingListResult, this);
        RecyclerView recyclerShoppingList = (RecyclerView) findViewById(
                R.id.recyclerShoppingList);
        recyclerShoppingList.setLayoutManager(new LinearLayoutManager(this));
        recyclerShoppingList.setAdapter(shoppingListAdapter);

        updateTotal();

    }

    public void updateTotal(){
        TextView tvTotal= (TextView) findViewById(R.id.tvTotal);
        tvTotal.setText(getString(R.string.total, total));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addItem:
                showCreateItemActivity();
                break;
            case R.id.action_deleteList:
                deleteAll();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCreateItemActivity() {
        Intent intentStart = new Intent(MainActivity.this,
                CreateItemActivity.class);
        startActivityForResult(intentStart, REQUEST_NEW_ITEM);
    }

    public void showEditItemActivity(String itemID, int position) {
        Intent intentStart = new Intent(MainActivity.this,
                CreateItemActivity.class);
        itemToEditPosition = position;

        intentStart.putExtra(KEY_EDIT, itemID);
        startActivityForResult(intentStart, REQUEST_EDIT_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                String itemID  = data.getStringExtra(
                        CreateItemActivity.KEY_ITEM);

                Item item = getRealm().where(Item.class)
                        .equalTo("itemID", itemID)
                        .findFirst();

                if (requestCode == REQUEST_NEW_ITEM) {
                    shoppingListAdapter.addItem(item);
                    total+=Integer.parseInt(item.getPrice());
                    updateTotal();
                    showSnackBarMessage(getString(R.string.item_added));
                } else if (requestCode == REQUEST_EDIT_ITEM) {


                    shoppingListAdapter.updateItem(itemToEditPosition, item);
                    showSnackBarMessage(getString(R.string.updated_item));
                }
                break;
            case RESULT_CANCELED:
                showSnackBarMessage(getString(R.string.cancelled));
                break;
        }
    }

    public void deleteItem(Item item) {
        total-=Integer.parseInt(item.getPrice());
        getRealm().beginTransaction();
        item.deleteFromRealm();
        getRealm().commitTransaction();
        updateTotal();
    }

    public void deleteAll(){
        total=0;
        getRealm().beginTransaction();
        getRealm().deleteAll();
        getRealm().commitTransaction();

        shoppingListAdapter.removeAll();
        updateTotal();
    }


    private void showSnackBarMessage(String message) {
        Snackbar.make(layoutContent,
                message,
                Snackbar.LENGTH_LONG
        ).setAction(R.string.action_hide, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...
            }
        }).show();
    }

    public Realm getRealm() {
        return ((ShoppingListApplication)getApplication()).getRealmShoppingList();
    }


    @Override
    protected void onDestroy() {
        ((ShoppingListApplication)getApplication()).closeRealm();

        super.onDestroy();
    }




}
