package com.boardgamegeek.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import android.text.TextUtils;

import com.boardgamegeek.R;
import com.boardgamegeek.provider.BggContract;
import com.boardgamegeek.util.DateTimeUtils;
import com.boardgamegeek.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@Root(name = "item")
public class GeekListItem {
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

	private long mPostDateTime = DateTimeUtils.UNPARSED_DATE;
	private long mEditDateTime = DateTimeUtils.UNPARSED_DATE;

	@Attribute private String id;

	@Attribute private String imageid;

	public int imageId() {
		return Integer.valueOf(imageid);
	}

	@Attribute private String objectid;

	public int getObjectId() {
		return StringUtils.parseInt(objectid, BggContract.INVALID_ID);
	}

	@Attribute private String objectname;

	public String getObjectName() {
		return objectname;
	}

	@Attribute private String objecttype;

	public int getObjectTypeId() {
		if ("thing".equals(objecttype)) {
			if ("boardgame".equals(subtype)) {
				return R.string.title_board_game;
			} else if ("boardgameaccessory".equals(subtype)) {
				return R.string.title_board_game_accessory;
			}
			return R.string.title_thing;
		} else if ("company".equals(objecttype)) {
			if ("boardgamepublisher".equals(subtype)) {
				return R.string.title_board_game_publisher;
			}
			return R.string.title_company;
		} else if ("person".equals(objecttype)) {
			if ("boardgamedesigner".equals(subtype)) {
				return R.string.title_board_game_designer;
			}
			return R.string.title_person;
		} else if ("family".equals(objecttype)) {
			return R.string.title_family;
			// subtype="boardgamefamily"
		} else if ("filepage".equals(objecttype)) {
			return R.string.title_file;
		} else if ("geeklist".equals(objecttype)) {
			return R.string.title_geeklist;
		}
		return 0;
	}

	public boolean isBoardGame() {
		return "thing".equals(objecttype);
	}

	public String getObejctUrl() {
		return "http://www.boardgamegeek.com/" + (TextUtils.isEmpty(subtype) ? objecttype : subtype) + "/" + objectid;
	}

	@Attribute private String postdate;

	public long getPostDate() {
		mPostDateTime = DateTimeUtils.tryParseDate(mPostDateTime, postdate, FORMAT);
		return mPostDateTime;
	}

	@Attribute private String editdate;

	public long getEditDate() {
		mEditDateTime = DateTimeUtils.tryParseDate(mEditDateTime, editdate, FORMAT);
		return mEditDateTime;
	}

	@Attribute private String subtype;

	@Attribute private String thumbs;

	public int getThumbCount() {
		return StringUtils.parseInt(thumbs, 0);
	}

	@Attribute public String username;

	@Element(required = false) public String body;

	@ElementList(name = "comment", inline = true, required = false) private List<GeekListComment> comments;
}