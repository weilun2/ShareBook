package ca.ualberta.cmput301w19t05.sharebook.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ca.ualberta.cmput301w19t05.sharebook.AddBookScreen;
import ca.ualberta.cmput301w19t05.sharebook.R;

public final class MyShelfFragment extends Fragment {
    private TextView textView;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myshelf, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textView = getActivity().findViewById(R.id.textView1);
        button = getActivity().findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "myShelfFragment", Toast.LENGTH_SHORT).show();
                textView.setText("clicked");
                Intent intent = new Intent(getActivity(), AddBookScreen.class);
                startActivity(intent);
            }
        });

    }
}
