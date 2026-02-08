package com.copycloud.app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.copycloud.app.fragments.AboutFragment;
import com.copycloud.app.fragments.HistoryFragment;
import com.copycloud.app.fragments.RetrieveFragment;
import com.copycloud.app.fragments.SendFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new SendFragment();
            case 1:
                return new RetrieveFragment();
            case 2:
                return new HistoryFragment();
            default:
                return new SendFragment();
        }
    }
    
    @Override
    public int getItemCount() {
        return 3;
    }
}
