package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.ui.GameActivity;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RadioGroup difficultyGroup, timeGroup;
    private Button startButton;
    private String difficulty, time;
    private int[][] best_score_all;
    private ImageView Emoji;

    public static Fragment newInstance(Bundle args){
        // get your data and do whatever
        return new HomeFragment(); }

    public HomeFragment() {
        // Required empty public constructor
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);




        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        Emoji = (ImageView) root.findViewById(R.id.idIVEmoji);




        difficultyGroup = root.findViewById(R.id.difficulty_group);
        timeGroup = root.findViewById(R.id.time_setting);

        startButton = root.findViewById(R.id.start_button);

        Bundle bundle = getArguments();
        if (bundle != null) {
            best_score_all = (int[][]) bundle.getSerializable("best_score_all");
        }



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = difficultyGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = getView().findViewById(selectedId);
                difficulty = selectedRadioButton.getText().toString();

                int selectedIdTime = timeGroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonTime = getView().findViewById(selectedIdTime);
                time = selectedRadioButtonTime.getText().toString();


                Intent intent = new Intent(getActivity(), GameActivity.class);
                intent.putExtra("difficulty", difficulty);
                intent.putExtra("time", time);
                intent.putExtra("eventKey", "default");
                intent.putExtra("best_score_all", best_score_all);
                startActivity(intent);
            }
        });

        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}