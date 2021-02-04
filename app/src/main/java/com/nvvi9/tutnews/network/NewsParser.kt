package com.nvvi9.tutnews.network

import android.util.Xml
import com.nvvi9.tutnews.data.NewsCategory
import com.nvvi9.tutnews.data.NewsInfo
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.xmlpull.v1.XmlPullParser
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class for parsing latest news from tut.by
 */

class NewsParser {

    /**
     * @param newsCategoryRoute category of news
     * @return Single containing all recent news in category
     */
    fun parseNews(newsCategoryRoute: String): Single<List<NewsInfo>> =
        Single.just("https://news.tut.by/rss/$newsCategoryRoute.rss")
            .subscribeOn(Schedulers.io())
            .map { URL(it).openStream() }
            .map {
                it.use {
                    val parser = Xml.newPullParser().apply {
                        setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                        setInput(it, null)
                        nextTag()
                    }

                    val newsItems = mutableListOf<NewsInfo>()

                    try {
                        while (parser.next() != XmlPullParser.END_DOCUMENT) {
                            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "item") {
                                readNewsItem(parser)?.let { newsInfo ->
                                    newsItems.add(newsInfo)
                                }
                            }
                        }

                        newsItems
                    } catch (t: Throwable) {
                        newsItems
                    }
                }
            }

    private fun readNewsItem(parser: XmlPullParser): NewsInfo? {
        var title: String? = null
        var link: String? = null
        var description: String? = null
        var thumbnailUri: String? = null
        val authors = mutableListOf<String>()
        var publicationTime: Date? = null
        var newsCategory: NewsCategory? = null

        while (parser.next() != XmlPullParser.END_TAG || parser.name != "item") {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "title" -> title = readText(parser)
                "link" -> link = readText(parser)
                "description" -> description = readDescription(parser)
                "enclosure" -> thumbnailUri = readThumbnailUri(parser)
                "category" -> newsCategory = readCategory(parser)
                "pubDate" -> publicationTime = readPublicationTime(parser)
                "guid" -> link = readText(parser)
                "atom:name" -> authors.addAll(readAuthors(parser))
            }
        }

        return if (title != null && link != null && description != null && thumbnailUri != null && publicationTime != null && newsCategory != null) {
            NewsInfo(
                title = title,
                link = link,
                description = description,
                thumbnailUri = thumbnailUri,
                authors = authors,
                publicationTime = publicationTime,
                newsCategory = newsCategory
            )
        } else {
            null
        }
    }

    private fun readAuthors(parser: XmlPullParser) =
        readText(parser)
            ?.split(", ")
            ?.filter { !it.toLowerCase(Locale.ROOT).contains("tut") }
            ?: emptyList()

    private fun readThumbnailUri(parser: XmlPullParser) =
        parser.getAttributeValue(null, "url")
            ?.also { parser.nextTag() }

    private fun readDescription(parser: XmlPullParser) =
        readText(parser)?.substringAfter("/>")?.substringBefore("<br")

    private fun readCategory(parser: XmlPullParser) =
        readText(parser)
            ?.let { NewsCategory.getByCategoryName(it) }

    private fun readPublicationTime(parser: XmlPullParser) =
        readText(parser)?.let {
            SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.US).parse(it)
        }

    private fun readText(parser: XmlPullParser) =
        parser
            .takeIf { it.next() == XmlPullParser.TEXT }
            ?.let { parser.text }
            ?.also { parser.nextTag() }
}