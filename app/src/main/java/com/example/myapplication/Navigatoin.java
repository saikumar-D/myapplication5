package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Navigatoin extends AppCompatActivity {
    private FirebaseAuth mAuth;

private NavigationView navigationView;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigatoin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);
      fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

 /*final NavController navController = Navigation.findNavController(this, R.id.nav_view);
     NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
   navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
           public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int id = destination.getId();
                switch (id) {
                    case R.id.nav_gallery:
                        Intent friendintent = new Intent(Navigatoin.this,friends.class);
                        startActivity(friendintent);
                        break;
                    default:
                        Toast.makeText(Navigatoin.this,"error",Toast.LENGTH_LONG).show();
                }
            }
        });*/
navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        switch (id)
        {
            case R.id.nav_gallery:
                Intent friendintent = new Intent(Navigatoin.this,friends.class);
                startActivity(friendintent);
                break;
            default:
                Toast.makeText(Navigatoin.this,"error",Toast.LENGTH_LONG).show();

        }
        return true;
    }
});
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigatoin, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Logout:
                mAuth.signOut();
                senttologin1();
                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(Navigatoin.this, settings.class);
                startActivity(settingsIntent);
                break;
            case R.id.action_allusers:
                Intent usersintent = new Intent(Navigatoin.this, allusers.class);
                startActivity(usersintent);
                break;
            default:
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    public void senttologin1() {
        Intent logintent1 = new Intent(Navigatoin.this, login.class);
        //  logintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logintent1);
        finish();
        //startActivity(new Intent(getApplicationContext(),Navigation.class));
    }


 /*  public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/


    }
