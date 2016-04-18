package de.lebenshilfe_muenster.uk_gebaerden_muensterland;

import de.lebenshilfe_muenster.uk_gebaerden_muensterland.database.Sign;

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
public class TestConstants {
    public static final String MAMA = "Mama";
    public static final String MAMA_MNEMONIC = "Wange kreisend streicheln";
    public static final String PAPA = "Papa";
    public static final String FOOTBALL = "Fußball Spielen";
    public static final String PROGRESS_0 = "0";
    public static final String ENTER = "\n";
    public static final String MAM = "mam";
    public static final String PAP = "paP";
    public static final Sign FOOTBALL_SIGN = new Sign.Builder().setId(0).setName("football").setNameLocaleDe("Fußball spielen")
            .setMnemonic("Faust tritt in Handfläche").setStarred(false).setLearningProgress(0).create();
    public static final Sign MAMA_SIGN = new Sign.Builder().setId(0).setName("mama").setNameLocaleDe("Mama")
            .setMnemonic("Wange kreisend streicheln").setStarred(false).setLearningProgress(0).create();
    public static final Sign PAPA_SIGN = new Sign.Builder().setId(0).setName("papa").setNameLocaleDe("Papa")
            .setMnemonic("Schnauzbart andeuten").setStarred(false).setLearningProgress(0).create();
    public static final Sign TEST_SIGN = new Sign.Builder().setId(0).setName("test_sign").setNameLocaleDe("test_sign_de")
            .setMnemonic("test_sign_mnemonic").setStarred(false).setLearningProgress(0).create();
    public static final int INITIAL_NUMBER_OF_SIGNS = 210;
}
