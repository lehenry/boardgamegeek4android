package com.boardgamegeek.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.boardgamegeek.events.CollectionItemResetEvent;
import com.boardgamegeek.provider.BggContract.Collection;

import org.greenrobot.eventbus.EventBus;

public class ResetCollectionItemTask extends AsyncTask<Void, Void, Boolean> {
	private final Context context;
	private final long internalId;

	public ResetCollectionItemTask(Context context, long internalId) {
		this.context = context.getApplicationContext();
		this.internalId = internalId;
	}

	@NonNull
	@Override
	protected Boolean doInBackground(Void... params) {
		ContentValues values = new ContentValues(9);
		values.put(Collection.COLLECTION_DIRTY_TIMESTAMP, 0);
		values.put(Collection.STATUS_DIRTY_TIMESTAMP, 0);
		values.put(Collection.COMMENT_DIRTY_TIMESTAMP, 0);
		values.put(Collection.RATING_DIRTY_TIMESTAMP, 0);
		values.put(Collection.PRIVATE_INFO_DIRTY_TIMESTAMP, 0);
		values.put(Collection.WISHLIST_COMMENT_DIRTY_TIMESTAMP, 0);
		values.put(Collection.TRADE_CONDITION_DIRTY_TIMESTAMP, 0);
		values.put(Collection.WANT_PARTS_DIRTY_TIMESTAMP, 0);
		values.put(Collection.HAS_PARTS_DIRTY_TIMESTAMP, 0);

		ContentResolver resolver = context.getContentResolver();
		int rows = resolver.update(Collection.buildUri(internalId), values, null, null);
		return rows > 0;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			EventBus.getDefault().post(new CollectionItemResetEvent(internalId));
		}
	}
}
