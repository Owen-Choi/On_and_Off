package org.techtown.sns_project.Normal.Search;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.techtown.sns_project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class NormalSearchActivity extends AppCompatActivity {

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    ArrayList<SearchTitleClass> arraylist = new ArrayList<SearchTitleClass>();
    HashMap<String,Object> HashMap = new HashMap<String,Object>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_search);
        ArrayList<String> james = new ArrayList<>();
        // Generate sample data

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collectionGroup("brand").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    HashMap = (HashMap<String, Object>) documentSnapshot.getData();
                    String info = (String) HashMap.get("info");
                    String title = (String) HashMap.get("title");
                    String url = (String) HashMap.get("url");
                    SearchTitleClass stc = new SearchTitleClass(title,info,url);
                    arraylist.add(stc);
                }
                list = (ListView) findViewById(R.id.listview);
                adapter = new ListViewAdapter(this, arraylist);
                list.setAdapter(adapter);

                // Locate the EditText in listview_main.xml
                editsearch = (SearchView) findViewById(R.id.search);
                editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        adapter.filter(s);
                        return false;
                    }
                });
            }
        });
        // Locate the ListView in listview_main.xml

    }


}



