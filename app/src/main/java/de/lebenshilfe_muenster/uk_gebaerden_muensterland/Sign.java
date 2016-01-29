package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

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
public class Sign {

    private final String name;
    private boolean starred;
    private int learningProgress;
    private String mnemonic;

    /**
     * Constructor for a sign ('Gebärde')
     *
     * @param name             the name, has to be unique within the app
     * @param mnemonic         the mnemonic ('Eselsbrücke')
     * @param starred          whether the user has starred this sign (added to his favorites)
     * @param learningProgress the learning progress for this sign. Must not be < -10 or > 10
     */
    public Sign(String name, String mnemonic, boolean starred, int learningProgress) {
        Validate.notNull(name, "Name must not be null");
        Validate.notBlank(name, "Name must not be empty.");
        Validate.notNull(mnemonic, "Mnemonic must not be null");
        Validate.notBlank(mnemonic, "Mnemonic must not be empty.");
        Validate.inclusiveBetween(-5, 5, learningProgress, "Learning progress cannot be < -5 or > 5");
        this.name = name;
        this.mnemonic = mnemonic;
        this.starred = starred;
        this.learningProgress = learningProgress;
    }

    public String getName() {
        return this.name;
    }

    public boolean isStarred() {
        return starred;
    }

    public int getLearningProgress() {
        return learningProgress;
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
                "name='" + name + '\'' +
                ", mnemonic='" + mnemonic + '\'' +
                ", starred=" + starred +
                ", learningProgress=" + learningProgress +
                '}';
    }
}
