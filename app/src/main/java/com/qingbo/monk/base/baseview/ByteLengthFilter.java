package com.qingbo.monk.base.baseview;

import android.text.InputFilter;
import android.text.Spanned;

public class ByteLengthFilter implements InputFilter {
    private final int mMaxBytes;

    public ByteLengthFilter(int maxBytes) {
        mMaxBytes = maxBytes;
    }

    public CharSequence filter(CharSequence source, int start, int end,Spanned dest, int dstart, int dend) {
        int srcByteCount = 0;
        // count UTF-8 bytes in source substring
        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            srcByteCount += (c < (char) 0x0080) ? 1 : 2;
        }
        int destLen = dest.length();
        int destByteCount = 0;
        // count UTF-8 bytes in destination excluding replaced section
        for (int i = 0; i < destLen; i++) {
            if (i < dstart || i >= dend) {
                char c = dest.charAt(i);
                destByteCount += (c < (char) 0x0080) ? 1 : 2;
            }
        }
        int keepBytes = mMaxBytes - destByteCount;
        if (keepBytes <= 0) {
            return "";
        } else if (keepBytes >= srcByteCount) {
            return null; // use original dest string
        } else {
            // find end position of largest sequence that fits in keepBytes
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                keepBytes -= (c < (char) 0x0080) ? 1 : 2;
                if (keepBytes < 0) {
                    return source.subSequence(start, i);
                }
            }
            // If the entire substring fits, we should have returned null
            // above, so this line should not be reached. If for some
            // reason it is, return null to use the original dest string.
            return null;
        }
    }
}
