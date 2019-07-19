package com.c0llabor8.kanban.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import com.c0llabor8.kanban.R;
import com.c0llabor8.kanban.adapter.ProjectPagerAdapter;
import com.c0llabor8.kanban.databinding.ActivityMainBinding;
import com.c0llabor8.kanban.fragment.TaskListFragment;
import com.c0llabor8.kanban.fragment.dialog.NewProjectDialog;
import com.c0llabor8.kanban.fragment.dialog.NewTaskDialog;
import com.c0llabor8.kanban.fragment.sheet.BottomNavigationSheet;
import com.c0llabor8.kanban.fragment.sheet.BottomNavigationSheet.BottomNavSheetListener;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements BottomNavSheetListener {

  ActivityMainBinding binding;
  Handler handler;
  BottomNavigationSheet navFragment;
  ProjectPagerAdapter pagerAdapter;
  NewTaskDialog newTaskDialog;
  NewProjectDialog newProjectDialog;
  SparseArray<String> projectMenuMap = new SparseArray<>();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    handler = new Handler();
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    // Simulate a request for all projects the user is a member
    String[] projects = {
        "Demo Project",
        "Facebook Project",
        "School Project"
    };

    // Using a handler to simulate network requests
    handler.postDelayed(() -> {
      for (int i = 0; i < projects.length; i++) {
        projectMenuMap.put(Menu.FIRST + i, projects[i]);
      }
    }, 300);

    Fragment[] fragments = {
        TaskListFragment.newInstance()
    };

    // Initialize the pagination of our fragments based off our initial Fragments
    pagerAdapter = new ProjectPagerAdapter(getSupportFragmentManager(), fragments);

    binding.pager.setAdapter(pagerAdapter);
    binding.tabs.setupWithViewPager(binding.pager, true);

    setSupportActionBar(binding.bar);

    // Create and save a new instance of our BottomNavigationSheet
    navFragment = BottomNavigationSheet.newInstance();
    binding.fab.setOnClickListener(view -> openTaskCreationDialog());

    newTaskDialog = NewTaskDialog.newInstance();
    newProjectDialog = NewProjectDialog.newInstance();
  }

  private void openTaskCreationDialog() {
    newTaskDialog.show(getSupportFragmentManager(), "");
  }

  /*
   * Sets the text for TextView(R.id.bottom_title) within the BottomAppBar
   *
   * @param title text to display in as the title
   * */
  @Override
  public void setTitle(CharSequence title) {
    binding.bottomTitle.setText(title);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    // Show our BottomNavigationSheet when the menu icon is clicked
    if (item.getItemId() == android.R.id.home) {
      navFragment.show(getSupportFragmentManager(), "");
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onAttachFragment(@NonNull Fragment fragment) {
    if (fragment instanceof BottomNavigationSheet) {
      BottomNavigationSheet navFragment = (BottomNavigationSheet) fragment;
      navFragment.setListener(this);
    }
  }

  /*
   * Inflate the project menu once the BottomSheet's view is created based off our projectMenuMap
   * (SparseArray)
   * */
  @Override
  public void populateProjects(SubMenu subMenu) {
    for (int i = 0; i < projectMenuMap.size(); i++) {
      int key = projectMenuMap.keyAt(i);
      subMenu.add(Menu.NONE, key, key, projectMenuMap.get(key));
    }
  }

  /*
   * Listener used by the BottomNavSheet to determine which navigation item was selected
   * */
  @Override
  public OnNavigationItemSelectedListener onBottomNavItemSelected() {
    return item -> {

      // If the selected item is the user's personal tasks
      if (item.getItemId() == R.id.my_tasks) {
        setTitle(item.getTitle());
        navFragment.dismiss();
        return true;
      }

      // if the selected item's id is in our HashSet<int(menuID), String(Project)>
      if (projectMenuMap.indexOfKey(item.getItemId()) > -1) {
        setTitle(projectMenuMap.get(item.getItemId()));
        navFragment.dismiss();
        return true;
      }

      if (item.getItemId() == R.id.new_project) {
        newProjectDialog.show(getSupportFragmentManager(), "");
        navFragment.dismiss();
        return true;
      }

      if (item.getItemId() == R.id.action_signout) {
        ParseUser.logOutInBackground(new LogOutCallback() {
          @Override
          public void done(ParseException e) {
            Intent intent = new Intent(MainActivity.this, AuthActivity.class);
            startActivity(intent);
            finish();
          }
        });
      }

      return false;
    };
  }
}
