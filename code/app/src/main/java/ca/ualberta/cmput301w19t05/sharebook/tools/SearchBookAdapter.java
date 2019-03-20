package ca.ualberta.cmput301w19t05.sharebook.tools;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
/**
 * FirebaseHandler
 * Tools for handling search book from book list
 *
 */
public class SearchBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<Book> bookList;
    private List<Book> filteredBook;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;


    public SearchBookAdapter(Context context, List<Book> books) {

        bookList = books;
        this.mInflater = LayoutInflater.from(context);
        mContext = context;
        //filteredBook = books;
    }

    public void addBook(Book book) {
        if (bookList == null) {
            bookList = new ArrayList<>();
        }
        bookList.add(0, book);
    }

    public void changeBook(Book book) {

        changedAdd(book);
        int index = 0;
        for (Book it : filteredBook) {
            if (it.getBookId().equals(book.getBookId())) {
                filteredBook.set(index, book);
                notifyItemChanged(index);
                return;
            } else {
                index++;
            }
        }
    }

    private void changedAdd(Book book) {
        int index = 0;
        for (Book it : bookList) {
            if (book.getBookId().equals(it.getBookId())) {
                bookList.set(index, book);
            }
        }
    }

    public boolean contains(Book book) {

        for (Book it : bookList) {
            if (it.getBookId().equals(book.getBookId())) {
                return true;
            }
        }
        return false;
    }

    public void removeBook(Book book) {
        removeAdd(book);
        int index = 0;
        for (Book it : filteredBook) {
            if (it.getBookId().equals(book.getBookId())) {
                filteredBook.remove(index);
                notifyItemRemoved(index);
                return;
            } else {
                index++;
            }
        }
    }

    private void removeAdd(Book book) {
        int index = 0;
        for (Book it : bookList) {
            if (it.getBookId().equals(book.getBookId())) {
                bookList.remove(index);
                return;
            } else {
                index++;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.book_list_vertical, parent, false);
            return new BookViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof BookViewHolder) {

            populateItemRows((BookViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    public Book getItem(int position) {
        return filteredBook.get(position);
    }

    @Override
    public int getItemCount() {
        return filteredBook == null ? 0 : filteredBook.size();
    }

    @Override
    public int getItemViewType(int position) {
        return filteredBook.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void setClickListener(ItemClickListener itemClickListener) {

        this.mClickListener = itemClickListener;
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(BookViewHolder holder, int position) {

        Book book = filteredBook.get(position);
        Glide.with(mContext).load(Uri.parse(book.getPhoto()))
                .into(holder.cover);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.owner.setText(book.getOwner().getUsername());


    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String constraintString = constraint.toString();
                if (constraintString.isEmpty()) {
                    filteredBook = new ArrayList<>();
                } else {
                    List<Book> FilteredList = new ArrayList<>();
                    for (Book it : bookList) {
                        if (it.getTitle().contains(constraintString) || it.getAuthor().contains(constraintString)) {
                            FilteredList.add(it);
                        }
                    }
                    filteredBook = FilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredBook;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredBook = (ArrayList<Book>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    private class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView cover;
        private TextView title;
        private TextView author;
        private TextView owner;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.rearch_res_cover);
            title = itemView.findViewById(R.id.rearch_res_title);
            author = itemView.findViewById(R.id.rearch_res_author);
            owner = itemView.findViewById(R.id.rearch_res_owner);

        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


}

