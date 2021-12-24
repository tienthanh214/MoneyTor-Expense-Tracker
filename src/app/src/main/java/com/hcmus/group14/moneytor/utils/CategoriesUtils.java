package com.hcmus.group14.moneytor.utils;

import com.hcmus.group14.moneytor.services.options.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoriesUtils {
    static public List<Category> categories = null;
    public static List<Category> getDefaultCategories() {
        if (categories == null) {
            categories = new ArrayList<>();
            Collections.addAll(categories, Category.values());
        }
        return categories;
    }

    public static Category findCategoryById(String id) {
        for (Category cat: Category.values()) {
            if (cat.getId().equals(id))
                return cat;
        }
        return Category.OTHERS;
    }

    public static int findPositionById(String id) {
        for (int i = 0; i < categories.size(); ++i)
            if (categories.get(i).getId().equals(id))
                return i;
        return -1;
    }

    public static String getCategoryNameById(String id) {
        Category result = findCategoryById(id);
        if (result == null)
            return "";
        else
            return result.getName();
    }

    public static String getCategoryIdByPosition(Integer position) {
        if (position == null)
            return Category.OTHERS.getId();
        return categories.get(position).getId();
    }
}
