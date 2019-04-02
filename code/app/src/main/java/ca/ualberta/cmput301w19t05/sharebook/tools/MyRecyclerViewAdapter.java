package ca.ualberta.cmput301w19t05.sharebook.tools;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;

/**
 * MyRecyclerViewAdapter
 * Handle books consist with their image(Book cover)
 *
 */
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

    public void addBook(final Book book) {
        if (mbooks == null) {
            mbooks = new ArrayList<>();
        }
        mbooks.add(0, book);
        notifyItemInserted(0);

    }

    public void changeBook(Book book) {
        int index = 0;
        for (Book it : mbooks) {
            if (it.getBookId().equals(book.getBookId())) {
                mbooks.set(index, book);
                notifyItemChanged(index);
                return;
            } else {
                index++;
            }
        }
    }
    public void removeBook(Book book) {

        int index = 0;
        for (Book it : mbooks) {
            if (it.getBookId().equals(book.getBookId())) {
                mbooks.remove(index);
                notifyItemRemoved(index);
                return;
            } else {
                index++;
            }
        }

    }

    public boolean contains(Book book) {
        for (Book it : mbooks) {
            if (it.getBookId().equals(book.getBookId())) {
                return true;
            }
        }
        return false;
    }

    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Book book = mbooks.get(position);
        holder.bookName.setText(book.getTitle());
        holder.authorName.setText(book.getAuthor());
        holder.bookImage.setImageURI(null);
        Glide.with(mContext).load(Uri.parse(book.getPhoto()))
                .into(holder.bookImage);

        //holder.myView.setImageURI(Uri.parse(book.getPhoto()));

        //holder.myView.setImageResource(R.drawable.common_google_signin_btn_icon_dark);

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mbooks.size();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {

        this.mClickListener = itemClickListener;
    }

    // convenience method for getting data at click position
    public Book getItem(int id) {
        return mbooks.get(id);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView bookImage;
        TextView bookName;
        TextView authorName;

        public ViewHolder(View itemView) {
            super(itemView);
            bookImage = itemView.findViewById(R.id.book_cover);
            bookName = itemView.findViewById(R.id.book_name);
            authorName = itemView.findViewById(R.id.author_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
