package ru.komcity.mobile.Presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.komcity.mobile.View.ForumView
import org.jsoup.nodes.Document
import ru.komcity.mobile.Model.ForumItem

@InjectViewState
class ForumPresenter: MvpPresenter<ForumView>() {

    fun getForums(mHtmlDoc: Document?) {
        val forumList = arrayListOf<ForumItem>()
        if (mHtmlDoc != null) {
            val rootTABLE = mHtmlDoc.getElementsByTag("table")       // get all tags Table
            var StartFlag = false

            for (tr in rootTABLE) {
                try {
                    if (tr.attr("width") != null &&
                            tr.attr("width") == "100%" &&
                            tr.attr("height") != null &&
                            tr.attr("height") == "90%" &&
                            tr.attr("border") != null &&
                            tr.attr("border") == "0" &&
                            tr.attr("cellpadding") != null &&
                            tr.attr("cellpadding") == "0" &&
                            tr.attr("cellspacing") != null &&
                            tr.attr("cellspacing") == "0") {
                        val td = tr.select("td")
                        var j = 0
                        while (j < td.size) {
                            val thisElement = td.get(j)
                            val thisTxt = thisElement.text().trim()
                            if (!thisTxt.isEmpty()) {   // читаем только НЕ пустые строки
                                if (StartFlag) {    // Если нашли точку входа
                                    //собираем
                                    try {
                                        var forumName: String = ""
                                        var linkForum: String = ""
                                        if (thisElement.select("a").size > 0) {
                                            val elem_a = thisElement.select("a").get(0)
                                            linkForum = elem_a.attr("href").substring(1)   // Убираем первый символ (слэш)
                                            forumName = elem_a.text().trim()
                                        }
                                        var _descr: String? = null    // Здесь должно быть описание
                                        try {
                                            if (forumName.isNotEmpty())
                                                _descr = thisTxt.substring(forumName.length).split("Модератор:".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[0].trim({ it <= ' ' })
                                        } catch (ex: Exception) {
                                            //utils.getException(ex)
                                        }

                                        val _countReplic = td.get(j + 2).text().trim()
                                        val _countTheme = td.get(j + 1).text().trim()
                                        if (forumName.isNotEmpty() && _descr != null)
                                            forumList.add(ForumItem(forumName, _descr, _countReplic, _countTheme, linkForum))
                                        j += 4// Переместим курсор на начало тем
                                    } catch (e: Exception) {
                                        //utils.getException(e)
                                        break
                                    }

                                } else {
                                    if (thisTxt.equals("тем", true) && td.get(j + 1).text().equals("реплик", true)) {
                                        // нашли вход
                                        StartFlag = true
                                        j += 3// Переместим курсор на начало тем
                                    }
                                }
                            }
                            j++
                        }
                        break
                    }
                } catch (e: Exception) {
                    //utils.getException(e)
                }
            }
        }
    }

    fun parseSubForum(mHtmlDoc: Document?) {
//        val subForumList = ArrayList()
//        if (mHtmlDoc != null) {
//            val rootTable = mHtmlDoc!!.getElementsByTag("table")
//            var endpointForum = false
//            if (rootTable.size() > 7) {
//                val elem_td = rootTable.get(7).getElementsByTag(td)
//                var i = 0
//                while (i < elem_td.size()) {
//                    val stylePadding = "padding-left: 8; padding-right: 8; padding-bottom: 3; padding-top: 3"
//                    val elem_x = elem_td.get(i).getElementsByAttributeValue("style", stylePadding)
//                    if (elem_x.size() > 0) {
//                        if (endpointForum) {
//                            var title: String? = null
//                            var link: String? = null
//                            var count: String? = null
//                            var lastDateMessage: String? = null
//                            try {
//                                title = elem_x.first().getElementsByTag("b").first().text()
//                                link = elem_x.first().getElementsByTag("a").first().attr("href")
//                                count = elem_td.get(i + 1).getElementsByAttributeValue("style", stylePadding).text()
//                                val elem_nobr = elem_td.get(i + 6).getElementsByAttributeValue("style", stylePadding)
//                                if (elem_nobr.size() > 0)
//                                    if (elem_nobr.first().getElementsByTag("nobr").size() > 0) {
//                                        lastDateMessage = elem_nobr.first().getElementsByTag("nobr")
//                                                .first().text()
//                                    }
//                            } catch (ex: Exception) {
//                                utils.getException(ex)
//                            }
//
//                            if (title != null && link != null) {
//                                if (title.equals("создать новую тему", ignoreCase = true))
//                                    break
//                                subForumList.add(ForumItem(title, lastDateMessage,
//                                        count!!.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].trim { it <= ' ' },
//                                        count.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' },
//                                        link))
//                            }
//                            i += 7
//                        } else {
//                            // Будем искать точку входа
//                            val elem_t = elem_x.get(0).getElementsByTag("table")
//                            if (elem_t.size() > 0) {
//                                if (elem_t.first() != null) {
//                                    val tema = elem_t.first().text().substring(0, 4)
//                                    val elemtTD = elem_t.first().getElementsByTag(td)
//                                    if (elemtTD.size() === 2 && elemtTD.text() != null)
//                                        if (elemtTD.first().text().equals("тема")) {
//                                            // нашли точку входа
//                                            endpointForum = true
//                                            i += 6
//                                        }
//                                }
//                            }
//                        }
//                    }
//                    i++
//                }
//            }
//        }
    }

    fun parseForumDetail(mHtmlDoc: Document) {
//        val forumDetailList = ArrayList()
//        if (mHtmlDoc != null) {
//            var endpointTD = false
//            val rootTable = mHtmlDoc!!.getElementsByAttributeValue("height", "89%")
//            val allTR = rootTable.first().getElementsByTag(tr)
//            var i = 0
//            while (i < allTR.size()) {
//                if (endpointTD) {
//                    var date: String? = null
//                    var name: String? = null
//                    var text: String? = null
//                    try {
//                        date = allTR.get(i).getElementsByTag(td).first().text().trim().replace("\u00A0", "")
//                        if (date!!.isEmpty()) {
//                            i++
//                            continue
//                        }
//                        try {
//                            if (allTR.get(i + 2).getElementsByTag(td).size() >= 5) {
//                                name = allTR.get(i + 2).getElementsByTag(td).get(2).getElementsByTag("a").text().trim()
//                                text = allTR.get(i + 2).getElementsByTag(td).get(5).text().trim()
//                            }
//                        } catch (ex: Exception) {
//                            try {
//                                if (allTR.get(i + 1).getElementsByTag(td).size() >= 5) {
//                                    name = allTR.get(i + 1).getElementsByTag(td).get(2).getElementsByTag("a").text().trim()
//                                    text = allTR.get(i + 1).getElementsByTag(td).get(5).text().trim()
//                                }
//                            } catch (e: Exception) {
//                                utils.getException(e)
//                            }
//
//                        }
//
//                        i += allTR.get(i + 1).getElementsByTag(tr).size() + allTR.get(i + 2).getElementsByTag(tr).size()
//                    } catch (ex: Exception) {
//                        utils.getException(ex)
//                    }
//
//                    if (date != null && name != null && text != null) {
//                        if (!date.isEmpty() && !name.isEmpty() && !text.isEmpty()) {
//                            forumDetailList.add(ForumDetailItem(date, name, text))
//                        }
//                    }
//                } else {
//                    if (allTR.get(i).getElementsByTag(td).size() === 3) {
//                        try {
//                            val elem_ep = allTR.get(i).getElementsByAttributeValue("width", "64%")
//                            if (elem_ep.size() > 0) {
//                                if (elem_ep.first().text() != null) {
//                                    if (elem_ep.first().text().contains("создать новую тему") || elem_ep.first().text().contains("написать сообщение")) {
//                                        endpointTD = true// Нашли точку входа
//                                    }
//                                }
//                            }
//                        } catch (ex: Exception) {
//                            utils.getException(ex)
//                        }
//
//                    }
//                }
//                i++
//            }
//        }
    }
}