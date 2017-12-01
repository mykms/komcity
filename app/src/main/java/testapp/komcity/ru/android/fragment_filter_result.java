package testapp.komcity.ru.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.ListView;
import android.widget.ProgressBar;

public class fragment_filter_result extends Fragment {

    public fragment_filter_result()
    {
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_filter_result, container, false);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        View rootView = getView();// берем корневой элемент всего фрагмента
        if (rootView != null)
        {
            // Получаем наш список
            final ListView announcemenList = (ListView)rootView.findViewById(R.id.FilterResult);

            ProgressBar progressBar = (ProgressBar)rootView.findViewById(R.id.ProgressFilterResult);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            String url = "http://www.komcity.ru/board/main/getPubAdverts/?id=236&s=3&thisrubric=230&h=3";
        }
    }
}
