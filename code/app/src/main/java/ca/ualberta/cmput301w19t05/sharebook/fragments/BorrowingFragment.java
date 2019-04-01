package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.activities.BookDetailActivity;
import ca.ualberta.cmput301w19t05.sharebook.models.Book;
import ca.ualberta.cmput301w19t05.sharebook.tools.FirebaseHandler;
import ca.ualberta.cmput301w19t05.sharebook.tools.MyRecyclerViewAdapter;
import ca.ualberta.cmput301w19t05.sharebook.tools.SearchBookAdapter;

import static android.support.constraint.Constraints.TAG;

/**
 * Fragment for borrowing book
 */
public final class BorrowingFragment extends Fragment {
    private static final int BORROWED = 3;
    private EditText searchView;
    private SearchBookAdapter searchBookAdapter;
    private FirebaseHandler firebaseHandler;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter requestingAdapter;
    private MyRecyclerViewAdapter acceptedAdapter;
    private MyRecyclerViewAdapter borrowedAdapter;
    private List<Book> requestingBooks;
    private final static int REQUEST = 1;
    private final static int ACCEPTED = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_borrowing, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseHandler = new FirebaseHandler(getContext());

        initAdapter();

        initSearchview();

        ArrayList<String> status = new ArrayList<>();
        status.add("available");
        status.add("requested");
        onlineDatabaseListener(searchBookAdapter, status);
        requestingBooks = new ArrayList<>();
        viewRequesting();
        viewAccepted();
        viewBorrowed();



    }

    private void viewBorrowed() {
        RecyclerView recyclerView;
        recyclerView = getView().findViewById(R.id.borrowing_borrowed_list);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        borrowedAdapter = new MyRecyclerViewAdapter(getActivity(), new ArrayList<Book>());
        borrowedAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "setClickListener: clicked");

                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra(BookDetailActivity.BOOK, borrowedAdapter.getItem(position));
                intent.putExtra(BookDetailActivity.FUNCTION,BookDetailActivity.BORROWED);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(borrowedAdapter);
        firebaseHandler.getMyRef().child(Book.BORROWED).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot userId: dataSnapshot.getChildren()){
                    if (firebaseHandler.getCurrentUser().getUserID().equals(userId.getKey())){
                        addBookById(BORROWED,dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String bookId = dataSnapshot.getKey();
                if (bookId!= null){
                    removeBookById(BORROWED, bookId);
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void viewAccepted() {
        RecyclerView recyclerView;
        recyclerView = getView().findViewById(R.id.borrowing_accepted_list);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        acceptedAdapter = new MyRecyclerViewAdapter(getActivity(), new ArrayList<Book>());
        acceptedAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "setClickListener: clicked");

                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra(BookDetailActivity.BOOK, acceptedAdapter.getItem(position));
                intent.putExtra(BookDetailActivity.FUNCTION,BookDetailActivity.ACCEPTED);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(acceptedAdapter);
        firebaseHandler.getMyRef().child("accepted").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot userId: dataSnapshot.getChildren()){
                    if (firebaseHandler.getCurrentUser().getUserID().equals(userId.getKey())){
                        addBookById(ACCEPTED,dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String bookId = dataSnapshot.getKey();
                if (bookId!= null){
                    removeBookById(ACCEPTED,bookId);
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initAdapter() {
        searchBookAdapter = new SearchBookAdapter(getActivity(), new ArrayList<Book>());
        searchBookAdapter.setClickListener(new SearchBookAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: " + position);
                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra(BookDetailActivity.BOOK, searchBookAdapter.getItem(position));
                intent.putExtra(BookDetailActivity.FUNCTION,BookDetailActivity.REQUEST);
                startActivity(intent);
            }
        });


        recyclerView = getView().findViewById(R.id.search_res);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(searchBookAdapter);
    }

    private void initSearchview() {
        searchView = getView().findViewById(R.id.SearchBar);
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    searchView.setText(null);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBookAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void viewRequesting(){
        RecyclerView recyclerView;
        recyclerView = getView().findViewById(R.id.borrowing_requested_list);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        requestingAdapter = new MyRecyclerViewAdapter(getActivity(), new ArrayList<Book>());
        recyclerView.setAdapter(requestingAdapter);
        firebaseHandler.getMyRef().child(Book.REQUESTED).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot userId: dataSnapshot.getChildren()){
                    if (firebaseHandler.getCurrentUser().getUserID().equals(userId.getKey())){
                        addBookById(REQUEST,dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String bookId = dataSnapshot.getKey();
                if (bookId!= null){
                    removeBookById(REQUEST, bookId);
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void removeBookById(final int adapterType,final String bookId) {


        firebaseHandler.getMyRef().child("books")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot users: dataSnapshot.getChildren()){
                            if (users.child(bookId).exists()){
                                Book book = users.child(bookId).getValue(Book.class);
                                switch (adapterType){
                                    case REQUEST:{
                                        searchBookAdapter.addBook(book);
                                        requestingBooks.remove(book);
                                        requestingAdapter.removeBook(book);
                                        break;
                                    }
                                    case ACCEPTED:{
                                        acceptedAdapter.removeBook(book);
                                        break;
                                    }
                                    case BORROWED:{
                                        borrowedAdapter.removeBook(book);
                                        break;
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void addBookById(final int adapterType, final String bookId) {

        firebaseHandler.getMyRef().child("books")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot users: dataSnapshot.getChildren()){
                            if (users.child(bookId).exists()){
                                Book book = users.child(bookId).getValue(Book.class);
                                if (adapterType==REQUEST){
                                    requestingAdapter.addBook(book);
                                }
                                else if (adapterType==ACCEPTED){
                                    acceptedAdapter.addBook(book);
                                }
                                else if (adapterType==BORROWED){
                                    borrowedAdapter.addBook(book);
                                }

                                searchBookAdapter.removeBook(book);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void onlineDatabaseListener(final SearchBookAdapter adapter, final ArrayList<String> status) {

        final DatabaseReference reference = firebaseHandler.getMyRef().child("books");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot it : dataSnapshot.getChildren()) {
                    Book temp = it.getValue(Book.class);
                    if (temp != null&& temp.getOwner()!=null) {
                        if (!requestingBooks.contains(temp)&&status.contains(temp.getStatus())&&!temp.getOwner().getUserID().equals(firebaseHandler.getCurrentUser().getUserID())){
                            adapter.addBook(temp);
                        }

                    }

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                adapter.clear();
                for (DataSnapshot it : dataSnapshot.getChildren()) {
                    Book temp = it.getValue(Book.class);


                    if (temp != null) {
                        if (requestingBooks.contains(temp) || !status.contains(temp.getStatus())) {
                            adapter.removeBook(temp);
                            continue;
                        }
                        else {
                            adapter.addBook(temp);
                        }

                    }
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot it : dataSnapshot.getChildren()) {
                    Book temp = it.getValue(Book.class);
                    adapter.removeBook(temp);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
