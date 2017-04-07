package testapp.komcity.ru.myapplicationetalon;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class fragment_news extends Fragment
{
    private ArrayAdapter<String> dataList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
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
            ListView NewsList = (ListView)rootView.findViewById(R.id.newsList);

            ProgressBar progressBar = (ProgressBar)rootView.findViewById(R.id.ProgressNews);
            progressBar.setVisibility(ProgressBar.VISIBLE);

            NewsListTask _newsTask = new NewsListTask();
            _newsTask.execute(NewsList, getActivity().getApplicationContext(), progressBar);
        }
    }

    class NewsListTask extends AsyncTask<Object, Void, Void>
    {
        ListView _list;
        ArrayList<Article> articles = new ArrayList<Article>();
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
                html = Jsoup.connect("http://komcity.ru/news/").get();
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }

            //Если всё считалось, что вытаскиваем из считанного html документа table
            if (html != null)
            {
                //int size = html.getElementsByTag("table").size();   // Total
                Elements rootTR = html.getElementsByTag("tr");       // get all tags TD
                for (int i = 0; i < rootTR.size(); i++)
                {
                    Element row = rootTR.get(i);
                    Elements cols = row.select("td");

                    String text = cols.get(0).text().trim();
                    if (text.length() < 6) continue;
                    try
                    {
                        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
                        Date time = formatTime.parse(text.substring(0, 5));
                        // поймали дату / время
                        date = getShortDate(text.substring(6)) + " " + text.substring(0, 5);
                        //dataNews[0][count] = date;
                    } catch (ParseException e) {
                        e.printStackTrace();   }
                    // Нашли заголовок
                    if(text.length() > 4) {
                        if (text.substring(0, 4).equalsIgnoreCase("тема"))
                        {
                            theme = text.substring(5, text.length());
                            //dataNews[1][count] = theme;
                            try
                            {
                                Element body = rootTR.get(i+1).select("td").get(5);
                                if (body.getElementsByTag("span") != null)
                                    body.getElementsByTag("span").remove();
                                art = body.text().trim();

                                articles.add(new Article(date, theme, art));    // Заполняем адаптер
                            }
                            catch (Exception e) { e.printStackTrace(); }
                        }
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

            NewsAdapter boxAdapter = new NewsAdapter(thisContext, articles);// Заполняем адаптер ListView
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

        public class Article
        {
            private String date;
            private String title;
            private String text;

            public Article(String _date, String _title, String _text)
            {
                date = _date;
                title = _title;
                text = _text;
            }

            public String getDate()
            {
                return date;
            }

            public String getTitle()
            {
                return title;
            }

            public String getText()
            {
                return text;
            }
        }

        public class NewsAdapter extends BaseAdapter
        {
            Context context;
            LayoutInflater lInflater;
            ArrayList<Article> objects;

            NewsAdapter(Context _context, ArrayList<Article> products)
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
                Article a = (Article)getItem(position);

                // заполняем View в пункте списка данными
                ((TextView) view.findViewById(R.id.newsTitle)).setText(a.getTitle());
                ((TextView) view.findViewById(R.id.newsDate)).setText(a.getDate());
                ((TextView) view.findViewById(R.id.newsFullText)).setText(a.getText());

                return view;
            }
        }
    }
}
