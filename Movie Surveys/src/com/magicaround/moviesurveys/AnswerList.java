package com.magicaround.moviesurveys;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class AnswerList extends ArrayList<Answer> implements Parcelable {
	/**
	 * 
	 */
	public AnswerList() {
		super();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(this.size());
		for (int i = 0; i < this.size(); i++) {
			out.writeInt(this.get(i).id);
			out.writeInt(this.get(i).q_id);
			out.writeInt(this.get(i).state);
			out.writeString(this.get(i).text);
			out.writeInt(this.get(i).year);
			out.writeString(this.get(i).picture);
			out.writeString(this.get(i).info);
			out.writeInt(this.get(i).votes);
			out.writeDouble(this.get(i).rating);
			out.writeInt(this.get(i).lastPosition);
			out.writeInt(this.get(i).preLastPosition);
			out.writeInt(this.get(i).votesLocal);
			out.writeDouble(this.get(i).ratingLocal);
		}
	}

	public static final Parcelable.Creator<AnswerList> CREATOR = new Parcelable.Creator<AnswerList>() {
		@Override
		public AnswerList createFromParcel(Parcel in) {
			return new AnswerList(in);
		}

		@Override
		public AnswerList[] newArray(int size) {
			return new AnswerList[size];
		}
	};

	private AnswerList(Parcel in) {
		// TODO Auto-generated method stub
	}

}
