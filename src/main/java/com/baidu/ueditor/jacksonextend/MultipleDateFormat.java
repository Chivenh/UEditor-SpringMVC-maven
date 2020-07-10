//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.ueditor.jacksonextend;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class MultipleDateFormat extends SimpleDateFormat {
    private static final long serialVersionUID = 1L;
    private static final Set<String> ALL_FORMATS;
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATETIME_FORMAT;
    public static final String DEFAULT_TIMESTAMP_FORMAT;
    private String[] patterns;

    public MultipleDateFormat() {
    }

    public MultipleDateFormat(String pattern, DateFormatSymbols formatSymbols) {
        super(pattern, formatSymbols);
    }

    public MultipleDateFormat(String pattern, Locale locale) {
        super(pattern, locale);
    }

    public MultipleDateFormat(String pattern) {
        super(pattern);
    }


    public static Date defaultParse(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        } else {
            Date date = null;
            Iterator var2 = ALL_FORMATS.iterator();

            ParsePosition pos;
            do {
                if (!var2.hasNext()) {
                    return null;
                }

                String pattern = (String)var2.next();
                pos = new ParsePosition(0);
                date = (new SimpleDateFormat(pattern)).parse(source, pos);
            } while(pos.getIndex() == 0 || pos.getIndex() < source.length());

            return date;
        }
    }

    @Override
    public Date parse(String source) throws ParseException {
        try {
            return super.parse(source);
        } catch (Exception var8) {
            if (this.patterns != null) {
                String[] var2 = this.patterns;
                int var3 = var2.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    String pattern = var2[var4];
                    ParsePosition pos = new ParsePosition(0);
                    Date date = (new SimpleDateFormat(pattern)).parse(source, pos);
                    if (pos.getIndex() != 0 && pos.getIndex() >= source.length()) {
                        return date;
                    }
                }
            }

            return defaultParse(source);
        }
    }

    @Override
    public Object parseObject(String source) throws ParseException {
        return this.parse(source);
    }

    public String[] getPatterns() {
        return this.patterns;
    }

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }

    static {
        DEFAULT_DATETIME_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;
        DEFAULT_TIMESTAMP_FORMAT = DEFAULT_DATETIME_FORMAT + ".SSSSSS";
        ALL_FORMATS = new LinkedHashSet<>();
        ALL_FORMATS.add("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
        ALL_FORMATS.add("yyyy-MM-dd'T'HH:mm:ss'Z'");
        ALL_FORMATS.add("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX");
        ALL_FORMATS.add("yyyy-MM-dd'T'HH:mm:ssX");
        ALL_FORMATS.add("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        ALL_FORMATS.add("yyyy-MM-dd'T'HH:mm:ss");
        ALL_FORMATS.add("yyyy-MM-dd HH:mm:ss.SSSSSS");
        ALL_FORMATS.add("yyyy-MM-dd HH:mm:ss");
        ALL_FORMATS.add("yyyy-MM-dd HH:mm");
        ALL_FORMATS.add("yyyy-MM-dd HH");
        ALL_FORMATS.add("yyyy-MM-dd");
        ALL_FORMATS.add("yyyy-MM");
        ALL_FORMATS.add("yyyy/MM/dd HH:mm:ss.SSSSSS");
        ALL_FORMATS.add("yyyy/MM/dd HH:mm:ss");
        ALL_FORMATS.add("yyyy/MM/dd HH:mm");
        ALL_FORMATS.add("yyyy/MM/dd HH");
        ALL_FORMATS.add("yyyy/MM/dd");
        ALL_FORMATS.add("yyyy/MM");
        ALL_FORMATS.add("dd/MM/yyyy HH:mm:ss.SSSSSS");
        ALL_FORMATS.add("dd/MM/yyyy HH:mm:ss");
        ALL_FORMATS.add("dd/MM/yyyy HH:mm");
        ALL_FORMATS.add("dd/MM/yyyy HH");
        ALL_FORMATS.add("dd/MM/yyyy");
        ALL_FORMATS.add("MM/dd/yyyy HH:mm:ss.SSSSSS");
        ALL_FORMATS.add("MM/dd/yyyy HH:mm:ss");
        ALL_FORMATS.add("MM/dd/yyyy HH:mm");
        ALL_FORMATS.add("MM/dd/yyyy HH");
        ALL_FORMATS.add("MM/dd/yyyy");
        ALL_FORMATS.add("yyyy\\MM\\dd HH:mm:ss.SSSSSS");
        ALL_FORMATS.add("yyyy\\MM\\dd HH:mm:ss");
        ALL_FORMATS.add("yyyy\\MM\\dd HH:mm");
        ALL_FORMATS.add("yyyy\\MM\\dd HH");
        ALL_FORMATS.add("yyyy\\MM\\dd");
        ALL_FORMATS.add("yyyy\\MM");
        ALL_FORMATS.add("yyyy.MM.dd HH:mm:ss.SSSSSS");
        ALL_FORMATS.add("yyyy.MM.dd HH:mm:ss");
        ALL_FORMATS.add("yyyy.MM.dd HH:mm");
        ALL_FORMATS.add("yyyy.MM.dd HHmmssSSSSSS");
        ALL_FORMATS.add("yyyy.MM.dd HHmmss");
        ALL_FORMATS.add("yyyy.MM.dd HHmm");
        ALL_FORMATS.add("yyyy.MM.dd HH");
        ALL_FORMATS.add("yyyy.MM.dd");
        ALL_FORMATS.add("yyyy.MM");
        ALL_FORMATS.add("yyyyMMdd:HHmmssSSSSSS");
        ALL_FORMATS.add("yyyyMMdd:HHmmss");
        ALL_FORMATS.add("yyyyMMdd:HHmm");
        ALL_FORMATS.add("yyyyMMdd:HH");
        ALL_FORMATS.add("yyyyMMdd HHmmssSSSSSS");
        ALL_FORMATS.add("yyyyMMdd HHmmss");
        ALL_FORMATS.add("yyyyMMdd HHmm");
        ALL_FORMATS.add("yyyyMMdd HH");
        ALL_FORMATS.add("yyyyMMddHHmmssSSSSSS");
        ALL_FORMATS.add("yyyyMMddHHmmss");
        ALL_FORMATS.add("yyyyMMddHHmm");
        ALL_FORMATS.add("yyyyMMdd");
        ALL_FORMATS.add("yyyyMM");
        ALL_FORMATS.add("yyyy年MM月dd日HH时mm分ss秒SSSSSS毫秒");
        ALL_FORMATS.add("yyyy年MM月dd日HH时mm分ss秒");
        ALL_FORMATS.add("yyyy年MM月dd日HH时mm分");
        ALL_FORMATS.add("yyyy年MM月dd日HH时");
        ALL_FORMATS.add("yyyy年MM月dd日");
        ALL_FORMATS.add("yyyy年MM月");
        ALL_FORMATS.add("yyyy年");
    }
}
