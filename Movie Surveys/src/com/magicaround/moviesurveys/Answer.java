package com.magicaround.moviesurveys;

import android.os.Parcel;
import android.os.Parcelable;

public class Answer implements Parcelable {
	public int id;
	public int q_id;
	public int state;
	public String text;
	public int year;
	public String picture;
	public String info;
	public int votes;
	public double rating;
	public int lastPosition;
	public int preLastPosition;
	public int votesLocal;
	public double ratingLocal;
	public int tmp1;
	public String tmp2;

	public Answer() {
		id = 0;
		q_id = 0;
		state = 0;
		text = "";
		year = 0;
		picture = "";
		info = "";
		votes = 0;
		rating = 0;
		votesLocal = 0;
		ratingLocal = 0;
		lastPosition = 0;
		preLastPosition = 0;
		tmp1 = 0;
		tmp2 = "";

	}

	public Answer(int id) {
		this();
		this.id = id;
	}

	@Override
	public String toString() {
		return "id:" + String.valueOf(id) + "; q_id:" + String.valueOf(q_id)
				+ "; question:" + text + "; picture:" + picture + "; info"
				+ info;
	}

	public static final Parcelable.Creator<Answer> CREATOR = new Parcelable.Creator<Answer>() {
		@Override
		public Answer createFromParcel(Parcel in) {
			return new Answer(in);
		}

		@Override
		public Answer[] newArray(int size) {
			return new Answer[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeInt(q_id);
		out.writeInt(state);
		out.writeString(text);
		out.writeInt(year);
		out.writeString(picture);
		out.writeString(info);
		out.writeInt(votes);
		out.writeDouble(rating);
		out.writeInt(lastPosition);
		out.writeInt(preLastPosition);
		out.writeInt(votesLocal);
		out.writeDouble(ratingLocal);
	}

	private Answer(Parcel in) {
		id = in.readInt();
		q_id = in.readInt();
		state = in.readInt();
		text = in.readString();
		year = in.readInt();
		picture = in.readString();
		info = in.readString();
		votes = in.readInt();
		rating = in.readDouble();
		lastPosition = in.readInt();
		preLastPosition = in.readInt();
		votesLocal = in.readInt();
		ratingLocal = in.readDouble();
	}

}
