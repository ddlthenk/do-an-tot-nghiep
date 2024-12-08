package com.datn.commonbase.common;

public class HtmlTagRemover {
    public static String removeHtmlTags(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.replaceAll("<[^>]+>", " ").replaceAll("\\s+", " ").trim();
    }
}
