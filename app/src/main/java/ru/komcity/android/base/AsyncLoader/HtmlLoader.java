package ru.komcity.android.base.AsyncLoader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.komcity.android.base.Utils;
import ru.komcity.android.forum.ForumDetailItem;
import ru.komcity.android.forum.ForumItem;
import ru.komcity.android.news.NewsItem;

public class HtmlLoader {
    private String domen = "komcity.ru";
    private String rootAddress = "http://" + domen + "/";
    private String rootNewsAddress = "news/";
    private String tr = "tr";
    private String td = "td";   // Тэг <td>
    private int textLen = 6;    // min-длина текста для распарсивания
    private IHtmlLoader iHtmlLoader = null;
    private IAsyncLoader iAsyncLoader = null;
    private Utils utils = new Utils();
    private List<String > announcementLinksList = new ArrayList<>();

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
        if (mAddress != null) {
            if (!mAddress.isEmpty()) {
                AsyncLoader asyncLoader = new AsyncLoader(iAsyncLoader, rootAddress + mAddress);
                asyncLoader.execute();
            }
        }
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
                        if (cols.first() != null) {
                            String text = cols.first().text().trim();
                            // Не будем смотреть текст меньше определенной длины, т.к. мы там не сможем найти время в искомом формате
                            if (text != null) {
                                if (text.length() < textLen)
                                    continue;
                                // Ищем время публикации
                                try {
                                    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm dd MMMM yyyy", Locale.getDefault());
                                    Date time = formatTime.parse(text);
                                    date = text;// поймали дату / время
                                } catch (ParseException ex) {
                                    utils.getException(ex);
                                }

                                // Нашли заголовок
                                if (text.substring(0, 4).equalsIgnoreCase("тема")) {
                                    theme = text.substring(5, text.length());
                                    // После заголовка идет статья
                                    try {
                                        Element body = rootTR.get(i + 1).select("td").get(5);
                                        String link = body.attr("onclick"); // Ссылка на полную новость
                                        if (body.getElementsByTag("span") != null)
                                            body.getElementsByTag("span").remove();
                                        // Ищем рисунок
                                        Elements div_elems = body.getElementsByTag("div");
                                        Element photodiv = body.getElementById("photodiv");
                                        if (photodiv != null && div_elems != null) {
                                            try {
                                                // Уточнение элемента
                                                Element div_elem = div_elems.first();
                                                if (photodiv.equals(div_elem)) {
                                                    Elements img_elems = div_elem.getElementsByTag("img");
                                                    if (    img_elems != null &&
                                                            img_elems.first().attr("src") != null) {
                                                        item.setImage(img_elems.first().attr("src"));  // Добавим ссылку на рисунок
                                                    }
                                                }
                                            } catch (Exception ex) {
                                                utils.getException(ex);
                                            }
                                        }
                                        art = body.text().trim();

                                        if (art != null && theme != null && date != null) {
                                            item.setDate(date);
                                            item.setTitle(theme);
                                            item.setText(art);
                                            item.setUrl(rootNewsAddress + link.replace("\'", "").replace("window.location=", ""));

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

    public void parseNewsImageLinks(Document mHtmlDoc) {
        List<Object> imageLinkList = new ArrayList<>();
        if (mHtmlDoc != null) {
            Elements rootA = mHtmlDoc.getElementsByTag("a");
            if (rootA != null) {
                Elements elemA = rootA
                                    .attr("class", "photoset-grid-lightbox")
                                    .attr("rel", "group");
                for (int i = 0; i < elemA.size(); i++) {
                    Element element = elemA.get(i);
                    if (    element.attr("href") != null &&
                            element.attr("href").contains(domen) &&
                            element.attr("href").contains(rootNewsAddress)) {
                        Element elementIMG = element.getElementsByTag("img").first();
                        if (elementIMG != null) {
                            imageLinkList.add(elementIMG.attr("src"));
                        }
                    }
                }
            }
        }
        if (iHtmlLoader != null)
            iHtmlLoader.onReadyToShow(imageLinkList);
    }

    public void parseForum(Document mHtmlDoc) {
        List<Object> forumList = new ArrayList<>();
        if (mHtmlDoc != null) {
            Elements rootTABLE = mHtmlDoc.getElementsByTag("table");       // get all tags Table
            boolean StartFlag = false;

            for (int i = 0; i < rootTABLE.size(); i++) {
                Element tr = rootTABLE.get(i);
                try {
                    if (    tr.attr("width") != null &&
                            tr.attr("width").equalsIgnoreCase("100%") &&
                            tr.attr("height") != null &&
                            tr.attr("height").equalsIgnoreCase("90%") &&
                            tr.attr("border") != null &&
                            tr.attr("border").equalsIgnoreCase("0") &&
                            tr.attr("cellpadding") != null &&
                            tr.attr("cellpadding").equalsIgnoreCase("0") &&
                            tr.attr("cellspacing") != null &&
                            tr.attr("cellspacing").equalsIgnoreCase("0")) {
                        Elements td = tr.select("td");
                        for (int j = 0; j < td.size(); j++) {
                            Element thisElement = td.get(j);
                            String thisTxt = thisElement.text().trim();
                            if (!thisTxt.isEmpty()) {   // читаем только НЕ пустые строки
                                if (StartFlag) {    // Если нашли точку входа
                                    //собираем
                                    try {
                                        String forumName = null;
                                        String linkForum = null;
                                        if (thisElement.select("a").size() > 0) {
                                            Element elem_a = thisElement.select("a").get(0);
                                            linkForum = elem_a.attr("href").substring(1);   // Убираем первый символ (слэш)
                                            forumName = elem_a.text().trim();
                                        }
                                        String _descr = null;    // Здесь должно быть описание
                                        try {
                                            if (forumName != null)
                                                _descr = thisTxt.substring(forumName.length()).split("Модератор:")[0].trim();
                                        } catch (Exception ex) {
                                            utils.getException(ex);
                                        }
                                        String _countReplic = td.get(j + 2).text().trim();
                                        String _countTheme = td.get(j + 1).text().trim();
                                        if (forumName != null && _descr != null)
                                            forumList.add(new ForumItem(forumName, _descr, _countReplic, _countTheme, linkForum));
                                        j += 4;// Переместим курсор на начало тем
                                    } catch (Exception e) {
                                        utils.getException(e);
                                        break;
                                    }
                                } else {
                                    if (    thisTxt.equalsIgnoreCase("тем") &&
                                            td.get(j + 1).text().equalsIgnoreCase("реплик")) {
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

    public void parseSubForum(Document mHtmlDoc) {
        List<Object> subForumList = new ArrayList<>();
        if (mHtmlDoc != null) {
            Elements rootTable = mHtmlDoc.getElementsByTag("table");
            boolean endpointForum = false;
            if (rootTable.size() > 7) {
                Elements elem_td = rootTable.get(7).getElementsByTag(td);
                for (int i = 0; i < elem_td.size(); i++) {
                    String stylePadding = "padding-left: 8; padding-right: 8; padding-bottom: 3; padding-top: 3";
                    Elements elem_x = elem_td.get(i).getElementsByAttributeValue("style", stylePadding);
                    if (elem_x.size() > 0) {
                        if (endpointForum) {
                            String title = null;
                            String link = null;
                            String count = null;
                            String lastDateMessage = null;
                            try {
                                title = elem_x.first().getElementsByTag("b").first().text();
                                link = elem_x.first().getElementsByTag("a").first().attr("href");
                                count = elem_td.get(i + 1).getElementsByAttributeValue("style", stylePadding).text();
                                lastDateMessage = elem_td.get(i + 6)
                                        .getElementsByAttributeValue("style", stylePadding)
                                        .first()
                                        .getElementsByTag("nobr")
                                        .first().text();
                            } catch (Exception ex) {
                                utils.getException(ex);
                            }
                            if (title != null && link != null) {
                                if (title.equalsIgnoreCase("создать новую тему"))
                                    break;
                                subForumList.add(new ForumItem(title, lastDateMessage,
                                                                count.split("/")[0].trim(),
                                                                count.split("/")[1].trim(),
                                                                link));
                            }
                            i += 7;
                        } else {
                            // Будем искать точку входа
                            Elements elem_t = elem_x.get(0).getElementsByTag("table");
                            if (elem_t.size() > 0) {
                                if (elem_t.first() != null) {
                                    String tema = elem_t.first().text().substring(0, 4);
                                    Elements elemtTD = elem_t.first().getElementsByTag(td);
                                    if (elemtTD.size() == 2 && elemtTD.text() != null)
                                        if (elemtTD.first().text().equals("тема")) {
                                            // нашли точку входа
                                            endpointForum = true;
                                            i += 6;
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (iHtmlLoader != null)
            iHtmlLoader.onReadyToShow(subForumList);
    }

    public void parseForumDetail(Document mHtmlDoc) {
        List<Object> forumDetailList = new ArrayList<>();
        if (mHtmlDoc != null) {
            boolean endpointTD = false;
            Elements rootTable = mHtmlDoc.getElementsByAttributeValue("height", "89%");
            Elements allTR = rootTable.first().getElementsByTag(tr);
            for (int i = 0; i < allTR.size(); i++) {
                if (endpointTD) {
                    String date = null;
                    String name = null;
                    String text = null;
                    try {
                        date = allTR.get(i).getElementsByTag(td).first().text().trim();
                        name = allTR.get(i + 2).getElementsByTag(td).get(2).getElementsByTag("a").text().trim();
                        text = allTR.get(i + 2).getElementsByTag(td).get(5).text().trim();
                        i += allTR.get(i + 1).getElementsByTag(tr).size() +
                                allTR.get(i + 2).getElementsByTag(tr).size();
                    } catch (Exception ex) {
                        utils.getException(ex);
                    }
                    if (date != null && name != null && text != null) {
                        forumDetailList.add(new ForumDetailItem(date, name, text));
                    }
                } else {
                    if (allTR.get(i).getElementsByTag(td).size() == 3) {
                        try {
                            if (allTR.get(i).getElementsByAttributeValue("width", "64%").first().text().contains("создать новую тему")) {
                                // Нашли точку входа
                                endpointTD = true;
                            }
                        } catch (Exception ex) {
                            utils.getException(ex);
                        }
                    }
                }
            }
        }

        if (iHtmlLoader != null)
            iHtmlLoader.onReadyToShow(forumDetailList);
    }

    public void parseAnnouncement(Document mHtmlDoc) {
        List<Object> Title = new ArrayList<>();
        List<String> Links = new ArrayList<>();

        Elements TextList = mHtmlDoc.getElementsByAttributeValue("class", "dsgTextcolor");
        for (int i = 0; i < TextList.size(); i++) {
            String title = null;
            try {
                title = TextList.get(i).text().trim();
            } catch (Exception ex) {
                utils.getException(ex);
            }
            if (title != null)
                Title.add(title);
        }
        Elements LinksList = mHtmlDoc.getElementsByAttribute("onclick");
        for (int i = 0; i < LinksList.size(); i++) {
            String value = null;
            try {
                value = LinksList.get(i).attributes().get("onclick").substring(14);
            } catch (Exception ex) {
                utils.getException(ex);
            }
            if (value != null) {
                try {
                    value = LinksList.get(i).text().trim() + ", " + value.substring(0, value.length() - 1);
                    Links.add(value);
                } catch (Exception ex) {
                    utils.getException(ex);
                }
            }
        }

        if (iHtmlLoader != null)
            iHtmlLoader.onReadyToShow(Title);

        announcementLinksList = Links;
    }

    public List<String> getAnnouncementItemsForLinks() {
        return announcementLinksList;
    }

    public List<String> getAnnouncementTypeByID(int id) {
        List<String> sub = new ArrayList<String>();
        int last = -1;
        if (announcementLinksList != null) {
            for (int i = 0; i < announcementLinksList.size(); i++) {
                String[] arr = announcementLinksList.get(i).split(", ", 4);
                int val = 0;
                try {
                    val = Integer.valueOf(arr[1]);
                    if (val == id) {
                        sub.add(arr[0]);
                    }
                } catch (Exception e) {
                    utils.getException(e);
                }
            }
        }

        return sub;
    }
}
