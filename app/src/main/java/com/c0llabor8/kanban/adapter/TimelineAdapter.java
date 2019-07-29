package com.c0llabor8.kanban.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.c0llabor8.kanban.R;
import com.c0llabor8.kanban.databinding.ListItemTimelineBinding;
import com.c0llabor8.kanban.model.Task;
import com.c0llabor8.kanban.util.DateTimeUtils;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

  private static final int VIEW_TYPE_TOP = 0;
  private static final int VIEW_TYPE_MIDDLE = 1;
  private static final int VIEW_TYPE_BOTTOM = 2;

  private List<Task> tasks;

  public TimelineAdapter(List<Task> tasks) {
    this.tasks = tasks;
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position,
      @NonNull List<Object> payloads) {
    super.onBindViewHolder(holder, position, payloads);
  }


  @NonNull
  @Override
  public TimelineAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());

    ListItemTimelineBinding binding =
        ListItemTimelineBinding.inflate(inflater, parent, false);

    return new ViewHolder(binding);

  }

  @Override
  public void onBindViewHolder(@NonNull TimelineAdapter.ViewHolder holder, int position) {
    final Task task = tasks.get(position);
    // Populate all tasks
    holder.populateTasks(task, getItemViewType(position));
  }


  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return VIEW_TYPE_TOP;
    } else if (position == tasks.size() - 1) {
      return VIEW_TYPE_BOTTOM;
    }
    return VIEW_TYPE_MIDDLE;
  }


  @Override
  public int getItemCount() {
    return tasks.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder {

    private ListItemTimelineBinding binding;


    public ViewHolder(ListItemTimelineBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    /**
     * takes necessary information about tasks and assigns to correct layout For different locations
     * within the timeline, the layout should change
     */

    public void populateTasks(Task task, int type) {
      binding.itemTitle.setText(task.getTitle());
      binding.itemDescription.setText(task.getDescription());
      binding.itemDate.setText(DateTimeUtils.getDateString(task.getEstimate()));
      setPriority(task);

      switch (type) {
        case VIEW_TYPE_TOP:
          // The top of the line has to be rounded
          binding.contLine.setImageResource(R.drawable.line_bg_top);
          break;
        case VIEW_TYPE_MIDDLE:
          // but a drawable can be used to make the cap rounded also here
          //binding.contLine.setImageResource(R.drawable.line_bg_top);
          break;
        case VIEW_TYPE_BOTTOM:
          //also rounded the bottom
          binding.contLine.setImageResource(R.drawable.line_bg_bottom);
          break;
      }
    }

    /**
     * changes color of icon on timeline based on priority level
     */
    private void setPriority(Task task) {
      if (task.getPriority() == 0) {
        binding.ivPriority.setImageResource(R.drawable.circle_priority_low);
      } else if (task.getPriority() == 1) {
        binding.ivPriority.setImageResource(R.drawable.circle_priority_medium);
      } else {
        binding.ivPriority.setImageResource(R.drawable.circle_priority_high);
      }
    }
  }

}
