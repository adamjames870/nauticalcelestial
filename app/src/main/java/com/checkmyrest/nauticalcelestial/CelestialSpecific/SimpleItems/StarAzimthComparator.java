package com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialPosition;

import java.util.Comparator;

public class StarAzimthComparator implements Comparator<CelestialPosition> {

    @Override
    public int compare(CelestialPosition lhs, CelestialPosition rhs) {
        if (lhs.Azimuth().getTrueCourse() < rhs.Azimuth().getTrueCourse()) {
            return -1;
        } else if (lhs.Azimuth().getTrueCourse() > rhs.Azimuth().getTrueCourse()) {
            return 1;
        } else {
            return 0;
        }
    }

}
