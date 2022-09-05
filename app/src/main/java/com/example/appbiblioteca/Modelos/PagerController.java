package com.example.appbiblioteca.Modelos;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.appbiblioteca.fragments.frag_emotion;
import com.example.appbiblioteca.fragments.frag_people_to_monitor;
import com.example.appbiblioteca.fragments.frag_see_detect;
import com.example.appbiblioteca.fragments.frag_tutor;

public class PagerController extends FragmentPagerAdapter {
    int numoftabs;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numoftabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new frag_people_to_monitor();
            case 1:
                return new frag_emotion();
            case 2:
                return new frag_see_detect();
            case 3:
                return new frag_tutor();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numoftabs;
    }

}
