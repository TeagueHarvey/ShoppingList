package hu.ait.android.shoppinglist;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by teagu_000 on 2/11/2017.
 */

public class ShoppingListApplication extends Application {
//this represents our whole application, hence why it's good for it to manage the database, so that every activity can manage the database

    private Realm realmShoppingList;

    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
    }

    public void openRealm() {
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        realmShoppingList = Realm.getInstance(config);
    }

    public void closeRealm() {
        realmShoppingList.close();
    }

    public Realm getRealmShoppingList() {
        return realmShoppingList;
    }

}
