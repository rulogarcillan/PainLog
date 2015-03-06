package com.pain.log.painlog.negocio;

import java.util.Comparator;

/**
 * Created by Rulo on 06/03/2015.
 */
    public class Ordena implements Comparator<Logs> {

    @Override
    public int compare(Logs lhs, Logs rhs) {
        return rhs.getDate().compareTo(lhs.getDate());
    }
}
