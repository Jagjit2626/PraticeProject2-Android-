package easy.tuto.taskmasterpro;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText taskEditText;
    private Button addButton;
    private ListView taskListView;
    private SearchView searchView;
    private ArrayList<Task> taskList;
    private TaskAdapter adapter;
    private ArrayList<Task> filteredTaskList; // New filtered list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskEditText = findViewById(R.id.taskEditText);
        addButton = findViewById(R.id.addButton);
        taskListView = findViewById(R.id.taskListView);
        searchView = findViewById(R.id.searchView);

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);
        taskListView.setAdapter(adapter);
        filteredTaskList = new ArrayList<>(); // Initialize the filtered list

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskDescription = taskEditText.getText().toString().trim();
                if (!taskDescription.isEmpty()) {
                    Task task = new Task(taskDescription);
                    taskList.add(task);
                    adapter.notifyDataSetChanged();
                    taskEditText.setText("");
                }
            }
        });

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Handle task item click (e.g., mark as completed).
                Task task = taskList.get(position);
                task.toggleCompleted();
                adapter.notifyDataSetChanged();
            }
        });

        // Set up the SearchView for filtering tasks
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the task list based on the user's input
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
