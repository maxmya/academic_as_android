package com.graduation.academic.as.activities;

import android.app.ProgressDialog;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.graduation.academic.as.App;
import com.graduation.academic.as.R;
import com.squareup.picasso.Picasso;

import org.chat21.android.core.ChatManager;
import org.chat21.android.core.contacts.listeners.OnContactCreatedCallback;
import org.chat21.android.core.exception.ChatRuntimeException;
import org.chat21.android.core.users.models.ChatUser;
import org.chat21.android.core.users.models.IChatUser;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = Home.class.getSimpleName();
    private CircleImageView userPP;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userName = navigationView.getHeaderView(0).findViewById(R.id.fullname);
        userPP = navigationView.getHeaderView(0).findViewById(R.id.profile);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("users").document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String fullName = (String) documentSnapshot.get("fullname");
                String ppUrl = (String) documentSnapshot.get("ppURL");
                userName.setText(fullName);
                setupChatContact(uid, fullName, "", "", ppUrl);
                Picasso.get().load(ppUrl).placeholder(R.drawable.user).into(userPP);
            }
        });
    }

    void setupChatContact(String uid, String fname, String email, String lname, String ppurl) {
        App.initChat(this);
        IChatUser iChatUser = new ChatUser();
        iChatUser.setEmail("");
        iChatUser.setId(uid);
        iChatUser.setFullName(fname);
        // iChatUser.setFullName(currentUser.getName());
        // iChatUser.setProfilePictureUrl(currentUser.getPpURL());
        ChatManager.start(this, App.mChatConfiguration, iChatUser);
        ChatManager.getInstance().setLoggedUser(iChatUser);
        ChatManager.getInstance().createContactFor(uid, email, fname, lname, ppurl, new OnContactCreatedCallback() {
            @Override
            public void onContactCreatedSuccess(ChatRuntimeException exception) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.groups) {
            App.openActivityWithFadeAnim(GroupsListActivity.class, this, false);
        } else if (id == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            App.deleteLoginFromPrefs();
            final ProgressDialog pd = new ProgressDialog(Home.this);
            pd.setMessage("Logging Out .. ");
            pd.show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                    App.openActivityWithFadeAnim(LoginActivity.class, Home.this, true);
                }
            }, 2000);
        } else if (id == R.id.chat) {
            App.openChat(this);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
