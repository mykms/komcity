package ru.komcity.mobile.common

import android.text.style.TtsSpan.TextBuilder
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.TextNode
import ru.komcity.mobile.viewModel.ForumItem
import ru.komcity.mobile.viewModel.NewsItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HtmlLoader() {
    private val domen = "komcity.ru"
    private var rootAddress = "http://$domen/"
    private val rootNewsAddress = "news/"
    private val tr = "tr"
    private val td = "td" // Тэг <td>
    private val textLen = 6 // min-длина текста для распарсивания

    //    private IHtmlLoader iHtmlLoader = null;
    //    private IAsyncLoader iAsyncLoader = null;
    //    private Utils utils = new Utils();
    private val announcementLinksList: List<String> = ArrayList()

    //    public HtmlLoader(IHtmlLoader mIHtmlLoader, IAsyncLoader mIAsyncLoader) {
    //        iHtmlLoader = mIHtmlLoader;
    //        iAsyncLoader = mIAsyncLoader;
    //    }
    init {
        var page = ""
        if (page == null) page = ""
        rootAddress = page.trim { it <= ' ' }
    }

    /**
     * @param mAddress Адрес типа "news/"
     * @return Возвращает html-документ типа Document
     */
    fun htmlAddressToParse(mAddress: String?) {
        if (mAddress != null) {
            if (!mAddress.isEmpty()) {
                try {
                    //Считываем заглавную страницу
                    val html = Jsoup.connect(rootAddress + mAddress).get()
                } catch (ex: Exception) {
                    //utils.getException(ex);
                }
            }
        }
    }

    /**
     * @param mHtmlDoc Загруженный html-документ
     */
    fun parseNews(mHtmlDoc: Document?): List<NewsItem> {
        val newsList: MutableList<NewsItem> = ArrayList()
        //Если всё считалось, что вытаскиваем из считанного html документа table
        if (mHtmlDoc != null) {
            val newsSection = mHtmlDoc.body().getElementsByTag("section")
            if (newsSection.isNotEmpty()) {
                newsSection[0].getElementsByAttributeValue("class", "single-news-item").forEach { newsElement ->
                    val newsItem = newsElement.getElementsByAttributeValue("class", "news-content").first()
                    val title = newsItem?.getElementsByTag("h3")?.first()?.text() ?: ""
                    val link = newsItem?.getElementsByTag("a")?.first()?.attr("href") ?: ""
                    val text = newsItem?.getElementsByTag("p")?.first()?.text() ?: ""
                    val category = newsItem?.getElementsByTag("p")?.last()?.firstElementChild()?.text() ?: ""
                    val date = (newsItem?.getElementsByTag("p")?.last()?.lastChild() as? TextNode)?.text() ?: ""

                    val imgItem = newsElement.getElementsByAttributeValue("class", "news-image")
                    val imgPreview = imgItem.first()?.getElementsByTag("img")?.first()?.attr("src") ?: ""

                    newsList.add(NewsItem(title, date, text, imgPreview, link, category))
                }
            }
        }
        return newsList
    }

    fun parseNewsDetails(mHtmlDoc: Document): NewsItem {
        val contentElement = mHtmlDoc.body().getElementsByAttributeValue("class", "single-post")
        val rows = contentElement.first()?.getElementsByTag("p")
        var content = ""
        rows?.forEach {
            content += it.text() + "\n"
        }
        return NewsItem("", "", content, "", "", "")
    }

    fun parseNewsImageLinks(mHtmlDoc: Document?) {
        val imageLinkList: MutableList<Any> = ArrayList()
        if (mHtmlDoc != null) {
            val rootA = mHtmlDoc.getElementsByTag("a")
            if (rootA != null) {
                val elemA = rootA
                    .attr("class", "photoset-grid-lightbox")
                    .attr("rel", "group")
                for (i in elemA.indices) {
                    val element = elemA[i]
                    if (element.attr("href") != null &&
                        element.attr("href").contains(domen) &&
                        element.attr("href").contains(rootNewsAddress)
                    ) {
                        val elementIMG = element.getElementsByTag("img").first()
                        if (elementIMG != null) {
                            imageLinkList.add(elementIMG.attr("src"))
                        }
                    }
                }
            }
        }
        //        if (iHtmlLoader != null)
//            iHtmlLoader.onReadyToShow(imageLinkList);
    }

    fun parseForum(mHtmlDoc: Document?) {
        val forumList: MutableList<Any> = ArrayList()
        if (mHtmlDoc != null) {
            val rootTABLE = mHtmlDoc.getElementsByTag("table") // get all tags Table
            var StartFlag = false
            for (i in rootTABLE.indices) {
                val tr = rootTABLE[i]
                try {
                    if (tr.attr("width") != null &&
                        tr.attr("width")
                            .equals("100%", ignoreCase = true) && tr.attr("height") != null &&
                        tr.attr("height")
                            .equals("90%", ignoreCase = true) && tr.attr("border") != null &&
                        tr.attr("border")
                            .equals("0", ignoreCase = true) && tr.attr("cellpadding") != null &&
                        tr.attr("cellpadding")
                            .equals("0", ignoreCase = true) && tr.attr("cellspacing") != null &&
                        tr.attr("cellspacing").equals("0", ignoreCase = true)
                    ) {
                        val td = tr.select("td")
                        var j = 0
                        while (j < td.size) {
                            val thisElement = td[j]
                            val thisTxt = thisElement.text().trim { it <= ' ' }
                            if (!thisTxt.isEmpty()) {   // читаем только НЕ пустые строки
                                if (StartFlag) {    // Если нашли точку входа
                                    //собираем
                                    try {
                                        var forumName: String? = null
                                        var linkForum: String? = null
                                        if (thisElement.select("a").size > 0) {
                                            val elem_a = thisElement.select("a")[0]
                                            linkForum = elem_a.attr("href")
                                                .substring(1) // Убираем первый символ (слэш)
                                            forumName = elem_a.text().trim { it <= ' ' }
                                        }
                                        var _descr: String? = null // Здесь должно быть описание
                                        try {
                                            if (forumName != null) _descr =
                                                thisTxt.substring(forumName.length)
                                                    .split("Модератор:".toRegex())
                                                    .dropLastWhile { it.isEmpty() }
                                                    .toTypedArray()[0].trim { it <= ' ' }
                                        } catch (ex: Exception) {
                                            //utils.getException(ex);
                                        }
                                        val _countReplic = td[j + 2].text().trim { it <= ' ' }
                                        val _countTheme = td[j + 1].text().trim { it <= ' ' }
                                        if (forumName != null && _descr != null) forumList.add(
                                            ForumItem(
                                                forumName,
                                                _descr,
                                                _countReplic,
                                                _countTheme,
                                                linkForum!!
                                            )
                                        )
                                        j += 4 // Переместим курсор на начало тем
                                    } catch (e: Exception) {
                                        //utils.getException(e);
                                        break
                                    }
                                } else {
                                    if (thisTxt.equals("тем", ignoreCase = true) &&
                                        td[j + 1].text().equals("реплик", ignoreCase = true)
                                    ) {
                                        // нашли вход
                                        StartFlag = true
                                        j += 3 // Переместим курсор на начало тем
                                    }
                                }
                            }
                            j++
                        }
                        break
                    }
                } catch (e: Exception) {
                    //utils.getException(e);
                }
            }
        }

//        if (iHtmlLoader != null)
//            iHtmlLoader.onReadyToShow(forumList);
    }

    fun parseSubForum(mHtmlDoc: Document?) {
        val subForumList: MutableList<Any> = ArrayList()
        if (mHtmlDoc != null) {
            val rootTable = mHtmlDoc.getElementsByTag("table")
            var endpointForum = false
            if (rootTable.size > 7) {
                val elem_td = rootTable[7].getElementsByTag(td)
                var i = 0
                while (i < elem_td.size) {
                    val stylePadding =
                        "padding-left: 8; padding-right: 8; padding-bottom: 3; padding-top: 3"
                    val elem_x = elem_td[i].getElementsByAttributeValue("style", stylePadding)
                    if (elem_x.size > 0) {
                        if (endpointForum) {
                            var title: String? = null
                            var link: String? = null
                            var count: String? = null
                            var lastDateMessage: String? = null
                            try {
                                title = elem_x.first().getElementsByTag("b").first().text()
                                link = elem_x.first().getElementsByTag("a").first().attr("href")
                                count = elem_td[i + 1].getElementsByAttributeValue(
                                    "style",
                                    stylePadding
                                ).text()
                                val elem_nobr = elem_td[i + 6].getElementsByAttributeValue(
                                    "style",
                                    stylePadding
                                )
                                if (elem_nobr.size > 0) if (elem_nobr.first()
                                        .getElementsByTag("nobr").size > 0
                                ) {
                                    lastDateMessage = elem_nobr.first().getElementsByTag("nobr")
                                        .first().text()
                                }
                            } catch (ex: Exception) {
                                //utils.getException(ex);
                            }
                            if (title != null && link != null) {
                                if (title.equals("создать новую тему", ignoreCase = true)) break
                                subForumList.add(ForumItem(title, lastDateMessage!!,
                                    count!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()[0].trim { it <= ' ' },
                                    count.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                                        .toTypedArray()[1].trim { it <= ' ' },
                                    link
                                )
                                )
                            }
                            i += 7
                        } else {
                            // Будем искать точку входа
                            val elem_t = elem_x[0].getElementsByTag("table")
                            if (elem_t.size > 0) {
                                if (elem_t.first() != null) {
                                    val tema = elem_t.first().text().substring(0, 4)
                                    val elemtTD = elem_t.first().getElementsByTag(td)
                                    if (elemtTD.size == 2 && elemtTD.text() != null) if (elemtTD.first()
                                            .text() == "тема"
                                    ) {
                                        // нашли точку входа
                                        endpointForum = true
                                        i += 6
                                    }
                                }
                            }
                        }
                    }
                    i++
                }
            }
        }

//        if (iHtmlLoader != null)
//            iHtmlLoader.onReadyToShow(subForumList);
    }

    fun parseForumDetail(mHtmlDoc: Document?) {
        val forumDetailList: MutableList<Any> = ArrayList()
        if (mHtmlDoc != null) {
            var endpointTD = false
            val rootTable = mHtmlDoc.getElementsByAttributeValue("height", "89%")
            val allTR = rootTable.first().getElementsByTag(tr)
            var i = 0
            while (i < allTR.size) {
                if (endpointTD) {
                    var date: String? = null
                    var name: String? = null
                    var text: String? = null
                    try {
                        date = allTR[i].getElementsByTag(td).first().text().trim { it <= ' ' }
                            .replace("\u00A0", "")
                        if (date.isEmpty()) {
                            i++
                            continue
                        }
                        try {
                            if (allTR[i + 2].getElementsByTag(td).size >= 5) {
                                name = allTR[i + 2].getElementsByTag(td)[2].getElementsByTag("a")
                                    .text().trim { it <= ' ' }
                                text =
                                    allTR[i + 2].getElementsByTag(td)[5].text().trim { it <= ' ' }
                            }
                        } catch (ex: Exception) {
                            try {
                                if (allTR[i + 1].getElementsByTag(td).size >= 5) {
                                    name =
                                        allTR[i + 1].getElementsByTag(td)[2].getElementsByTag("a")
                                            .text().trim { it <= ' ' }
                                    text = allTR[i + 1].getElementsByTag(td)[5].text()
                                        .trim { it <= ' ' }
                                }
                            } catch (e: Exception) {
                                //utils.getException(e);
                            }
                        }
                        i += allTR[i + 1].getElementsByTag(tr).size +
                                allTR[i + 2].getElementsByTag(tr).size
                    } catch (ex: Exception) {
                        //utils.getException(ex);
                    }
                    if (date != null && name != null && text != null) {
                        if (!date.isEmpty() && !name.isEmpty() && !text.isEmpty()) {
                            //forumDetailList.add(ForumDetailItem(date, name, text))
                        }
                    }
                } else {
                    if (allTR[i].getElementsByTag(td).size == 3) {
                        try {
                            val elem_ep = allTR[i].getElementsByAttributeValue("width", "64%")
                            if (elem_ep.size > 0) {
                                if (elem_ep.first().text() != null) {
                                    if (elem_ep.first().text().contains("создать новую тему") ||
                                        elem_ep.first().text().contains("написать сообщение")
                                    ) {
                                        endpointTD = true // Нашли точку входа
                                    }
                                }
                            }
                        } catch (ex: Exception) {
                            //utils.getException(ex);
                        }
                    }
                }
                i++
            }
        }

//        if (iHtmlLoader != null)
//            iHtmlLoader.onReadyToShow(forumDetailList);
    }

    fun parseAnnouncement(mHtmlDoc: Document?) {
        val titleArr: Array<String?>
        val linksTitleArr: Array<String?>
        val linksArr: Array<String?>
        if (mHtmlDoc != null) {
            val TextList = mHtmlDoc.getElementsByAttributeValue("class", "dsgTextcolor")
            titleArr = arrayOfNulls(TextList.size)
            for (i in TextList.indices) {
                var title: String? = null
                try {
                    title = TextList[i].text().trim { it <= ' ' }
                } catch (ex: Exception) {
                    //utils.getException(ex);
                }
                if (title != null) titleArr[i] = title
            }
            val LinksList = mHtmlDoc.getElementsByAttribute("onclick")
            linksTitleArr = arrayOfNulls(LinksList.size)
            linksArr = arrayOfNulls(LinksList.size)
            for (i in LinksList.indices) {
                var value: String? = null
                try {
                    value = LinksList[i].attributes()["onclick"].substring(14)
                } catch (ex: Exception) {
                    //utils.getException(ex);
                }
                if (value != null) {
                    try {
                        linksTitleArr[i] = LinksList[i].text().trim { it <= ' ' }
                        linksArr[i] = value.substring(0, value.length - 1)
                    } catch (ex: Exception) {
                        //utils.getException(ex);
                    }
                }
            }
            //            AnnouncementData data = new AnnouncementData(titleArr, linksTitleArr, linksArr);
//            if (iHtmlLoader != null)
//                ((IAnnoncementHtmlLoader)iHtmlLoader).onReadyToShowCategoryAndTypes(data.getModelList());
        }
    }

    fun parseAnnouncementSubCategory(mHtmlDoc: Document?) {
        val subCategoryList: MutableList<Any> = ArrayList()
        if (mHtmlDoc != null) {
            //Если всё считалось, что вытаскиваем из считанного html документа table
            var is_a = false
            var is_all_a = false
            val elem_span = mHtmlDoc.getElementsByTag("span")
//            var links_id: MutableList<AnnouncementSubCategoryItemModel?>? = null
//            var spanText: String? = null
//            for (i in elem_span.indices) {
//                if (!is_a || is_all_a) links_id = ArrayList<AnnouncementSubCategoryItemModel?>()
//                val elem_a = elem_span[i].getElementsByTag("a")
//                if (elem_a.size > 0) {
//                    val itemModel = AnnouncementSubCategoryItemModel(
//                        elem_a.attr("id"),
//                        elem_a.text().replace("\u2022", "").trim { it <= ' ' })
//                    if (i > 0 && !is_all_a) links_id!!.add(itemModel) else {
//                        // Если в самый первый раз попался тэг <a>, то скорее всего все пункты будут с тэгом <a>
//                        if (i == 0 || is_all_a) {
//                            links_id!!.add(itemModel)
//                            subCategoryList.add(AnnouncementSubCategoryModel(null, links_id))
//                            is_all_a = true
//                        }
//                    }
//                    is_a = true
//                    if (i == elem_span.size - 1 && !is_all_a) subCategoryList.add(
//                        AnnouncementSubCategoryModel(spanText, links_id)
//                    )
//                } else {
//                    if (is_a) {
//                        subCategoryList.add(AnnouncementSubCategoryModel(spanText, links_id))
//                    }
//                    spanText = elem_span[i].text().replace("\u2022", "").trim { it <= ' ' }
//                    is_a = false
//                }
//            }
        }

//        if (iHtmlLoader != null)
//            ((IAnnoncementHtmlLoader)iHtmlLoader).onReadyToShowSubCategory(subCategoryList);
    }
}
