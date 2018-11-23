package mx.niluxer.realmtodolist;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import io.realm.RealmResults;


public class ToDoRealmAdapter
        extends RecyclerView.Adapter<ToDoRealmAdapter.ViewHolder> {

    Context context;
    RealmResults<TodoItem> realmResults;

    public ToDoRealmAdapter(Context context,
                            RealmResults<TodoItem> realmResults) {
        this.context = context;
        this.realmResults = realmResults;
    }

    public void setRealmResults(RealmResults<TodoItem> realmResults) {
        this.realmResults = realmResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context1 = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context1);

        View v = inflater.inflate(R.layout.to_do_item_view, viewGroup, false);
        ViewHolder vh = new ViewHolder((FrameLayout) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final TodoItem toDoItem = realmResults.get(i);
        System.out.println("DESC -> " + toDoItem.getDescription());
        viewHolder.todoTextView.setText(toDoItem.getDescription());
        viewHolder.itemView.setBackgroundColor(
                MainActivity.COLORS[(int) (toDoItem.getId() % MainActivity.COLORS.length)]
        );

    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView todoTextView;
        public ViewHolder(FrameLayout container) {
            super(container);
            this.todoTextView = (TextView) container.findViewById(R.id.todo_text_view);
        }
    }

}
