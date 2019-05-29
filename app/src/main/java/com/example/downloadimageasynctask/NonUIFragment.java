package com.example.downloadimageasynctask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class NonUIFragment extends Fragment {
    Task task;
    Activity activity;

    public NonUIFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setRetainInstance(true);
        activity = (Activity) context;
        if (task != null) {
            task.onAttach(activity);
        }
    }

    public void beginTask(String... args) {
        task = new Task(activity);
        task.execute(args);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (task != null)
            task.onDetach();
    }
}
