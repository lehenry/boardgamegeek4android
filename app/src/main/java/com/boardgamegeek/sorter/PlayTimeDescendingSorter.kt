package com.boardgamegeek.sorter

import android.content.Context
import androidx.annotation.StringRes

import com.boardgamegeek.R
import com.boardgamegeek.entities.CollectionItemEntity

class PlayTimeDescendingSorter(context: Context) : PlayTimeSorter(context) {
    @StringRes
    public override val subDescriptionResId = R.string.longest

    @StringRes
    public override val typeResId = R.string.collection_sort_type_play_time_desc

    override fun sort(items: Iterable<CollectionItemEntity>) = items.sortedByDescending { it.playingTime }
}
