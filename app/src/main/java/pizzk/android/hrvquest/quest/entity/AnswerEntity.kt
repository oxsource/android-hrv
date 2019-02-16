package pizzk.android.hrvquest.quest.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * 答题结果
 */
data class AnswerEntity(
    val objectId: String = "",
    val value: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(objectId)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AnswerEntity> {
        override fun createFromParcel(parcel: Parcel): AnswerEntity {
            return AnswerEntity(parcel)
        }

        override fun newArray(size: Int): Array<AnswerEntity?> {
            return arrayOfNulls(size)
        }
    }
}