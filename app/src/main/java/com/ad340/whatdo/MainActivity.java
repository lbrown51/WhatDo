package com.ad340.whatdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private List<ToDoItem> toDoList;
    private MaterialToolbar header;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView toDoRecyclerView = findViewById(R.id.todo_list_recycler_view);
        toDoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        toDoList = new ArrayList<>();
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));
        toDoList.add(new ToDoItem("Test"));

        ToDoItemRecyclerViewAdapter adapter = new ToDoItemRecyclerViewAdapter(toDoList, this);
        toDoRecyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.large_item_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.small_item_spacing);
        toDoRecyclerView.addItemDecoration(new ToDoItemDecoration(largePadding, smallPadding));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
                showCreateDialog();
            });

        Calendar today = Calendar.getInstance();
        header = findViewById(R.id.top_app_bar);
        StringBuilder displayText = new StringBuilder(header.getTitle());
        // probably a more elegant way of doing this
        // if you save whitespace as a string, it comes out with quotation marks :(
        displayText.append("      ");
        displayText.append(today.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH));
        displayText.append(String.format(" %02d, %04d",
                today.get(Calendar.DAY_OF_MONTH),
                today.get(Calendar.YEAR)));
        header.setTitle(displayText);
    }

    private void showCreateDialog() {
        final View createView = View.inflate(this, R.layout.create_todo_dialog, null);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(createView);

        ImageButton closeButton = (ImageButton) dialog.findViewById(R.id.close_dialog);
        closeButton.setOnClickListener((view) -> {
            revealShow(createView, false, dialog);
        });

        dialog.setOnShowListener((view) -> {
            revealShow(createView, true, null);
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK){
                    revealShow(createView, false, dialog);
                    return true;
                };
                return false;
            };
        });

        dialog.show();
    };

    private void revealShow(View dialogView, boolean open, final Dialog dialog) {

        final View view = dialogView.findViewById(R.id.create_todo_dialog);
//        int w = view.getWidth();
//        int h = view.getHeight();
//
//        int endRadius = (int) Math.hypot(w, h);
//
//        int cx = (int)(fab.getX() + (fab.getWidth())/2);
//        int cy = (int) fab.getY() + fab.getHeight() + 56;

        if(open){
            // animation not working - try something else
            // Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view, cx,cy, 0, endRadius);
            view.setVisibility(View.VISIBLE);
//            revealAnimator.setDuration(700);
//            revealAnimator.start();
        } else {
            dialog.dismiss();
            view.setVisibility(View.INVISIBLE);
//            Animator closeAnimator =
//                    ViewAnimationUtils.createCircularReveal(view, cx, cy, endRadius, 0);

//            closeAnimator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    dialog.dismiss();
//                    view.setVisibility(View.INVISIBLE);
//                }
//            });
//            closeAnimator.setDuration(700);
//            closeAnimator.start();
        };
    };

}
