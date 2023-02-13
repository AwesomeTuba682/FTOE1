package com.example.myapplication.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentNotificationsBinding;
import com.example.myapplication.ui.GameActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Button startEventButtonOne;
    private int[][] best_score_all;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        startEventButtonOne = root.findViewById(R.id.idBTStartEventOne);

        Bundle bundle = getArguments(); // Get Matrix from MainActivity
        if (bundle != null) {
            best_score_all = (int[][]) bundle.getSerializable("best_score_all");
        }

        //GetKeys:
        TextView eventOneKey = root.findViewById(R.id.idEventOneKey);

        startEventButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra("difficulty", "Impossible");
                intent.putExtra("time", "15s");
                intent.putExtra("best_score_all", best_score_all);
                intent.putExtra("eventKey", eventOneKey.getText());
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String[] eventKeys = {"coca_cola"};
        int eventSum = 1;
        for (int i = 0; i<eventSum; i++) {
            databaseReference.child("urls").child(eventKeys[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String url = child.getValue(String.class);
                        Picasso.get().load(url).fetch();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Error", "Fuck");
                }
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}