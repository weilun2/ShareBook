package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.ualberta.cmput301w19t05.sharebook.AddBookScreen;
import ca.ualberta.cmput301w19t05.sharebook.MainActivity;
import ca.ualberta.cmput301w19t05.sharebook.R;
import ca.ualberta.cmput301w19t05.sharebook.customizedWidgets.MyRecyclerViewAdapter;

public final class MyShelfFragment extends Fragment {
    private MyRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myshelf, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView recyclerView = getView().findViewById(R.id.available_list);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);

        ArrayList<String> bookNames = new ArrayList<>();
        bookNames.add("first");
        bookNames.add("second");
        ArrayList<Integer> bookCover = new ArrayList<>();
        bookCover.add(R.drawable.common_google_signin_btn_icon_dark);
        bookCover.add(R.drawable.common_google_signin_btn_icon_dark_normal);
        adapter = new MyRecyclerViewAdapter(getActivity(),bookCover, bookNames);
        adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ;
            }
        });
        recyclerView.setAdapter(adapter);


    }
}
