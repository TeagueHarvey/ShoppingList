package hu.ait.android.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hu.ait.android.shoppinglist.MainActivity;
import hu.ait.android.shoppinglist.R;
import hu.ait.android.shoppinglist.data.Item;


public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvItem;
        public TextView tvCost;
        public TextView tvDescription;
        public CheckBox cbAlreadyPurchased;
        public Button btnDelete;
        public Button btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            cbAlreadyPurchased = (CheckBox) itemView.findViewById(R.id.cbItem);
        }
    }

    private List<Item> shoppingList;
    private Context context;
    private int lastPosition = -1;

    public ShoppingListAdapter(List<Item> shoppingList, Context context) {
        this.shoppingList = shoppingList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.tvItem.setText(shoppingList.get(position).getName());
        viewHolder.tvCost.setText(shoppingList.get(position).getPrice());
        viewHolder.tvDescription.setText(shoppingList.get(position).getDescription());
        viewHolder.ivIcon.setImageResource(
                shoppingList.get(position).getItemType().getIconId());
        viewHolder.cbAlreadyPurchased.setChecked(shoppingList.get(position).isAlreadyPurchased());

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(viewHolder.getAdapterPosition());
            }
        });
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).showEditItemActivity(
                        shoppingList.get(viewHolder.getAdapterPosition()).getItemID(),
                        viewHolder.getAdapterPosition());
            }
        });

        setAnimation(viewHolder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }

    public void addItem(Item item) {
        shoppingList.add(item);
        notifyDataSetChanged();
    }

    public void removeAll(){
        shoppingList.clear();
        notifyDataSetChanged();
    }

    public void updateItem(int index, Item item) {
        shoppingList.set(index, item);

        notifyItemChanged(index);

    }

    public void removeItem(int index) {
        ((MainActivity)context).deleteItem(shoppingList.get(index));
        shoppingList.remove(index);
        notifyItemRemoved(index);
    }

    public void swapItems(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(shoppingList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(shoppingList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public Item getItem(int i) {
        return shoppingList.get(i);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
