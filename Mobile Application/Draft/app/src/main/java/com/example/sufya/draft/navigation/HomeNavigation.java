package com.example.sufya.draft.navigation;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sufya.draft.registration_login_profile_system.ProfileFragment;
import com.example.sufya.draft.R;
import com.example.sufya.draft.add_expenditure.addExpenditureActivity;
import com.example.sufya.draft.category.CategoryActivity;
import com.example.sufya.draft.home.HomeFragment;
import com.example.sufya.draft.search.Search;

/**
 * Created by sufyan Ahmed on 31/01/2017.
 */

/**
 *Navigation bar activity
 */
public class HomeNavigation extends Fragment implements View.OnClickListener  {
    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            Fragment home = new HomeFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction().addToBackStack(null);
            ft.add(R.id.fragment_home,home);
            ft.commit();
            ft.addToBackStack(null);
        }

        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_navigation,parent,false);
        return view;

    }

    /**
     * listens to what XML element has been clicked upon
     * @param v XML ID clicked
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_profile:
                goToProfile();
                break;

            case R.id.tv_categories:
                goToCategory();
                break;

            case R.id.tv_trend:

                break;

            case R.id.tv_home:
                goToHome();
                break;

            case R.id.tv_add:
                goToAddExpenditure();
                break;

            case R.id.tv_search_box:
                goToSearch();
                break;

            case R.id.tv_search_go:
                goToSearch();
                break;

            default:
                goToHome();
                break;

        }
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Initialises the XML elements

        Button tv_search_box = (Button) view.findViewById(R.id.tv_search_box);
        tv_search_box.setOnClickListener(this);

        Button tv_search_go = (Button) view.findViewById(R.id.tv_search_go);
        tv_search_go.setOnClickListener(this);

        Button tv_home = (Button) view.findViewById(R.id.tv_home);
        tv_home.setOnClickListener(this);

        Button tv_profile = (Button) view.findViewById(R.id.tv_profile);
        tv_profile.setOnClickListener(this);

        Button tv_categories = (Button) view.findViewById(R.id.tv_categories);
        tv_categories.setOnClickListener(this);

        Button tv_add = (Button) view.findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);

    }

    /**
     *Go to profile page fragment
     */
    private void goToProfile(){

        // Instantiate a new fragment.
        Fragment profile = new ProfileFragment();
        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        ft.replace(R.id.fragment_home, profile);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     *go to home fragment
     */
    private void goToHome(){

        // Instantiate a new fragment.
        Fragment home = new HomeFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        ft.replace(R.id.fragment_home, home);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     *start category activity
     */
    private void goToCategory(){
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        startActivity(intent);
    }

    /**
     *start add expenditure activity
     */
    private void goToAddExpenditure(){
        Intent intent = new Intent(getActivity(), addExpenditureActivity.class);
        startActivity(intent);

    }

    /**
     *start go to search activity
     */
    private void goToSearch(){
        Intent intent = new Intent(getActivity(), Search.class);
        startActivity(intent);

    }

}
