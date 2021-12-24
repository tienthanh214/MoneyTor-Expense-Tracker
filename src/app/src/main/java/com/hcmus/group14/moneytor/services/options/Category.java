package com.hcmus.group14.moneytor.services.options;


import android.graphics.Color;

import com.hcmus.group14.moneytor.R;

public enum Category
{
    OTHERS("Others", R.drawable.ic_category_others,
            Color.parseColor("#455a64")),
    FOOD_AND_DRINK("Food & Drink", R.drawable.ic_category_food_and_drink,
            Color.parseColor("#d32f2f")),       //Food and drink related
    TRAFFIC("Traffic", R.drawable.ic_category_traffic,
            Color.parseColor("#c2185b")),       //For vehicles, gasoline,...
    BILLS("Bills", R.drawable.ic_category_bills,
            Color.parseColor("#7b1fa2")),       //Cyclical bills (electricity, water, internet, Netflix...)
    MAINTENANCE("Maintenance", R.drawable.ic_category_maintenance,
            Color.parseColor("#512da8")),       //House/vehicle maintenance
    HEALTH("Health", R.drawable.ic_category_health,
            Color.parseColor("#303f9f")),       //Healthcare (drugs 'n stuff)
    EDUCATION("Education", R.drawable.ic_category_education,
            Color.parseColor("#1976d2")),       //Education and study related
    APPLIANCES("Appliances", R.drawable.ic_category_appliances,
            Color.parseColor("#0288d1")),       //Household appliances (TV, fridge,...)
    PETS("Pets", R.drawable.ic_category_pets,
            Color.parseColor("#0097a7")),       //Related to pets (if available), stuff like food, medical expenses...
    UTILITIES("Utilities", R.drawable.ic_category_utilities,
            Color.parseColor("#00796b")),       //Other stuff related to household (internet installment...)
    FITNESS("Fitness", R.drawable.ic_category_fitness,
            Color.parseColor("#388e3c")),       //Gym, aerobics, etc.
    MAKEUP("Makeup", R.drawable.ic_category_makeup,
            Color.parseColor("#689f38")),       //Beauty expenses
    ENTERTAINMENT("Entertainment", R.drawable.ic_category_entertainment,
            Color.parseColor("#afb42b"));       //For fun stuff

    private final String name;
    private final int resourceId;
    private final int color;
    // TODO: image for each category
    Category(String name, int resourceId, int color) {
        this.name = name;
        this.resourceId = resourceId;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.toString();
    }

    public int getResourceId(){
        return this.resourceId;
    }

    public int getColor() {
        return this.color;
    }
}

