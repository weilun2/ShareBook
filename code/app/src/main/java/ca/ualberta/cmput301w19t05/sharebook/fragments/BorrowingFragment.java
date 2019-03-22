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
import com.google.firebase.database.Query;
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
    private EditText searchView;
    private SearchBookAdapter searchBookAdapter;
    private FirebaseHandler firebaseHandler;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter requestingAdapter;
    private List<Book> requestingBooks;

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



    }

    private void initAdapter() {
        searchBookAdapter = new SearchBookAdapter(getActivity(), new ArrayList<Book>());
        searchBookAdapter.setClickListener(new SearchBookAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onItemClick: " + position);
                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra("book", searchBookAdapter.getItem(position));
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
        firebaseHandler.getMyRef().child("requests").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot userId: dataSnapshot.getChildren()){
                    if (firebaseHandler.getCurrentUser().getUserID().equals(userId.getKey())){
                        addBookById(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void addBookById(final String bookId) {

        firebaseHandler.getMyRef().child(getString(R.string.db_books))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot users: dataSnapshot.getChildren()){
                            if (users.child(bookId).exists()){
                                Book book = users.child(bookId).getValue(Book.class);
                                requestingAdapter.addBook(book);
                                requestingBooks.add(book);
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
                    if (temp != null) {
                        if (!requestingBooks.contains(temp)&&status.contains(temp.getStatus())&&!temp.getOwner().getUserID().equals(firebaseHandler.getCurrentUser().getUserID())){
                            adapter.addBook(temp);
                        }

                    }

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Book temp = dataSnapshot.getValue(Book.class);

                if (temp != null) {
                    if (requestingBooks.contains(temp)||!status.contains(temp.getStatus())){
                        adapter.removeBook(temp);
                        return;
                    }
                    if (adapter.contains(temp)) {
                        adapter.changeBook(temp);
                    } else {
                        adapter.addBook(temp);
                    }

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Book temp = dataSnapshot.getValue(Book.class);
                adapter.removeBook(temp);
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
