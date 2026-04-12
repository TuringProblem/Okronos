package net.runelite.client.plugins.toa.features.timetracking;

import net.runelite.client.plugins.toa.util.RaidRoom;

public enum SplitsMode
{

	OFF,
	ROOM,
	PATH,
	;

	boolean includesRoom(RaidRoom r)
	{
		if (this == OFF || r.getRoomType() == RaidRoom.RaidRoomType.LOBBY)
		{
			return false;
		}

		return this == ROOM ||
			r.getRoomType() == RaidRoom.RaidRoomType.BOSS;
	}

	String nextSplit(RaidRoom r)
	{
		if (this == OFF || r == null || r.getRoomType() == RaidRoom.RaidRoomType.LOBBY)
		{
			return null;
		}

		if (this == ROOM)
		{
			return r.toString();
		}

		switch (r)
		{
			case CRONDIS:
			case ZEBAK:
				return RaidRoom.ZEBAK.toString();

			case SCABARAS:
			case KEPHRI:
				return RaidRoom.KEPHRI.toString();

			case APMEKEN:
			case BABA:
				return RaidRoom.BABA.toString();

			case HET:
			case AKKHA:
				return RaidRoom.AKKHA.toString();

			case WARDENS:
				return RaidRoom.WARDENS.toString();
		}

		return null;
	}

}
