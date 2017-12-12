package com.example.helplah;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helplah.data.Message;
import com.example.helplah.fixtures.DialogsFixtures;
import com.squareup.picasso.Picasso;

import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;
import com.example.helplah.data.Dialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DialogListActivity extends AppCompatActivity implements DialogsListAdapter.OnDialogClickListener<Dialog>,
        DialogsListAdapter.OnDialogLongClickListener<Dialog> {
    private ArrayList<Dialog> dialogs;
    protected ImageLoader imageLoader;
    protected DialogsListAdapter<Dialog> dialogsAdapter;

    public static void open(Context context) {
        context.startActivity(new Intent(context, DialogListActivity.class));
    }

    private DialogsList dialogsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_list);
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(DialogListActivity.this).load(url).into(imageView);
            }
        };
        dialogsList = (DialogsList) findViewById(R.id.dialogsList);
        initAdapter();
    }

    private void initAdapter() {
        this.dialogsAdapter = new DialogsListAdapter<>(this.imageLoader);
        this.dialogsAdapter.setItems(DialogsFixtures.getDialogs());

        this.dialogsAdapter.setOnDialogClickListener(this);
        this.dialogsAdapter.setOnDialogLongClickListener(this);

        dialogsList.setAdapter(this.dialogsAdapter);
    }


    @Override
    public void onDialogLongClick(Dialog dialog) {

    }

    @Override
    public void onDialogClick(Dialog dialog) {
        ConversationActivity.open(this);
    }

    private void onNewMessage(String dialogId, Message message) {
        boolean isUpdated = dialogsAdapter.updateDialogWithMessage(dialogId, message);
        if (!isUpdated) {
            //Dialog with this ID doesn't exist, so you can create new Dialog or update all dialogs list
        }
    }

    //for example
    private void onNewDialog(Dialog dialog) {
        dialogsAdapter.addItem(dialog);
    }
}