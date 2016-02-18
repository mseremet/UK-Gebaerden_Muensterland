package de.lebenshilfe_muenster.uk_gebaerden_muensterland.database;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.Validate;

/**
 * Copyright (c) 2016 Matthias Tonhäuser
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Sign implements Parcelable {

    /**
     * Parcelable Creator.
     */
    public static final Parcelable.Creator<Sign> CREATOR = new Parcelable.Creator<Sign>() {
        public Sign createFromParcel(Parcel in) {
            return new Sign(in);
        }

        public Sign[] newArray(int size) {
            return new Sign[size];
        }
    };

    private static final int LEARNING_PROGRESS_LOWER_BOUNDARY = -5;
    private static final int LEARNING_PROGRESS_UPPER_BOUNDARY = 5;
    private final int id;
    private final String name;
    private final String mnemonic;
    private final String nameLocaleDe;
    private int learningProgress;
    private boolean starred;

    /**
     * Constructor for a sign ('Gebärde'), which has been persisted to the database. Used by the Builder.
     *
     * @param id               the database id
     * @param name             the name, has to be unique within the app
     * @param nameLocaleDe     the German name
     * @param mnemonic         the mnemonic ('Eselsbrücke')
     * @param starred          whether the user has starred this sign (added to his favorites)
     * @param learningProgress the learning progress for this sign. Must not be < -5 or > 5
     */
    private Sign(int id, String name, String nameLocaleDe, String mnemonic, boolean starred, int learningProgress) {
        validateParameters(name, mnemonic, learningProgress);
        this.id = id;
        this.name = name;
        this.nameLocaleDe = nameLocaleDe;
        this.mnemonic = mnemonic;
        this.starred = starred;
        this.learningProgress = learningProgress;
    }

    /**
     * Constructor for a sign which has been parcelled.
     *
     * @param in an Android parcel.
     */
    private Sign(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.nameLocaleDe = in.readString();
        this.mnemonic = in.readString();
        this.starred = (boolean) in.readValue(null);
        this.learningProgress = in.readInt();
    }

    private void validateParameters(String name, String mnemonic, int learningProgress) {
        Validate.notNull(name, "Name must not be null");
        Validate.notBlank(name, "Name must not be empty.");
        Validate.notNull(mnemonic, "Mnemonic must not be null");
        Validate.notBlank(mnemonic, "Mnemonic must not be empty.");
        Validate.inclusiveBetween(LEARNING_PROGRESS_LOWER_BOUNDARY, LEARNING_PROGRESS_UPPER_BOUNDARY, learningProgress, "Learning progress cannot be < -5 or > 5");
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getNameLocaleDe() {
        return this.nameLocaleDe;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public int getLearningProgress() {
        return learningProgress;
    }

    public void increaseLearningProgress() {
        if (this.learningProgress < LEARNING_PROGRESS_UPPER_BOUNDARY) {
            this.learningProgress++;
        }
    }

    public void decreaseLearningProgress() {
        if (this.learningProgress > LEARNING_PROGRESS_LOWER_BOUNDARY) {
            this.learningProgress--;
        }
    }

    public String getMnemonic() {
        return mnemonic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sign sign = (Sign) o;
        return name.equals(sign.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Sign{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameLocaleDe='" + nameLocaleDe + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                ", starred=" + starred +
                ", learningProgress=" + learningProgress +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeString(this.name);
        out.writeString(this.nameLocaleDe);
        out.writeString(this.mnemonic);
        out.writeValue(this.starred);
        out.writeInt(this.learningProgress);
    }

    public static class Builder {
        private int id = 0;
        private String name;
        private String nameLocaleDe;
        private String mnemonic;
        private boolean starred = false;
        private int learningProgress = 0;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setNameLocaleDe(String nameLocaleDe) {
            this.nameLocaleDe = nameLocaleDe;
            return this;
        }

        public Builder setMnemonic(String mnemonic) {
            this.mnemonic = mnemonic;
            return this;
        }

        public Builder setStarred(boolean starred) {
            this.starred = starred;
            return this;
        }

        public Builder setLearningProgress(int learningProgress) {
            this.learningProgress = learningProgress;
            return this;
        }

        public Sign create() {
            return new Sign(id, name, nameLocaleDe, mnemonic, starred, learningProgress);
        }

    }

}
