package com.rubik.rubikinteractive.bistonapp.Entidades;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PagerController extends FragmentPagerAdapter {

    private final List<Fragment> listaFragments = new ArrayList<>();
    private final List<String> listaTitulos = new ArrayList<>();

    public PagerController(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String titulo){
        listaFragments.add(fragment);
        listaTitulos.add(titulo);
    }

    public void removeFragments(){
        listaFragments.removeAll(listaFragments);
        listaTitulos.removeAll(listaTitulos);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listaTitulos.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return listaFragments.get(position);
    }

    @Override
    public int getCount() {
        return listaFragments.size();
    }
}
