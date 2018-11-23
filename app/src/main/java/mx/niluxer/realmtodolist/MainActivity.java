package mx.niluxer.realmtodolist;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    Realm realm;
    ToDoRealmAdapter toDoRealmAdapter;


    public static final int[] COLORS = new int[] {
            Color.argb(255, 28, 160, 170),
            Color.argb(255, 99, 161, 247),
            Color.argb(255, 13, 79, 139),
            Color.argb(255, 89, 113, 173),
            Color.argb(255, 200, 213, 219),
            Color.argb(255, 99, 214, 74),
            Color.argb(255, 205, 92, 92),
            Color.argb(255, 105, 5, 98)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.fab);

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().name("myrealm.realm").build();
        Realm.setDefaultConfiguration(configuration);

        realm = Realm.getDefaultInstance();
        RealmResults<TodoItem> toDoItems = realm
                .where(TodoItem.class)
                .findAll();

        RecyclerView realmRecyclerView = (RecyclerView) findViewById(R.id.realm_recycler_view);
        toDoRealmAdapter = new ToDoRealmAdapter(this, toDoItems);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        realmRecyclerView.setLayoutManager(layoutManager);
        realmRecyclerView.setAdapter(toDoRealmAdapter);
        realmRecyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        realmRecyclerView.addItemDecoration(itemDecoration);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAndShowInputDialog();
            }
        });

    }

    private void buildAndShowInputDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Create A Task");

        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.to_do_dialog_view, null);
        final EditText input = (EditText) dialogView.findViewById(R.id.input);

        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToDoItem(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.show();
        input.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE ||
                                (event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            dialog.dismiss();
                            addToDoItem(input.getText().toString());
                            return true;
                        }
                        return false;
                    }
                });
    }

    private void addToDoItem(String toDoItemText) {
        if (toDoItemText == null || toDoItemText.length() == 0) {
            Toast
                    .makeText(this, "Empty ToDos don't get stuff done!", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        realm.beginTransaction();
        //Random r = new Random();
        TodoItem todoItem = realm.createObject(TodoItem.class, System.currentTimeMillis());
        //todoItem.setId(System.currentTimeMillis());

        todoItem.setDescription(toDoItemText);
        realm.commitTransaction();
        reloadTodoList();
    }

    private void reloadTodoList()
    {
        RealmResults<TodoItem> toDoItems = realm
                .where(TodoItem.class)
                .findAll();
        toDoRealmAdapter.setRealmResults(toDoItems);
        toDoRealmAdapter.notifyDataSetChanged();

    }

}
