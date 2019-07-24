package com.c0llabor8.kanban.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.c0llabor8.kanban.R;
import com.c0llabor8.kanban.adapter.TaskListAdapter;
import com.c0llabor8.kanban.databinding.FragmentTaskListBinding;
import com.c0llabor8.kanban.fragment.base.BaseTaskFragment;
import com.c0llabor8.kanban.fragment.dialog.NewTaskDialog.TaskCreatedListener;
import com.c0llabor8.kanban.model.Project;
import com.c0llabor8.kanban.model.Task;
import com.parse.FindCallback;
import com.parse.ParseException;
import java.util.List;

public class TaskListFragment extends BaseTaskFragment implements TaskCreatedListener {

  private TaskListAdapter listAdapter;
  private FragmentTaskListBinding binding;

  public static TaskListFragment newInstance() {
    return newInstance(new Bundle());
  }

  public static TaskListFragment newInstance(Bundle args) {
    args.putString("title", "Tasks");

    TaskListFragment fragment = new TaskListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task_list, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    listAdapter = new TaskListAdapter(getTaskList());
    binding.rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
    binding.rvTasks.setAdapter(listAdapter);
  }

  @Override
  public void onTasksLoaded() {
    listAdapter.notifyDataSetChanged();
  }

  @Override
  public void onTaskCreated() {
    reloadTasks();
  }
}
