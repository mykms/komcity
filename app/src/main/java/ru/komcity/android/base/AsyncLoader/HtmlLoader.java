package ru.komcity.android.base.AsyncLoader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ru.komcity.android.base.Utils;
import ru.komcity.android.forum.ForumItem;
import ru.komcity.android.news.NewsItem;

public class HtmlLoader {
    private String rootAddress = "http://komcity.ru/";
    private String tr = "tr";
    private String td = "td";   // Тэг <td>
    private int textLen = 6;    // min-длина текста для распарсивания
    private IHtmlLoader iHtmlLoader = null;
    private IAsyncLoader iAsyncLoader = null;
    private Utils utils = new Utils();

    public HtmlLoader(IHtmlLoader mIHtmlLoader, IAsyncLoader mIAsyncLoader) {
        iHtmlLoader = mIHtmlLoader;
        iAsyncLoader = mIAsyncLoader;
    }

    public HtmlLoader(String page) {
        if (page == null)
            page = "";
        rootAddress = page.trim();
    }

    /**
     * @param mAddress Адрес типа "news/"
     * @return Возвращает html-документ типа Document
     */
    public void htmlAddressToParse(String mAddress) {
        AsyncLoader asyncLoader = new AsyncLoader(iAsyncLoader, rootAddress + mAddress);
        asyncLoader.execute();
    }

    /**
     * @param mHtmlDoc Загруженный html-документ
     */
    public void parseNews(Document mHtmlDoc) {
        List<Object> newsList = new ArrayList<>();
        //Если всё считалось, что вытаскиваем из считанного html документа table
        if (mHtmlDoc != null) {
            Elements rootTR = mHtmlDoc.getElementsByTag(tr);       // get all tags TD
            if (rootTR != null) {
                String date = null;
                String theme = null;
                String art = null;
                NewsItem item = new NewsItem();
                for (int i = 0; i < rootTR.size(); i++) {
                    Element row = rootTR.get(i);
                    if (row != null) {
                        // Найдем текст
                        Elements cols = row.select(td);
                        row = null;
                        if (cols.get(0) != null) {
                            String text = cols.get(0).text().trim();
                            cols = null;
                            // Не будем смотреть текст меньше определенной длины, т.к. мы там не сможем найти время в искомом формате
                            if (text != null) {

                                if (text.length() < textLen)
                                    continue;
                                // Ищем время публикации
                                try {
                                    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
                                    Date time = formatTime.parse(text.substring(0, 5));
                                    // поймали дату / время
                                    date = text.substring(6) + " " + text.substring(0, 5);
                                } catch (ParseException ex) {
                                    utils.getException(ex);
                                }

                                // Нашли заголовок
                                if (text.substring(0, 4).equalsIgnoreCase("тема")) {
                                    theme = text.substring(5, text.length());
                                    // После заголовка идет статья
                                    try {
                                        Element body = rootTR.get(i + 1).select("td").get(5);
                                        if (body.getElementsByTag("span") != null)
                                            body.getElementsByTag("span").remove();
                                        art = body.text().trim();

                                        if (art != null && theme != null && date != null) {
                                            item.setDate(date);
                                            item.setTitle(theme);
                                            item.setText(art);

                                            newsList.add(item);
                                            item = null;
                                            item = new NewsItem();
                                        }
                                    } catch (Exception ex) {
                                        utils.getException(ex);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (iHtmlLoader != null)
            iHtmlLoader.onReadyToShow(newsList);
    }

    public void parseForum(Document mHtmlDoc) {
        List<Object> forumList = new ArrayList<>();
        if (mHtmlDoc != null) {
            Elements rootTABLE = mHtmlDoc.getElementsByTag("table");       // get all tags Table
            boolean StartFlag = false;

            for (int i = 0; i < rootTABLE.size(); i++) {
                Element tr = rootTABLE.get(i);
                try {
                    if (tr.attr("width").equalsIgnoreCase("100%") && tr.attr("height").equalsIgnoreCase("90%") && tr.attr("border").equalsIgnoreCase("0") && tr.attr("cellpadding").equalsIgnoreCase("0") && tr.attr("cellspacing").equalsIgnoreCase("0")) {
                        Elements td = tr.select("td");
                        for (int j = 0; j < td.size(); j++) {
                            Element thisElement = td.get(j);
                            String thisTxt = thisElement.text().trim();
                            if (!thisTxt.isEmpty()) {   // читаем только НЕ пустые строки
                                if (StartFlag) {    // Если нашли точку входа
                                    //собираем
                                    try {
                                        String forumName = thisElement.select("a").get(0).text().trim();
                                        String _descr = thisTxt.substring(forumName.length()).split("Модератор:")[0].trim();    // Здесь должно быть описание
                                        String _count = td.get(j + 1).text().trim() + " / " + td.get(j + 2).text().trim();
                                        forumList.add(new ForumItem(forumName, _descr, _count));
                                        j += 4;// Переместим курсор на начало тем
                                    } catch (Exception e) {
                                        utils.getException(e);
                                        break;
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
                } catch (Exception e) {
                    utils.getException(e);
                }
            }
        }

        if (iHtmlLoader != null)
            iHtmlLoader.onReadyToShow(forumList);
    }

    public void parseAnnouncement(Document mHtmlDoc) {
        List<String> Title = new ArrayList<>();
        List<String> Links = new ArrayList<>();

        Elements TextList = mHtmlDoc.getElementsByAttributeValue("class", "dsgTextcolor");
        for (int i = 0; i < TextList.size(); i++)
        {
            String title = TextList.get(i).text().trim();
            Title.add(title);
        }
        Elements LinksList = mHtmlDoc.getElementsByAttribute("onclick");
        for (int i = 0; i < LinksList.size(); i++)
        {
            String value = LinksList.get(i).attributes().get("onclick").substring(14);
            Links.add(LinksList.get(i).text().trim() + ", " + value.substring(0, value.length()-1));
        }

        if (iHtmlLoader != null)
            iHtmlLoader.onReadyToShow(null);
    }
}
