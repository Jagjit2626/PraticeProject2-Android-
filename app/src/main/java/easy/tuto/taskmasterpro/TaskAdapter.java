package easy.tuto.taskmasterpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;

public class TaskAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<Task> taskList;
    private ArrayList<Task> filteredTaskList; // New filtered list
    private LayoutInflater inflater;

    public TaskAdapter(Context context, ArrayList<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
        this.filteredTaskList = taskList; // Initialize filtered list with all tasks
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.task_item, null);
        }

        Task task = filteredTaskList.get(position);

        TextView descriptionTextView = view.findViewById(R.id.textViewDescription);
        CheckBox completedCheckBox = view.findViewById(R.id.checkBoxCompleted);

        descriptionTextView.setText(task.getDescription());
        completedCheckBox.setChecked(task.isCompleted());

        // Handle checkbox click to remove the task
        completedCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                Task task = (Task) checkBox.getTag();
                task.toggleCompleted();
                removeTask(task);
            }
        });

        // Set the task as the tag for the checkbox
        completedCheckBox.setTag(task);

        return view;
    }

    @Override
    public Filter getFilter() {
        return new CustomFilter();
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Task> filteredTasks = new ArrayList<>();
                for (Task task : taskList) {
                    if (task.getDescription().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredTasks.add(task);
                    }
                }
                results.count = filteredTasks.size();
                results.values = filteredTasks;
            } else {
                results.count = taskList.size();
                results.values = taskList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredTaskList = (ArrayList<Task>) results.values;
            notifyDataSetChanged();
        }
    }

    // Helper method to remove a task
    private void removeTask(Task task) {
        taskList.remove(task);
        notifyDataSetChanged();
    }
}
