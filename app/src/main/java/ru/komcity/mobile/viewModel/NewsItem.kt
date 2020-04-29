package ru.komcity.mobile.viewModel

import android.os.Parcel
import android.os.Parcelable

data class NewsItem(var title: String,
                    var date: String,
                    var shortText: String,
                    var previewImg: String,
                    var imageUrls: List<String>,
                    val newsId: Int): Parcelable, BaseHolderItem {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createStringArrayList() ?: emptyList(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(date)
        parcel.writeString(shortText)
        parcel.writeString(previewImg)
        parcel.writeStringList(imageUrls)
        parcel.writeInt(newsId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsItem> {
        override fun createFromParcel(parcel: Parcel): NewsItem {
            return NewsItem(parcel)
        }

        override fun newArray(size: Int): Array<NewsItem?> {
            return arrayOfNulls(size)
        }
    }
}