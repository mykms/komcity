package ru.komcity.android.announcement;


public class loadElements {
    /*

public class fragment_filter extends Fragment
{
    private ArrayList<String> Category = new ArrayList<String>();
    private ArrayList<String> SubCategory = new ArrayList<String>();
    private int startposition = 0, id_Category = 0;
    private Spinner spin_typecategory;

    public fragment_filter() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Category = getArguments().getStringArrayList("category");
            SubCategory = getArguments().getStringArrayList("subcategory");
            startposition = getArguments().getInt("category_selected");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        // Получаем ссылку на раскрывающий список
        Spinner spin_category = (Spinner)view.findViewById(R.id.spinner_category);
        final Spinner spin_subcategory = (Spinner)view.findViewById(R.id.spinner_subcategory);
        spin_typecategory = (Spinner)view.findViewById(R.id.spinner_typecategory);

        ArrayAdapter<String> spinner1Adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, Category);
        spin_category.setAdapter(spinner1Adapter);
        spin_category.setPrompt("Title");// заголовок
        spin_category.setSelection(startposition);// выделяем элемент

        ArrayAdapter<String> spinner2Adapter = LoadToSpinner(view, startposition);
        spin_subcategory.setAdapter(spinner2Adapter);

        spin_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                ArrayAdapter<String> spAd = LoadToSpinner(view, position);
                spin_subcategory.setAdapter(spAd);
                id_Category = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                TypeCategory typeCategory = new TypeCategory();
                typeCategory.execute(id_Category, position, getActivity().getApplicationContext());
                //(Object)spin_typecategory
                // (Object)view.getContext()
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private ArrayAdapter<String> LoadToSpinner(View view, int id)
    {
        ArrayAdapter<String> adapter = null;
        ArrayList<String> sub = new ArrayList<String>();
        int last = -1;
        for (int i = 0; i < SubCategory.size(); i++)
        {
            String[] arr = SubCategory.get(i).split(", ", 4);
            int val = 0;
            try{
                val = Integer.valueOf(arr[1]);
                if (val == id)
                {
                    sub.add(arr[0]);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            last = val;
        }
        adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, sub);

        return adapter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            //case R.id.action_filter:
                //LoadResult();
                //Toast.makeText(getActivity().getApplicationContext(), "Не удалось получить содержимое\nПроверьте интернет соединение", Toast.LENGTH_SHORT).show();
                //return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void LoadResult()
    {
        return;
    }

    class TypeCategory extends AsyncTask<Object, Void, Void>
    {
        boolean showSpin = false;
        ArrayList<String> typeName = new ArrayList<String>();
        Context thisContext;
        ProgressBar progress;

        @Override
        protected Void doInBackground(Object... params)
        {
            String url = "http://www.komcity.ru/board/main/categoryclicklistener/?id1="+params[0]+"&id2="+params[1]+"&h=3";
            thisContext = (Context)params[2];
            //spin_typecategory = (Spinner)params[3];

            Document html = null;//Здесь хранится будет разобранный html документ
            try {
                //Считываем заглавную страницу
                html = Jsoup.connect(url).get();
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }

            //Если всё считалось, что вытаскиваем из считанного html документа table
            if (html != null)
            {
                Elements TextList = html.getElementsByTag("a");
                if (TextList.size() > 0) {
                    showSpin = true;
                    for (int i = 0; i < TextList.size(); i++) {
                        typeName.add(TextList.get(i).text().trim());
                    }
                }
                else {
                    // В данной рубрике объявления отсутствуют
                    showSpin = false;
                }
            }
            else {
                //Toast.makeText(thisContext, "Не удалось получить содержимое\nПроверьте интернет соединение", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (showSpin) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(thisContext, android.R.layout.simple_spinner_item, typeName);
                spin_typecategory.setAdapter(adapter);
                spin_typecategory.setSelection(0);
            }
            else {
                String[] q = new String[0];
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(thisContext, android.R.layout.simple_spinner_item, q);
                spin_typecategory.setAdapter(adapter);
            }
        }
    }
}

     */







    /*
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
    */
}
