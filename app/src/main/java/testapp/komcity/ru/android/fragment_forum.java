package testapp.komcity.ru.android;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class fragment_forum extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_forum, container, false);

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
            ListView forumList = (ListView)rootView.findViewById(R.id.forumList);

            ProgressBar progressBar = (ProgressBar)rootView.findViewById(R.id.ProgressForum);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            ForumListTask _forumTask = new ForumListTask();
            _forumTask.execute(forumList, getActivity().getApplicationContext(), progressBar);
        }
    }

    class ForumListTask extends AsyncTask<Object, Void, Void>
    {
        ListView _list;
        ArrayList<Forum> forums = new ArrayList<Forum>();
        String date = "", theme = "", art = "";
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
                html = Jsoup.connect("http://komcity.ru/forum/").get();
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }

            //Если всё считалось, что вытаскиваем из считанного html документа table
            if (html != null)
            {
                Elements rootTABLE = html.getElementsByTag("table");       // get all tags Table
                boolean StartFlag = false;

                for (int i = 0; i < rootTABLE.size(); i++)
                {
                    Element tr = rootTABLE.get(i);
                    try {
                        if (tr.attr("width").equalsIgnoreCase("100%") && tr.attr("height").equalsIgnoreCase("90%") && tr.attr("border").equalsIgnoreCase("0") && tr.attr("cellpadding").equalsIgnoreCase("0") && tr.attr("cellspacing").equalsIgnoreCase("0"))
                        {
                            Elements td = tr.select("td");
                            for (int j = 0; j < td.size(); j++)
                            {
                                Element thisElement = td.get(j);
                                String thisTxt = thisElement.text().trim();
                                if (!thisTxt.isEmpty()) {   // читаем только НЕ пустые строки
                                    if (StartFlag) {    // Если нашли точку входа
                                        //собираем
                                        try {
                                            String forumName = thisElement.select("a").get(0).text().trim();
                                            String _descr = thisTxt.substring(forumName.length()).split("Модератор:")[0].trim();    // Здесь должно быть описание
                                            String _count = td.get(j + 1).text().trim() + " / " + td.get(j + 2).text().trim();
                                            forums.add(new Forum(forumName, _descr, _count));
                                            j += 4;// Переместим курсор на начало тем
                                        }
                                        catch(Exception e)
                                        {
                                            break;
                                            //e.printStackTrace();
                                        }
                                    } else {
                                        if (thisTxt.equalsIgnoreCase("тем") && td.get(j + 1).text().equalsIgnoreCase("реплик")) {
                                            // нашли вход
                                            StartFlag = true;
                                            j += 3;// Переместим курсор на начало тем
                                        }
                                    }
                                }
                            }
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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

            ForumAdapter boxAdapter = new ForumAdapter(thisContext, forums);// Заполняем адаптер ListView
            _list.setAdapter(boxAdapter);// настраиваем список
            progress.setVisibility(ProgressBar.INVISIBLE);
        }

        public String getShortDate(String largeDate)
        {
            String[] Month =  new String[]{"января", "февраля", "марта", "апреля", "мая", "июня",
                    "июля", "августа", "сентября", "октября", "ноября", "декабря"};
            String[] fulldate = new String[0];
            try
            {
                fulldate = largeDate.split(" ", 3);
                if (fulldate.length < 3) return "";
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return "";
            }

            for (int i = 1; i <= Month.length; i++) {
                if (fulldate[1].equalsIgnoreCase(Month[i-1]))
                {
                    if (i < 10)
                        return fulldate[0] + ".0" + i + "." + fulldate[2];
                    else return fulldate[0] + "." + i + "." + fulldate[2];
                }
            }
            return "";
        }

        public class Forum
        {
            private String Theme;
            private String Description;
            private String CountThemeReplic;

            public Forum(String _theme, String _discr, String _count)
            {
                Theme = _theme;
                Description = _discr;
                CountThemeReplic = _count;
            }

            public String getTheme()
            {
                return Theme;
            }

            public String getDescription()
            {
                return Description;
            }

            public String getCountThemeReplic()
            {
                return CountThemeReplic;
            }
        }

        public class ForumAdapter extends BaseAdapter
        {
            Context context;
            LayoutInflater lInflater;
            ArrayList<Forum> objects;

            ForumAdapter(Context _context, ArrayList<Forum> products)
            {
                context = _context;
                objects = products;
                lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            // кол-во элементов
            @Override
            public int getCount() {
                return objects.size();
            }

            // элемент по позиции
            @Override
            public Object getItem(int position) {
                return objects.get(position);
            }

            // id по позиции
            @Override
            public long getItemId(int position) {
                return position;
            }

            // пункт списка
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {

                // используем созданные, но не используемые view
                View view = convertView;

                if (view == null) {
                    view = lInflater.inflate(R.layout.listview_item, parent, false);
                }
                Forum f = (Forum)getItem(position);

                // заполняем View в пункте списка данными
                ((TextView) view.findViewById(R.id.newsTitle)).setText(f.getTheme());
                ((TextView) view.findViewById(R.id.newsDate)).setText(f.getCountThemeReplic());
                ((TextView) view.findViewById(R.id.newsFullText)).setText(f.getDescription());

                return view;
            }
        }
    }
}
