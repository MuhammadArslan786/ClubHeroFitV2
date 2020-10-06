package com.arslan6015.clubherofitv2.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.arslan6015.clubherofitv2.Interface.ItemClickListener;
import com.arslan6015.clubherofitv2.Model.ClassesList;
import com.arslan6015.clubherofitv2.Model.UserGeneralInfo;
import com.arslan6015.clubherofitv2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.name_classes_item.setText(classesLists.get(position).getName());
        holder.time_classes_item.setText(classesLists.get(position).getTime());

        holder.booking_classes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("UserInfo")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ref.addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for(DataSnapshot datas: dataSnapshot.getChildren()) {
//                            String email = datas.child("email").getValue().toString();
//                            String fullName =datas.child("fullName").getValue().toString();
//                            String id = datas.child("id").getValue().toString();
//                            String image =datas.child("image").getValue().toString();
                            UserGeneralInfo userGeneralInfo = dataSnapshot.getValue(UserGeneralInfo.class);

//                            UserGeneralInfo userGeneralInfo = new UserGeneralInfo(id,fullName,email);
//                            userGeneralInfo.setImage(image);

                            bookingClasses(userGeneralInfo,holder,position);

//                        }
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                    });

            }
        });

////        In onBindViewHolder whenever
//        holder.setItemClickListener(new ItemClickListener() {
//            //            call the override method of interface here.
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                bookingClasses(holder,position);
//            }
//        });


    }

    private void bookingClasses(UserGeneralInfo userGeneralInfo, final ViewHolder holder, int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Classes")
                .child(classesLists.get(position).getId())
                .child("BookingList");
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(userGeneralInfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(context,"Classes booked",Toast.LENGTH_LONG).show();
                    holder.booking_classes.setText("Booked");
                    notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"There is an error",Toast.LENGTH_LONG).show();
            }
        });
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
        private Button booking_classes;
        // create object of interface
        private ItemClickListener itemClickListener;

        //ViewHolder constructor
        public ViewHolder(View itemView) {
            super(itemView);
            //initialize the textview of list_data.xml
            name_classes_item = itemView.findViewById(R.id.name_classes_item);
            time_classes_item = itemView.findViewById(R.id.time_classes_item);
            booking_classes = itemView.findViewById(R.id.booking_classes);
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
