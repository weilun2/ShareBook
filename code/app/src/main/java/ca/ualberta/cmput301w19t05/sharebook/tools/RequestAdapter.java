package ca.ualberta.cmput301w19t05.sharebook.tools;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.activities.UserProfile;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.models.User;

/**
 * RequestAdapter
 * Tools for handling search book from book list
 *
 */
public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> requester;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    final private Book book;
    private FirebaseHandler firebaseHandler;
    private Context mContext;

    public RequestAdapter(Context context, List<User> users, Book book) {
        this.book = book;
        this.requester = users;
        this.mInflater = LayoutInflater.from(context);
        this.firebaseHandler = new FirebaseHandler(context);
        this.mContext = context;
        //filteredBook = books;
    }

    public Book getBook() {
        return book;
    }

    public void addUser(User user) {
        if (requester == null) {
            requester = new ArrayList<>();
        }
        requester.add(0, user);
        notifyItemInserted(0);
    }

    public void changeUser(User user) {

        int index = 0;
        for (User it : requester) {
            if (it.getUserID().equals(user.getUserID())) {
                requester.set(index, user);
                notifyItemChanged(index);
                return;
            } else {
                index++;
            }
        }
    }

    public boolean contains(User user) {

        for (User it : requester) {
            if (it.getUserID().equals(user.getUserID())) {
                return true;
            }
        }
        return false;
    }

    public void removeUser(User user) {
        int index = 0;
        if(user.getUserID()!=null){
            for (User it : requester) {
                if (user.getUserID().equals(it.getUserID())) {
                    requester.remove(index);
                    notifyItemRemoved(index);
                    return;
                } else {
                    index++;
                }
            }
        }

    }


    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_request, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        populateItemRows((RequestAdapter.UserViewHolder) viewHolder, position);
    }

    private void populateItemRows(RequestAdapter.UserViewHolder holder, int position) {
        final User user = requester.get(position);
        holder.userName.setText(user.getUsername());
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseHandler.getMyRef().child("username_email_tuple")
                        .child(user.getUserID())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                Intent intent = new Intent(mContext, UserProfile.class);
                                intent.putExtra("owner", user);
                                mContext.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                for(User it: requester){
                    if (it.getUserID().equals(user.getUserID())){
                        firebaseHandler.acceptRequest(book,user);
                    }
                    else {
                        firebaseHandler.declineRequest(book,it,false);
                    }
                }
                if(mContext instanceof Activity){
                    ((Activity)mContext).finish(); }



            }
        });

        holder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                firebaseHandler.declineRequest(book,user, true);
            }
        });
    }


    public User getItem(int position) {
        return requester.get(position);
    }
    @Override
    public int getItemCount() {
        return requester.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {

        this.mClickListener = itemClickListener;
    }


    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView userName;
        Button acceptButton;
        Button declineButton;


        public UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.requester);
            acceptButton = itemView.findViewById(R.id.accept);
            declineButton = itemView.findViewById(R.id.decline);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
