package ru.komcity.mobile.Presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import ru.komcity.mobile.Model.NewsItem
import ru.komcity.mobile.View.NewsListView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@InjectViewState
class NewsPresenter: MvpPresenter<NewsListView>() {
    //private val jobs = arrayListOf<Job>()
    private val job: Job = Job()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val domen = "komcity.ru"
    private val rootAddress = "http://$domen/"
    private val rootNewsAddress = "news/"

    private suspend fun getHtml(url: String): Document =
            coroutineScope {
                async {
                    try {
                        Jsoup.connect(url).get()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                        Document("")
                    }
                }
            }.await()

    fun getNews() {
        scope.launch {
            val docWait = getHtml(rootAddress + rootNewsAddress)
            val items = parseNews(docWait)
            withContext(Dispatchers.Main) {
                viewState.onNewsLoaded(items)
            }
        }
    }

    /**
     * @param mHtmlDoc Загруженный html-документ
     */
    private fun parseNews(mHtmlDoc: Document?): List<NewsItem> {
        val tr = "tr"
        val td = "td"
        val textLen = 6
        val newsList = arrayListOf<NewsItem>()
        //Если всё считалось, что вытаскиваем из считанного html документа table
        if (mHtmlDoc != null) {
            val rootTR: Elements = mHtmlDoc.getElementsByTag(tr)
            var date: String = ""
            var title: String = ""
            var art: String = ""
            var imgUrl = ""
            for ((index, row) in rootTR.withIndex()) {
                if (row != null) {
                    // Найдем текст
                    val cols = row.select(td)
                    if (cols.first() != null) {
                        val text: String? = cols.first().text().trim()
                        // Не будем смотреть текст меньше определенной длины, т.к. мы там не сможем найти время в искомом формате
                        if (text != null) {
                            if (text.length < textLen)
                                continue
                            // Ищем время публикации
                            try {
                                val formatTime = SimpleDateFormat("HH:mm dd MMMM yyyy", Locale.getDefault())
                                val time = formatTime.parse(text)
                                date = text// поймали дату / время
                            } catch (ex: ParseException) {
                                ex.printStackTrace()
                            }

                            // Нашли заголовок
                            if (text.substring(0, 4).equals("тема", true)) {
                                title = text.substring(5, text.length)
                                // После заголовка идет статья
                                try {
                                    val body = rootTR[index + 1].select("td")[5]
                                    val link = body.attr("onclick") // Ссылка на полную новость
                                    if (body.getElementsByTag("span") != null)
                                        body.getElementsByTag("span").remove()
                                    // Ищем рисунок
                                    val div_elems = body.getElementsByTag("div")
                                    val photodiv = body.getElementById("photodiv")
                                    if (photodiv != null && div_elems != null) {
                                        try {
                                            // Уточнение элемента
                                            val div_elem = div_elems.first()
                                            if (photodiv.equals(div_elem)) {
                                                val img_elems = div_elem.getElementsByTag("img")
                                                if (img_elems != null && img_elems.first().attr("src") != null) {
                                                    imgUrl = img_elems.first().attr("src")  // Добавим ссылку на рисунок
                                                }
                                            }
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                        }

                                    }
                                    // Удалим в конце ненужное слово
                                    val b_tag = body.getElementsByTag("b")
                                    for (bItem in b_tag) {
                                        if (bItem.text().equals("обсудить", true)) {
                                            body.getElementsByTag("b").remove()
                                        }
                                        if (bItem.text().toLowerCase().startsWith("посмотреть обсуждение")) {
                                            body.getElementsByTag("b").remove()
                                        }
                                    }
                                    art = body.text().trim()

                                    if (art.isNotEmpty() && title.isNotEmpty()) {
                                        if (date.isNotEmpty())
                                            date = Calendar.getInstance().time.toString()

                                        newsList.add(NewsItem(title, date, art, imgUrl, rootNewsAddress + link.replace("\'", "").replace("window.location=", "")))
                                    }
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }
                        }
                    }
                }
            }
        }
        return newsList
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}