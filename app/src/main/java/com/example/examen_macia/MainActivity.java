package com.example.examen_macia;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListFragment.OnActivitySelectedListener {

    private static final String TAG_LIST_FRAGMENT = "list_fragment";
    private static final String TAG_DETAIL_FRAGMENT = "detail_fragment";
    private static final String STATE_ACTIVITIES = "state_activities";
    private static final String STATE_SELECTED_ACTIVITY = "state_selected_activity";

    private boolean isDualPane;
    private ListFragment listFragment;
    private DetailFragment detailFragment;
    private List<Activitat> activities; // Centralized activities list
    private Activitat currentSelectedActivity; // Track currently selected activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if we're in landscape mode (dual pane)
        isDualPane = findViewById(R.id.fragment_container_detail) != null;

        // Restore or initialize activities list
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVITIES)) {
            activities = (ArrayList<Activitat>) savedInstanceState.getSerializable(STATE_ACTIVITIES);
            // Restore the selected activity
            if (savedInstanceState.containsKey(STATE_SELECTED_ACTIVITY)) {
                currentSelectedActivity = (Activitat) savedInstanceState.getSerializable(STATE_SELECTED_ACTIVITY);
            }
        } else {
            activities = initializeActivities();
        }

        if (savedInstanceState == null) {
            // First time creating the activity
            setupFragments();
        } else {
            // Restore fragments after rotation
            restoreFragments();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the activities list at the activity level
        if (activities != null) {
            outState.putSerializable(STATE_ACTIVITIES, new ArrayList<>(activities));
        }
        // Save the currently selected activity
        if (currentSelectedActivity != null) {
            outState.putSerializable(STATE_SELECTED_ACTIVITY, currentSelectedActivity);
        }
    }

    private List<Activitat> initializeActivities() {
        List<Activitat> list = new ArrayList<>();

        list.add(new Activitat(
            "Yoga",
            "Yoga és una pràctica ancestral que combina postures físiques, tècniques de respiració i meditació per millorar la flexibilitat, la força i el benestar mental.",
            android.R.drawable.ic_menu_compass,
            "60 minuts",
            "Baixa-Mitjana"
        ));

        list.add(new Activitat(
            "Spinning",
            "Classe de ciclisme indoor d'alta intensitat amb música motivadora. Perfecte per cremar calories i millorar la resistència cardiovascular.",
            android.R.drawable.ic_menu_rotate,
            "45 minuts",
            "Alta"
        ));

        list.add(new Activitat(
            "Zumba",
            "Ball fitness que combina ritmes llatins amb exercicis aeròbics. Una manera divertida de fer exercici mentre gaudeixes de la música.",
            android.R.drawable.ic_menu_share,
            "55 minuts",
            "Mitjana"
        ));

        list.add(new Activitat(
            "CrossFit",
            "Entrenament funcional d'alta intensitat que combina halterofília, gimnàstica i exercicis cardiovasculars per desenvolupar força i resistència.",
            android.R.drawable.ic_menu_mylocation,
            "60 minuts",
            "Molt Alta"
        ));

        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_activity) {
            showAddActivityDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddActivityDialog() {
        AddActivityDialogFragment dialog = new AddActivityDialogFragment();
        dialog.setOnActivityAddedListener(activitat -> {
            // Add to the centralized list
            activities.add(activitat);

            // Update the list fragment if it exists and is visible
            if (listFragment != null && listFragment.isAdded()) {
                listFragment.setActivities(activities);
            }
        });
        dialog.show(getSupportFragmentManager(), "AddActivityDialog");
    }

    private void setupFragments() {
        listFragment = new ListFragment();
        listFragment.setActivities(activities);
        listFragment.setOnActivitySelectedListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (isDualPane) {
            // Landscape: show both fragments
            transaction.replace(R.id.fragment_container_list, listFragment, TAG_LIST_FRAGMENT);

            // Show the first activity by default in landscape
            if (!activities.isEmpty()) {
                detailFragment = DetailFragment.newInstance(activities.get(0));
            } else {
                detailFragment = new DetailFragment();
            }
            transaction.replace(R.id.fragment_container_detail, detailFragment, TAG_DETAIL_FRAGMENT);
        } else {
            // Portrait: show only list fragment
            transaction.replace(R.id.fragment_container, listFragment, TAG_LIST_FRAGMENT);
        }

        transaction.commit();
    }

    private void restoreFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (isDualPane) {
            // Landscape mode

            // Clear the back stack since we're switching to dual pane mode
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            // Find existing fragments
            ListFragment oldListFragment = (ListFragment) fragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT);
            DetailFragment oldDetailFragment = (DetailFragment) fragmentManager.findFragmentByTag(TAG_DETAIL_FRAGMENT);

            // Determine which activity to display
            Activitat selectedActivity = null;

            // Priority 1: Use currentSelectedActivity if available (saved from state)
            if (currentSelectedActivity != null) {
                selectedActivity = currentSelectedActivity;
            }
            // Priority 2: Try to get from existing detail fragment
            else if (oldDetailFragment != null && oldDetailFragment.getArguments() != null) {
                selectedActivity = (Activitat) oldDetailFragment.getArguments().getSerializable(DetailFragment.ARG_ACTIVITAT);
            }
            // Priority 3: Default to first activity
            else if (!activities.isEmpty()) {
                selectedActivity = activities.get(0);
            }

            // Remove ALL old fragments - we need clean state
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (oldListFragment != null) {
                transaction.remove(oldListFragment);
            }
            if (oldDetailFragment != null) {
                transaction.remove(oldDetailFragment);
            }

            transaction.commitNow(); // Use commitNow to execute immediately

            // Now recreate the fragments in the correct containers
            listFragment = new ListFragment();
            listFragment.setActivities(activities); // Always use the centralized list

            // Create detail fragment with the selected activity
            if (selectedActivity != null) {
                detailFragment = DetailFragment.newInstance(selectedActivity);
                currentSelectedActivity = selectedActivity; // Update current selection
            } else {
                detailFragment = new DetailFragment();
            }

            listFragment.setOnActivitySelectedListener(this);

            // Add fragments to their new containers
            transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container_list, listFragment, TAG_LIST_FRAGMENT);
            transaction.replace(R.id.fragment_container_detail, detailFragment, TAG_DETAIL_FRAGMENT);
            transaction.commit();

        } else {
            // Portrait mode - restore the appropriate fragment

            // Find existing fragments
            ListFragment oldListFragment = (ListFragment) fragmentManager.findFragmentByTag(TAG_LIST_FRAGMENT);
            DetailFragment oldDetailFragment = (DetailFragment) fragmentManager.findFragmentByTag(TAG_DETAIL_FRAGMENT);

            // Always remove fragments when switching to portrait mode to ensure clean state
            if (oldListFragment != null || oldDetailFragment != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Remove both fragments first
                if (oldListFragment != null) {
                    transaction.remove(oldListFragment);
                }
                if (oldDetailFragment != null) {
                    transaction.remove(oldDetailFragment);
                }

                transaction.commitNow();
            }

            // Create new list fragment
            listFragment = new ListFragment();
            listFragment.setActivities(activities); // Use centralized list
            listFragment.setOnActivitySelectedListener(this);

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, listFragment, TAG_LIST_FRAGMENT);
            transaction.commit();
        }

        // Re-attach the listener
        if (listFragment != null) {
            listFragment.setOnActivitySelectedListener(this);
        }
    }

    @Override
    public void onActivitySelected(Activitat activitat) {
        // Update the currently selected activity
        currentSelectedActivity = activitat;

        if (isDualPane) {
            // Landscape mode: update the detail fragment on the right
            if (detailFragment == null) {
                detailFragment = DetailFragment.newInstance(activitat);
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_detail, detailFragment, TAG_DETAIL_FRAGMENT)
                    .commit();
            } else {
                detailFragment.updateActivity(activitat);
            }
        } else {
            // Portrait mode: replace list fragment with detail fragment
            detailFragment = DetailFragment.newInstance(activitat);
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailFragment, TAG_DETAIL_FRAGMENT)
                .addToBackStack(null)
                .commit();
        }
    }
}
