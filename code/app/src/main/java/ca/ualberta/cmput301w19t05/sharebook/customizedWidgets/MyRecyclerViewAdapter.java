package ca.ualberta.cmput301w19t05.sharebook.customizedWidgets;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.Book;
import ca.ualberta.cmput301w19t05.sharebook.R;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Book> mbooks;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;


    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, List<Book> books) {
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
        this.mbooks = books;

    }

    public void addBook(Book book) {
        if (mbooks == null) {
            mbooks = new ArrayList<>();
        }
        mbooks.add(0, book);
        notifyItemInserted(0);
    }

    public void removeBook(Book book) {

        int index = 0;
        for (Book it : mbooks) {
            if (it.getTitle().equals(book.getTitle()) && it.getISBN().equals(book.getISBN())) {
                mbooks.remove(index);
                notifyItemRemoved(index);
                return;
            } else {
                index++;
            }
        }


    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.book_list, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Book book = mbooks.get(position);
        holder.myTextView.setText(book.getTitle());
        holder.myView.setImageURI(null);
        Glide.with(mContext).load(Uri.parse(book.getPhoto()))
                .into(holder.myView);
        //holder.myView.setImageURI(Uri.parse(book.getPhoto()));

        //holder.myView.setImageResource(R.drawable.common_google_signin_btn_icon_dark);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mbooks.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myView;
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myView = itemView.findViewById(R.id.book_cover);
            myTextView = itemView.findViewById(R.id.book_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public Book getItem(int id) {
        return mbooks.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
