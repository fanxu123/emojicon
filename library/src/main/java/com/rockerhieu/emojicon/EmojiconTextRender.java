package com.rockerhieu.emojicon;

import android.content.Context;
import android.text.Spannable;

/**
 * Emojicon text render to spannable
 * Created by mantou on 15/9/7.
 */
public class EmojiconTextRender {

    private Context mContext;

    public EmojiconTextRender context(Context context) {
        mContext = context;
        return this;
    }

    private int mEmojiSize = -1;

    public EmojiconTextRender emojiSize(int emojiSize) {
        mEmojiSize = emojiSize;
        return this;
    }

    private int mEmojiAlignment = -1;

    public EmojiconTextRender emojiAlignment(int emojiAlignment) {
        mEmojiAlignment = emojiAlignment;
        return this;
    }

    private int mTextSize = -1;

    public EmojiconTextRender textSize(int textSize) {
        mTextSize = textSize;
        return this;
    }

    private boolean mUseSystemDefault = false;

    public EmojiconTextRender useSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
        return this;
    }

    public EmojiconTextRender setup() {
        if (mContext == null) {
            throw new IllegalStateException("context == null");
        }
        if (mEmojiSize < 0) {
            throw new IllegalStateException("EmojiSize is not specified.");
        }
        if (mEmojiAlignment < 0) {
            throw new IllegalStateException("EmojiAlignment is not specified.");
        }
        if (mTextSize < 0) {
            throw new IllegalStateException("TextSize is not specified.");
        }
        return this;
    }

    private void removeAllEmojiconSpan(Spannable text) {
        // remove spans throughout all text
        EmojiconSpan[] oldSpans = text.getSpans(0, text.length(), EmojiconSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            text.removeSpan(oldSpans[i]);
        }
    }

    private int findIconResourceWhenFollowUnicodeIs20e3ByUnicode(int unicode) {
        int icon = 0;
        switch (unicode) {
            case 0x0031:
                icon = R.drawable.emoji_0031;
                break;
            case 0x0032:
                icon = R.drawable.emoji_0032;
                break;
            case 0x0033:
                icon = R.drawable.emoji_0033;
                break;
            case 0x0034:
                icon = R.drawable.emoji_0034;
                break;
            case 0x0035:
                icon = R.drawable.emoji_0035;
                break;
            case 0x0036:
                icon = R.drawable.emoji_0036;
                break;
            case 0x0037:
                icon = R.drawable.emoji_0037;
                break;
            case 0x0038:
                icon = R.drawable.emoji_0038;
                break;
            case 0x0039:
                icon = R.drawable.emoji_0039;
                break;
            case 0x0030:
                icon = R.drawable.emoji_0030;
                break;
            case 0x0023:
                icon = R.drawable.emoji_0023;
                break;
            default:
                break;
        }
        return icon;
    }

    private int findIconResourceWhenFollowUnicodeIsNot20e3(int unicode, int followUnicode) {
        int icon = 0;
        switch (unicode) {
            case 0x1f1ef:
                icon = (followUnicode == 0x1f1f5) ? R.drawable.emoji_1f1ef_1f1f5 : 0;
                break;
            case 0x1f1fa:
                icon = (followUnicode == 0x1f1f8) ? R.drawable.emoji_1f1fa_1f1f8 : 0;
                break;
            case 0x1f1eb:
                icon = (followUnicode == 0x1f1f7) ? R.drawable.emoji_1f1eb_1f1f7 : 0;
                break;
            case 0x1f1e9:
                icon = (followUnicode == 0x1f1ea) ? R.drawable.emoji_1f1e9_1f1ea : 0;
                break;
            case 0x1f1ee:
                icon = (followUnicode == 0x1f1f9) ? R.drawable.emoji_1f1ee_1f1f9 : 0;
                break;
            case 0x1f1ec:
                icon = (followUnicode == 0x1f1e7) ? R.drawable.emoji_1f1ec_1f1e7 : 0;
                break;
            case 0x1f1ea:
                icon = (followUnicode == 0x1f1f8) ? R.drawable.emoji_1f1ea_1f1f8 : 0;
                break;
            case 0x1f1f7:
                icon = (followUnicode == 0x1f1fa) ? R.drawable.emoji_1f1f7_1f1fa : 0;
                break;
            case 0x1f1e8:
                icon = (followUnicode == 0x1f1f3) ? R.drawable.emoji_1f1e8_1f1f3 : 0;
                break;
            case 0x1f1f0:
                icon = (followUnicode == 0x1f1f7) ? R.drawable.emoji_1f1f0_1f1f7 : 0;
                break;
            default:
                break;
        }
        return icon;
    }

    public boolean replaceWithEmojicons(Spannable text) {
        return replaceWithEmojicons(text, 0, -1);
    }

    public boolean replaceWithEmojicons(Spannable text, int index, int length) {
        if (mUseSystemDefault) {
            return false;
        }
        removeAllEmojiconSpan(text);

        final int textLength = text.length();
        final int textLengthToProcessMax = textLength - index;
        final int textLengthToProcess = length < 0 || length >= textLengthToProcessMax ? textLength : (length + index);

        int skip;
        boolean isAllEmoji = true;

        for (int i = index; i < textLengthToProcess; i += skip) {
            skip = 0;
            int icon = 0;
            char c = text.charAt(i);
            if (EmojiconCharToDrawableMap.isSoftBankEmoji(c)) {
                icon = EmojiconCharToDrawableMap.getSoftbankEmojiResource(c);
                skip = icon == 0 ? 0 : 1;
            }
            if (icon == 0) {
                int unicode = Character.codePointAt(text, i);
                skip = Character.charCount(unicode);

                if (unicode > 0xff) {
                    icon = EmojiconCharToDrawableMap.getEmojiResource(unicode);
                }

                if (icon == 0 && i + skip < text.length()) {
                    int followUnicode = Character.codePointAt(text, i + skip);
                    int followSkip = Character.charCount(followUnicode);
                    if (followUnicode == 0x20e3) {
                        icon = findIconResourceWhenFollowUnicodeIs20e3ByUnicode(unicode);
                    } else {
                        icon = findIconResourceWhenFollowUnicodeIsNot20e3(unicode, followUnicode);
                    }
                    if (icon > 0) {
                        skip += followSkip;
                    }
                }
            }
            if (icon > 0) {
                replaceWithEmojicon(text, icon, i, i + skip);
            } else {
                isAllEmoji = false;
            }
        }
        return isAllEmoji;
    }

    private boolean replaceWithEmojicon(Spannable text, int icon, int start, int end) {
        if (icon > 0) {
            text.setSpan(new EmojiconSpan(mContext, icon, mEmojiSize, mEmojiAlignment, mTextSize), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return true;
        }
        return false;
    }

    private int tryReplaceWithSoftBankEmoji(Spannable text, int index) {
        char c = text.charAt(index);
        if (!EmojiconCharToDrawableMap.isSoftBankEmoji(c)) {
            return 0;
        }
        int icon = EmojiconCharToDrawableMap.getSoftbankEmojiResource(c);
        boolean ret = replaceWithEmojicon(text, icon, index, index + 1);
        return ret ? 1 : 0;
    }
}
