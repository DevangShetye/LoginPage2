package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

public class ExtractedFragmentAdapter extends FragmentAdapter  {
    public ExtractedFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1: return new HousingFrag();
            case 2: return new EntertainmnetFrag();
            case 3: return new MedicalFrag();
            case 4: return new ElectricityNgasFrag();

        }
        return new FoodNdiningFrag();
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

