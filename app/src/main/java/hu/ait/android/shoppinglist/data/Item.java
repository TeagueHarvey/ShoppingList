package hu.ait.android.shoppinglist.data;

import io.realm.RealmObject;
import hu.ait.android.shoppinglist.R;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject{

    public enum ItemType {
        FOOD(0, R.drawable.food),
        ELECTRONICS(1, R.drawable.calculator), ALCOHOL(2, R.drawable.alcohol);

        private int value;
        private int iconId;

        private ItemType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static ItemType fromInt(int value) {
            for (ItemType i : ItemType.values()) {
                if (i.value == value) {
                    return i;
                }
            }
            return FOOD;
        }
    }

    @PrimaryKey
    private String itemID;

    private String name;
    private String price;
    private String description;
    private boolean alreadyPurchased;
    private int itemType;

    public Item(){ //for object relation mapping

    }

    public Item(String name, String price, String description, boolean alreadyPurchased) {
        this.name = name;
        this.price=price;
        this.description=description;
        this.alreadyPurchased=alreadyPurchased;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAlreadyPurchased() {
        return alreadyPurchased;
    }

    public void setAlreadyPurchased(boolean alreadyPurchased) {
        this.alreadyPurchased = alreadyPurchased;
    }

    public ItemType getItemType() {
        return ItemType.fromInt(itemType);
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getItemID() {
        return itemID;
    }

}
