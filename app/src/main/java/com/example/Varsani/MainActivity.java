package com.example.Varsani;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.Varsani.Clients.CompletedServices;
import com.example.Varsani.Clients.ContactUs;
import com.example.Varsani.Clients.Invoice;
import com.example.Varsani.Clients.MyBookings;
import com.example.Varsani.Clients.Search;
import com.example.Varsani.Clients.ServiceItems;
import com.example.Varsani.Clients.SupplierLogin;
import com.example.Varsani.Clients.home.HomeFragment;
import com.example.Varsani.Staff.Dashboard;
import com.example.Varsani.Staff.SelectLogin;
import com.example.Varsani.Clients.About;
import com.example.Varsani.Clients.Adapters.AdapterProducts;
import com.example.Varsani.Clients.CartItems;
import com.example.Varsani.Clients.Feedback;
import com.example.Varsani.Clients.Help_in;
import com.example.Varsani.Clients.Login;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.Clients.Orders_hist;
import com.example.Varsani.Clients.Profile;
import com.example.Varsani.Clients.Register;
import com.example.Varsani.R;
import com.example.Varsani.Suppliers.ApprovedSupplies;
import com.example.Varsani.Suppliers.MyRequests;
import com.example.Varsani.Suppliers.RegSuppliers;
import com.example.Varsani.Suppliers.Requests;
import com.example.Varsani.utils.SessionHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private UserModel user;
    private SessionHandler session;
    AdapterProducts adapterProducts;
    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_bar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        FloatingActionButton fab = findViewById(R.id.fab);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        homeFragment = new HomeFragment();

        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.isLoggedIn()) {
                    Intent in = new Intent(getApplicationContext(), CartItems.class);
                    startActivity(in);
                } else {
                    Snackbar.make(view, "You must login to view cart", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
        fab.setImageResource(R.drawable.ic_shopping_cart_white);
        */

        if (session.isLoggedIn()) {
            if (user.getUser_type().equals("Client") || user.getUser_type().equals("Supplier")) {
                // DO NOTHING
            } else {
                Intent in = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(in);
                finish();
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent in = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(in);

                } else if (id == R.id.nav_profile) {
                    Intent p = new Intent(getApplicationContext(), Profile.class);
                    startActivity(p);

                } else if (id == R.id.nav_request) {
                    Intent nr = new Intent(getApplicationContext(), MyRequests.class);
                    startActivity(nr);

                } else if (id == R.id.nav_approved_supplies) {
                    Intent nr = new Intent(getApplicationContext(), ApprovedSupplies.class);
                    startActivity(nr);

                }
                else if (id == R.id.nav_register) {
                    Intent intent = new Intent(getApplicationContext(), Register.class);
                    startActivity(intent);

                } else if (id == R.id.nav_reg_supplier) {
                    Intent ent = new Intent(getApplicationContext(), RegSuppliers.class);
                    startActivity(ent);

                } else if (id == R.id.nav_login) {
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);

                } else if (id == R.id.nav_contact) {
                    Intent ctn = new Intent(getApplicationContext(), ContactUs.class);
                    startActivity(ctn);

                } else if (id == R.id.nav_supplier_login) {
                    Intent isp = new Intent(getApplicationContext(), SupplierLogin.class);
                    startActivity(isp);

                } else if (id == R.id.nav_bookings) {
                    Intent bk = new Intent(getApplicationContext(), ServiceItems.class);
                    startActivity(bk);

                } else if (id == R.id.nav_my_bookings) {
                    Intent nmb = new Intent(getApplicationContext(), MyBookings.class);
                    startActivity(nmb);

                } else if (id == R.id.nav_completion) {
                    Intent cp = new Intent(getApplicationContext(), CompletedServices.class);
                    startActivity(cp);

                } else if (id == R.id.nav_invoice) {
                    Intent inv = new Intent(getApplicationContext(), Invoice.class);
                    startActivity(inv);

                } else if (id == R.id.nav_orders) {
                    if (user.getUser_type().equals("Client")) {
                        Intent o = new Intent(getApplicationContext(), Orders_hist.class);
                        startActivity(o);
                    } else {
                        Intent o = new Intent(getApplicationContext(), Requests.class);
                        startActivity(o);
                    }

                } else if (id == R.id.nav_feedback) {
                    Intent fbs = new Intent(getApplicationContext(), Feedback.class);
                    startActivity(fbs);

                } else if (id == R.id.nav_staff_login) {
                    Intent s = new Intent(getApplicationContext(), SelectLogin.class);
                    startActivity(s);

                } else if (id == R.id.nav_help) {
                    Intent he = new Intent(getApplicationContext(), Help_in.class);
                    startActivity(he);

                } else if (id == R.id.nav_about) {
                    Intent ab = new Intent(getApplicationContext(), About.class);
                    startActivity(ab);

                } else if (id == R.id.nav_logout) {
                    alertLogout();
                }

                drawer.closeDrawer(GravityCompat.START, true);
                return false;
            }
        });


        drawer.closeDrawers();

        check();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void alertLogout() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Do you want to logout?");
        alertDialog.setCancelable(false);
        alertDialog.setButton("Yes logout", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                session.logoutUser();
                Toast.makeText(getApplicationContext(), "You are logged out", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });
        alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void check() {
        // Default hide most items
        navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_feedback).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_orders).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_request).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_approved_supplies).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_reg_supplier).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_bookings).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_my_bookings).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_completion).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_invoice).setVisible(false);

        if (session.isLoggedIn()) {
            // Hide auth/register entries for logged-in users
            navigationView.getMenu().findItem(R.id.nav_register).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_reg_supplier).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_staff_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_supplier_login).setVisible(false);

            // Role-specific visibility
            if (user.getUser_type().equals("Client")) {
                navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_feedback).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                // navigationView.getMenu().findItem(R.id.nav_orders).setVisible(true);
                // navigationView.getMenu().findItem(R.id.nav_bookings).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_my_bookings).setVisible(true);

            } else if (user.getUser_type().equals("Supplier")) {
                navigationView.getMenu().findItem(R.id.nav_profile).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_feedback).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_request).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_approved_supplies).setVisible(true);
                navigationView.getMenu().findItem(R.id.nav_invoice).setVisible(false);
            }
            // else if (...) { // add more roles here when needed
            // }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        if (item.getItemId() == R.id.action_settings) {
            Intent s = new Intent(getApplicationContext(), Search.class);
            startActivity(s);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
