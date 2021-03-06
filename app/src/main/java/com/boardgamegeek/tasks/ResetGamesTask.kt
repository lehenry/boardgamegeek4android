package com.boardgamegeek.tasks

import android.content.Context
import androidx.core.content.contentValuesOf
import com.boardgamegeek.provider.BggContract.Games
import com.boardgamegeek.service.SyncService
import timber.log.Timber

class ResetGamesTask(context: Context?) : ToastingAsyncTask(context) {
    override val successMessageResource = 0

    override val failureMessageResource = 0

    override fun doInBackground(vararg params: Void?): Boolean {
        context?.applicationContext?.let {
            val values = contentValuesOf(
                    Games.UPDATED_LIST to 0,
                    Games.UPDATED to 0,
                    Games.UPDATED_PLAYS to 0,
            )
            val rows = it.contentResolver?.update(Games.CONTENT_URI, values, null, null) ?: 0
            Timber.i("Reset the sync timestamps on %,d games.", rows)
            if (rows > 0) SyncService.sync(it, SyncService.FLAG_SYNC_GAMES)
            return rows > 0
        } ?: return false
    }
}