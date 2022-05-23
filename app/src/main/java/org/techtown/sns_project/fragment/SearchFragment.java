package org.techtown.sns_project.fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;

import org.techtown.sns_project.Normal.Search.ListViewAdapter;
import org.techtown.sns_project.Normal.Search.SearchTitleClass;
import org.techtown.sns_project.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchFragment extends Fragment {
    private View view;

    private String TAG = "프래그먼트";
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    ArrayList<SearchTitleClass> arraylist = new ArrayList<SearchTitleClass>();
    java.util.HashMap<String,Object> HashMap = new HashMap<String,Object>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.search_fragment, container, false);
/*

        // 인범 This is on and off 애니메이션
        RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) view.findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setSize(35);

        Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, " ON", "  &", "OFF ");
        rotatable.setSize(35);
        rotatable.setAnimationDuration(500);

        rotatingTextWrapper.setContent("This is ?", rotatable);
        // 여기까지
*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collectionGroup("brand").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    HashMap = (HashMap<String, Object>) documentSnapshot.getData();
                    String info = (String) HashMap.get("info");
                    String title = (String) HashMap.get("title");
                    String url = (String) HashMap.get("url");
                    SearchTitleClass stc = new SearchTitleClass(title, info, url);
                    arraylist.add(stc);
                    System.out.println("TEST STC : " + stc);
                }
                list = view.findViewById(R.id.listview);
                adapter = new ListViewAdapter(this.getContext(), arraylist);
                list.setAdapter(adapter);

                // Locate the EditText in listview_main.xml
                editsearch = view.findViewById(R.id.search);
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
        return view;
    }
}
