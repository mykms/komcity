package testapp.komcity.ru.android;

import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class fragment_announcement extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_announcement, container, false);
        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        View rootView = getView();// берем корневой элемент всего фрагмента
        if (rootView != null)
        {
            // Получаем наш список
            final ListView announcemenList = (ListView)rootView.findViewById(R.id.announcementList);

            ProgressBar progressBar = (ProgressBar)rootView.findViewById(R.id.ProgressAnnouncement);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            AnnouncementListTask _announcementTask = new AnnouncementListTask();
            _announcementTask.execute(announcemenList, getActivity().getApplicationContext(), progressBar);

            return;
        }

    }

    class AnnouncementListTask extends AsyncTask<Object, Void, Void>
    {
        ListView _list;
        //ArrayList<Forum> forums = new ArrayList<Forum>();
        ArrayList<String> Title = new ArrayList<String>();
        ArrayList<String> Links = new ArrayList<String>();
        Context thisContext;
        ProgressBar progress;

        @Override
        protected Void doInBackground(Object... params)
        {
            _list = (ListView)params[0];
            thisContext = (Context)params[1];
            progress = (ProgressBar)params[2];

            Document html = null;//Здесь хранится будет разобранный html документ
            try {
                //Считываем заглавную страницу
                html = Jsoup.connect("http://www.komcity.ru/board/main/GetVolumes/?h=null").get();
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }

            //Если всё считалось, что вытаскиваем из считанного html документа table
            if (html != null)
            {
                Elements TextList = html.getElementsByAttributeValue("class", "dsgTextcolor");
                for (int i = 0; i < TextList.size(); i++)
                {
                    String title = TextList.get(i).text().trim();
                    Title.add(title);
                }
                Elements LinksList = html.getElementsByAttribute("onclick");
                for (int i = 0; i < LinksList.size(); i++)
                {
                    String value = LinksList.get(i).attributes().get("onclick").substring(14);
                    Links.add(LinksList.get(i).text().trim() + ", " + value.substring(0, value.length()-1));
                }
            }
            else {
                Toast.makeText(thisContext, "Не удалось получить содержимое\nПроверьте интернет соединение", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(thisContext, android.R.layout.simple_list_item_1, Title);

            _list.setAdapter(adapter);
            progress.setVisibility(ProgressBar.INVISIBLE);

            _list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Fragment fragment = new fragment_filter();
                    // Передаем параметры в Фрагмент-фильтр
                    Bundle params = new Bundle();
                    params.putStringArrayList("category", Title);
                    params.putStringArrayList("subcategory", Links);
                    params.putInt("category_selected", position);
                    fragment.setArguments(params);

                    FragmentManager fragmentManager = getFragmentManager();
                    //fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                }
            });
        }
    }
}
