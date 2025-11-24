package com.mts.mea.nodust_app.Vacation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mts.mea.nodust_app.Adapters.VacationAdapter;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.common.DataBaseHelper;

import java.util.List;

public class Vacationlist_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    DataBaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacationlist_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.Vacations));
        setSupportActionBar(toolbar);
        DB=new DataBaseHelper(this);
      //  IntiUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntiUI();
    }

    private void IntiUI() {
        recyclerView=(RecyclerView)findViewById(R.id.recView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(Vacationlist_Activity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        List<VacationRequest> lst_Vacations=DB.GetVacations();
        VacationAdapter adapter=new VacationAdapter(this,lst_Vacations);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_details_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_assign_task)
        {
            // open add request
            Intent i=new Intent(Vacationlist_Activity.this,VacationAdd_Activity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
}
