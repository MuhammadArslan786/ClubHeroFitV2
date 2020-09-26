package com.arslan6015.clubherofitv2.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitv2.Interface.ItemClickListener;
import com.arslan6015.clubherofitv2.Model.ClassesList;
import com.arslan6015.clubherofitv2.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.ViewHolder> {
    private Context context;
    private List<ClassesList> classesLists;

    //in constructor pass the context, & arraylist type object
    public ClassesAdapter(Context context, List<ClassesList> classesLists) {
        this.context = context;
        this.classesLists = classesLists;
    }

    //in creating adapter we have to implement three method.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //here list_data.xml layout converts into the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classes_data_item, parent, false);
        //Whenever adapter calls return that view into the viewHolder() constructors which is seen down below.
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name_classes_item.setText(classesLists.get(position).getName());
        holder.time_classes_item.setText(classesLists.get(position).getTime());
//
////        In onBindViewHolder whenever
//        holder.setItemClickListener(new ItemClickListener() {
//            //            call the override method of interface here.
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                UpdateDialogBox(position);
//            }
//        });
//

    }

    //Returns the arraylist size.
    @Override
    public int getItemCount() {
        return classesLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            //In order to get click events just implements the View.OnClickListener
            implements View.OnClickListener {
        // declare list_data.xml Textview
        public TextView name_classes_item, time_classes_item;
        // create object of interface
        private ItemClickListener itemClickListener;

        //ViewHolder constructor
        public ViewHolder(View itemView) {
            super(itemView);
            //initialize the textview of list_data.xml
            name_classes_item = itemView.findViewById(R.id.name_classes_item);
            time_classes_item = itemView.findViewById(R.id.time_classes_item);
            //whenever any of the item in the recyclerview is clicked
            itemView.setOnClickListener(this);

        }


        //        Make a setItemClickListener() so that we can pass Interface method inside it.
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
//

        //         if we implements View.OnClickListener then we have to call onClick() method here.
        public void onClick(View view) {
            // so we pass values to onClick in our custom ItemClickListener interface here.
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

}
