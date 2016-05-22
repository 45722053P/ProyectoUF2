package com.proyecto.projectmap;

import android.location.Location;

/**
 * Created by alex on 22/05/2016.
 */

class CambiaLocation {

    Location location;

        public CambiaLocation(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

}
