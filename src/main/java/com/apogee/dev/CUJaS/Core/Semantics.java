package com.apogee.dev.CUJaS.Core;

import com.apogee.dev.CUJaS.Core.NTK.NTKKey;

import java.util.HashMap;


/**
 * Interface générique pour les éléments sémantiques.
 * @see NTKKey
 * @author PRV
 * @version 1.0
 */
public interface Semantics {
    HashMap<XKey, String> keywords = new HashMap<>();
}